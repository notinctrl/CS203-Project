package taylor.project.sector;

import java.util.*;

import org.hibernate.mapping.Set;
import org.springframework.stereotype.Service;
import taylor.project.sector.exceptions.SectorExistsException;
import taylor.project.venue.*;
import taylor.project.ticket.*;
import taylor.project.concert.*;

@Service
public class SectorServiceImpl implements SectorService {
   
    private SectorRepository sectors;
    private TicketService ticketService;
    
    public SectorServiceImpl(SectorRepository sectors, TicketService ts){
        this.sectors = sectors;
        ticketService = ts;
    }

    @Override
    public List<Sector> listSectors() {
        return sectors.findAll();
    }

    
    @Override
    public Sector getSectorById(Long id){
        return sectors.findById(id).orElse(null);
    }

    @Override
    public Sector addSector(Sector sector) {
        // Check if a sector with the same name and venue already exists
        if (sectors.existsBySectorNameAndVenue(sector.getSectorName(), sector.getVenue())) {
            throw new SectorExistsException(sector);
        }

        // If no duplicate, save the sector
        return sectors.save(sector);
    }
    
    @Override
    public Sector updateSector(Long id, Sector newSectorInfo){
        return sectors.findById(id).map(sector -> 
                                                {sector.setId(newSectorInfo.getId());
                                                sector.setSectorSize(newSectorInfo.getSectorSize());
                                                sector.setRowNames(newSectorInfo.getRowNames());
                                                sector.setSeats(newSectorInfo.getSeats());
                                                sector.setVenue(newSectorInfo.getVenue());
            return sectors.save(sector);
    }).orElse(null);

    }

    /**
     * Changes selected seat status
     */
    @Override
    public void updateSelectedSeatsToStatus(Venue venue, List<String> selectedSeats, String sectorName, char newStatus, Long userId){
        switch(newStatus){
            case 'P': case 'U':
                break;
            default:
                System.out.println("Not valid status: input character 'P' (pending) or 'U' only");
                return;
        }
        List<Sector> sectors = venue.getSectors();
        TreeSet<String> rowNamesToFind = new TreeSet<>();
        for (String seat : selectedSeats){
            String[] seatDetails = seat.split(":");

            // if the length of the split is 1, its because this seat only has 1 row, 
            // meaning that its a general standing area. so stop looking!
            if (seatDetails.length == 1) break;

            rowNamesToFind.add(seatDetails[0]);
        }

        Concert c = venue.getConcert();

        findSeatUpdateStatusAndTickets(c, sectors, rowNamesToFind, selectedSeats, sectorName, newStatus, userId);
    }

    /**
     * Remove a sector with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a sector will also remove all its associated reviews
     */
    @Override
    public void deleteSector(Long id){
        sectors.deleteById(id);
    }

    public boolean checkValidStatusChange(char currentStatus, char toChange){
        if ((currentStatus == 'A' && toChange == 'P') || (currentStatus == 'P' && toChange == 'U')
            || (currentStatus == 'P' && toChange == 'P'))
            return true;

        return false;
    }

    public void findSeatUpdateStatusAndTickets(Concert concert, List<Sector> sectors, TreeSet<String> rowNamesToFind, 
                                            List<String> selectedSeats, String sectorName, char newStatus, Long userId){
        for (Sector sector : sectors){
            Long id = sector.getId();
            // sector found
            if (sector.getSectorName().equals(sectorName)){
                // special case: a general standing area has no row names to find (size == 0),
                // so we need to specially execute this.
                if (rowNamesToFind.size() == 0){
                    // (1) get how many seats left
                    double seatsLeft = sector.getSeatsLeft();
                    // (2) change seats left based on selSeats
                    double seatsToBook = Double.parseDouble(selectedSeats.get(0));
                    seatsLeft -= seatsToBook;
                    sector.setSeatsLeft(seatsLeft);

                    List<Ticket> tickets = ticketService.getTicketsByConcertAndSectorName(concert, sectorName);

                    for (int i = 0; i < seatsToBook; i++){
                        for (Ticket t : tickets){
                            if (t.getTicketStatus() == 'A'){
                                Long ticketId = t.getId();
                                ticketService.setUserIdAndStatus(ticketId, userId, newStatus);
                                break;
                            }
                        }
                    }
                }
                // normal case: sector has more than one row
                else {
                    List<String> rowNames = sector.getRowNames();
                    List<String> sectSeats = sector.getSeats();
System.out.println("row names to find are " + rowNamesToFind);
                    for (String rowName : rowNames){
                        if (rowNamesToFind.contains(rowName)){

                            // find row index inside rowNames to get the index of the seats in seats List.
                            int rowIdx = rowNames.indexOf(rowName);

                            // get the seat string for this row and turn it into a char array (for easier manip)
                            char[] seatRowToAlter = sectSeats.get(rowIdx).toCharArray();

                            // go thru each selected seat to find the seats corresp to current row
                            for (String ss : selectedSeats){

                                //boolean to check if the status has been successfully changed.
                                boolean statusChangeSuccess = false;

                                // get the rowName and the seatNo from ss.
                                String[] seatDetails = ss.split(":");
                                int seatNo = Integer.valueOf(seatDetails[1]) - 1;

                                if (seatDetails[0].equals(rowName)){
                                    // boolean isValidSeatChange = checkValidStatusChange(seatRowToAlter[seatNo], newStatus);
                                    // if (isValidSeatChange){
                                        System.out.println("hit");
                                        seatRowToAlter[seatNo] = newStatus;
                                        statusChangeSuccess = true;
                                    // }
                                }

                                if (statusChangeSuccess){
                                    // find the ticket and update the information
                                    Ticket ticket = ticketService.findSpecificTicket(concert, sectorName, rowName, seatNo).get();
                                    if (ticket == null) throw new RuntimeException("Cannot find ticket for " + concert.getConcertName()
                                                                                     + " sectorName " + sectorName + ", seat " + rowName + seatNo);

                                    // edit the ticket's status accordingly. this function also
                                    // helps to persist ticket and user to repo.
                                    ticketService.setUserIdAndStatus(ticket.getId(), userId, newStatus);
                                }
                                // if status not changed successfully, throw runtime error
                                // else throw new RuntimeException("Could not change status in sector: Seat " + ss 
                                //                                     + " from " + seatRowToAlter[seatNo] + " to " + newStatus);
                            }
                            // set the new row into that sector
                            String newRow = new String(seatRowToAlter);
    // System.out.println("new seat layout on " + rowName + " : " + newRow);
                            sectSeats.add(rowIdx, newRow);
                            sectSeats.remove(rowIdx + 1);

                            // update sector's seats in list
                            sector.setSeats(sectSeats);
                            for (String str : sector.getSeats()){
                                System.out.println(str);
                            }
                        }
                    }
                }
            
            }

            // update the sector based on the new data in repo
            updateSector(id, sector);
        }
    }
}