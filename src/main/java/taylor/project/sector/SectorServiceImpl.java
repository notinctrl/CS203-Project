package taylor.project.sector;

import java.util.*;

import org.springframework.stereotype.Service;
import taylor.project.sector.exceptions.SectorExistsException;
import taylor.project.venue.*;
import taylor.project.ticket.*;
import taylor.project.concert.*;

@Service
public class SectorServiceImpl implements SectorService {
   
    private SectorRepository sectors;
    private TicketService ticketService;
    private ConcertService concertService;
    
    public SectorServiceImpl(SectorRepository sectors, TicketService ts, ConcertService cs){
        this.sectors = sectors;
        ticketService = ts;
        concertService = cs;
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


    /**Updates a sector's selected seats to pending (P). [NOTE BELOW:]
     * NOTE: A seat can only turn into P if its original status is Available(A). Else, an exception will be thrown.
     * 
     * @param concert       : the concert that contains the selected sector.
     * @param sectorName    : the selected sector's name.
     * @param selectedSeats : the selected seats, in the form of a string list. 
     *                          format of each seat = [row name]:[seatNo] e.g: "A:11", "AA:5"
     */
    public void updateSectorSeatsToPending(Long concertId, String sectorName, List<String> selectedSeats){
        // 1. find sector that matches this name for the specified concert
        Concert concert = concertService.getConcertById(concertId);
        if (concert == null){
            throw new RuntimeException("Exception in sectsvcimpl, updateSectorSeatsToPending: concert not found");
        }
        Sector sectorToChange = getSpecifiedSector(concert, sectorName);
        if (sectorToChange == null) {
            throw new RuntimeException("Exception in sectsvcimpl, updateSectorSeatsToPending: sector not found");
        }

        // 1.5. if the sector is general standing, refer to function referenced below.
        if (sectorToChange.isGeneralStanding()){
            sectorToChange = updateGeneralStandingToPending(sectorToChange, selectedSeats);
        } else {
            // 2. go thru entire selectedSeats list and change the seatAvail for each selected seat to Pending
            List<String> rowNames = sectorToChange.getRowNames();
            List<String> seats = sectorToChange.getSeats();

            // go thru entire list of selected seats to change the seat status in sector's seats variable.
            for (String seat : selectedSeats){

                // 3. separate the rowName and seatNo
                String[] seatDetails = seat.split(":");
                String seatRowName = seatDetails[0];
                String seatNo = seatDetails[1];

                // 4. find the rowName's index. this index is used to find its corresp. row of seats, which is represented by
                //      a string. (e.g: if all seats are avail, the seat string looks like this: "AAAAAAAAAAA")
                for (int rowNameidx = 0; rowNameidx < rowNames.size(); rowNameidx++){
                    if (seatRowName.equals(rowNames.get(rowNameidx))){

                        // 5. with the rowNameidx found, i can now find the seat string, and the seat status of the selected seat.
                        String seatString = seats.get(rowNameidx);

                        // 6. change the selected seat to Pending, provided it is a legal change (mentioned on top).
                        String finalSeatString = updateSeatToPending(seatString, seatNo);
                        
                        // 7. set the new seat string back into the seats list
                        seats.set(rowNameidx, finalSeatString);
                    }
                }
            }
            // 8. once finished setting all the seats, set the seats list in sectorToChange
            sectorToChange.setSeats(seats);
            System.out.println("new sector layout is:");
            for (String str : sectorToChange.getSeats()){
                System.out.println(str);
            }
        }

        // 9. persist the changes to the sectors database.
        sectors.save(sectorToChange);
    }

    /**Changes the actual seat string content to pending.
     * 
     * @param seatString
     * @param seatNumStringed
     * @return
     */
    public String updateSeatToPending(String seatString, String seatNumStringed){
        // minus one because the index of seats starts from 0.
        int seatNumberidx = Integer.parseInt(seatNumStringed) - 1;
        StringBuilder sb = new StringBuilder(seatString);

        // check if current seat status is available.
        // if it is, change the seat to Pending
        // if it isnt, throw exception
        if (seatString.charAt(seatNumberidx) == 'A'){
            sb.setCharAt(seatNumberidx, 'P');
        } else {
            throw new RuntimeException("Illegal seat status change: from " + seatString.indexOf(seatNumberidx) + " to P");
        }

        return sb.toString();
    }

    /**Changes general standing sectors' quantity of seats according to the number of seats selected.
     * In this case, the selectedSeats list only contains the quantity of seats selected, which will
     * be used to edit the number of seats left within this standing sector.
     * 
     * @param sectorToChange
     * @param selectedSeats
     * @return
     */
    public Sector updateGeneralStandingToPending(Sector sectorToChange, List<String> selectedSeats){

        // 1. find number of seats selected for this sector. 
        int numOfSeatsSelected = Integer.parseInt(selectedSeats.get(0));
        
        // 2. check if there are seats available for selection.
        //      throw an exception if the selection exceeds seatsLeft.
        double seatsLeft = sectorToChange.getSeatsLeft();
        if (seatsLeft < numOfSeatsSelected) {
            throw new RuntimeException("Seats selected outweighs seats left, go again.");
        } else {
            seatsLeft -= numOfSeatsSelected;
        }

        Sector finalSectorToReturn = sectorToChange;
        finalSectorToReturn.setSeatsLeft(seatsLeft);
        return finalSectorToReturn;
    }

    /**Searches a concert to find a sector by sectorName
     * 
     * @param c
     * @param sName
     * @return
     */
    public Sector getSpecifiedSector(Concert c, String sName){
        Venue v = c.getConcertVenue();
        List<Sector> sectors = v.getSectors();
        for (Sector s : sectors){
            if (s.getSectorName().equals(sName)){
                return s;
            }
        }
        return null;
    }

    /**Updates a sector row's seats to Unavailable[U] as specified by the ticket
     * NOTE: Only seats with initial status Pending[P] can be changed. If not, an exception will be thrown.
     */
    public void updateSectorSeatsToUnavail(Long ticketId, Long userId){
        Ticket t = ticketService.getTicketById(ticketId);
        if (t == null) throw new RuntimeException("Ticket not found");

        String ticketSectName = t.getSectorName();
        String ticketRowName = t.getSeatRowName();
        Integer ticketSeatNo = t.getSeatNo();

        // 1. We need to get the exact sector and its seats from ticket
        Concert concert = t.getConcert();
        Venue v = concert.getConcertVenue();
        List<Sector> sectors = v.getSectors();

        for (Sector sector : sectors){
            if (sector.getSectorName().equals(ticketSectName)){
                // special case: ticket belongs to general standing area. stop the function because no altering of seat status req.
                if (sector.isGeneralStanding()){
                    return;
                }
                // 2. find the row and status of the seat reflected on the sector
                List<String> sectorRowNames = sector.getRowNames();
                List<String> sectorSeats = sector.getSeats();

                // 3. go thru rowNames and find a match. note down index, to be used to find row of seats.
                for (int rowNameIdx = 0; rowNameIdx < sectorRowNames.size(); rowNameIdx++){
                    if (sectorRowNames.get(rowNameIdx).equals(ticketRowName)){
                        StringBuilder rowSeatsToChange = new StringBuilder(sectorSeats.get(rowNameIdx));
                        // first seat starts at 0.
                        Integer ticketSeatNoIdx = ticketSeatNo - 1;
                        // 4. check if the index for the seat we are changing is Pending.
                        if (rowSeatsToChange.charAt(ticketSeatNoIdx) == 'P'){
                            rowSeatsToChange.setCharAt(ticketSeatNoIdx, 'U');
                        } else {
                            throw new RuntimeException("Illegal status change: from " + rowSeatsToChange.charAt(ticketSeatNoIdx) + " to U");
                        }

                        sectorSeats.set(rowNameIdx, rowSeatsToChange.toString());
                    }
                }
            }
        }
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
}