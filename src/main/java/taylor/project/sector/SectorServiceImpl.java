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

    public boolean checkValidStatusChange(char currentStatus, char toChange){
        if ((currentStatus == 'A' && toChange == 'P') || (currentStatus == 'P' && toChange == 'U')
            || (currentStatus == 'P' && toChange == 'P'))
            return true;

        return false;
    }

    /**Updates the seat status and the ticket status of the specified selected seats in
     * that belong to the sector. to refactor.
     * 
     * @param concert       : the concert that the selected seat belongs to.
     * @param sectors       : the list of sectors the concert has
     * @param rowNamesToFind
     * @param selectedSeats
     * @param sectorName
     * @param newStatus
     * @param userId
     */
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
                        String finalSeatString = changeSeatToPending(seatString, seatNo);
                        
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

    public String changeSeatToPending(String seatString, String seatNumStringed){
        // minus one because the index of seats starts from 0.
// System.out.println("seat no is" + seatNumStringed);
        int seatNumberidx = Integer.parseInt(seatNumStringed) - 1;
// System.out.println("seat no after is" + seatNumberidx);
        StringBuilder sb = new StringBuilder(seatString);

        // check if current seat status is available.
        // if it is, change the seat to Pending
        // if it isnt, throw exception
        if (seatString.charAt(seatNumberidx) == 'A'){
            sb.setCharAt(seatNumberidx, 'P');
// System.out.println("new seat string is " + sb.toString());
        } else {
            throw new RuntimeException("Illegal seat status change: from " + seatString.indexOf(seatNumberidx) + " to P");
        }

        return sb.toString();
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