package taylor.project.sector;

import java.util.List;

public interface SectorService {
    List<Sector> listSectors();
    Sector getSectorById(Long id);
    List<Sector> getSectorsByName(String SectorName);
    Sector addSector(Sector Sector);
    Sector updateSector(Long id, Sector Sector);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteSector(Long id);
}