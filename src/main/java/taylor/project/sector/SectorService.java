package taylor.project.sector;

import java.util.List;

public interface SectorService {
    List<Sector> listSectors();
    Sector getSectorById(String id);
    Sector addSector(Sector Sector);
    Sector updateSector(String id, Sector Sector);
    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteSector(String id);
}