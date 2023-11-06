package taylor.project.sector;

import java.util.List;

import taylor.project.venue.Venue;

public interface SectorService {
    List<Sector> listSectors();
    Sector getSectorById(Long id);
    Sector addSector(Sector Sector);
    Sector updateSector(Long id, Sector Sector);
    void updateSelectedSeatsToStatus(Venue venue, List<String> selectedSeats, String sectorName, char newStatus);
    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteSector(Long id);
}