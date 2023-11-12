package taylor.project.sector;

import java.util.List;


public interface SectorService {
    List<Sector> listSectors();
    Sector getSectorById(Long id);
    Sector addSector(Sector Sector);
    Sector updateSector(Long id, Sector Sector);

    void updateSectorSeatsToPending(Long concertId, String sectorName, List<String> selectedSeats);

    void updateSectorSeatsToUnavail(Long ticketId, Long userId);

    void deleteSector(Long id);
}