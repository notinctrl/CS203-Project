package taylor.project.sector;

import java.util.*;
import java.util.Optional;

import org.hibernate.mapping.Set;
import org.springframework.stereotype.Service;
import taylor.project.sector.exceptions.SectorExistsException;
import taylor.project.venue.*;

@Service
public class SectorServiceImpl implements SectorService {
   
    private SectorRepository sectors;
    
    public SectorServiceImpl(SectorRepository sectors){
        this.sectors = sectors;
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
    public void updateSelectedSectorSeatsToStatus(Venue venue, List<String> selectedSeats, String sectorName, char newStatus){
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
            rowNamesToFind.add(seatDetails[0]);
        }
        findSeatAndUpdateStatus(sectors, rowNamesToFind, selectedSeats, sectorName, newStatus);
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
        if ((currentStatus == 'A' && toChange == 'P') || (currentStatus == 'P' && toChange == 'U'))
            return true;

        return false;
    }

    public void findSeatAndUpdateStatus(List<Sector> sectors, TreeSet<String> rowNamesToFind, 
                                            List<String> selectedSeats, String sectorName, char newStatus){
        for (Sector sector : sectors){
            Long id = sector.getId();
            if (sector.getSectorName().equals(sectorName)){
                List<String> rowNames = sector.getRowNames();
                List<String> sectSeats = sector.getSeats();
                for (String rowName : rowNames){
                    if (rowNamesToFind.contains(rowName)){

                        // find row index inside rowNames to get the index of the seats in seats List.
                        int rowIdx = rowNames.indexOf(rowName);

                        // get the seat string for this row and turn it into a char array (for easier manip)
                        char[] seatRowToAlter = sectSeats.get(rowIdx).toCharArray();

                        // go thru selected seats to find the seats corresp to current row
                        for (String ss : selectedSeats){

                            String[] seatDetails = ss.split(":");

                            if (seatDetails[0].equals(rowName)){
                                int seatNo = Integer.valueOf(seatDetails[1]) - 1;
                                boolean isValidSeatChange = checkValidStatusChange(seatRowToAlter[seatNo], newStatus);
                                if (isValidSeatChange){
                                    seatRowToAlter[seatNo] = newStatus;
                                }
                            }
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

            // update the sector based on the new data in repo
            updateSector(id, sector);
        }
    }
}