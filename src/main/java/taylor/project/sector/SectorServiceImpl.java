package taylor.project.sector;

import java.util.List;

import org.springframework.stereotype.Service;


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
    public List<Sector> getSectorsByName(String sectorName) {
        return sectors.findBySectorNameContainingIgnoreCase(sectorName);
    }

    @Override
    public Sector addSector(Sector sector) {
        return sectors.save(sector);
    }
    
    @Override
    public Sector updateSector(Long id, Sector newSectorInfo){
        return sectors.findById(id).map(sector -> {sector.setId(newSectorInfo.getId());
                                                    sector.setSeatLayout(newSectorInfo.getSeatLayout());
                                                    sector.setSeats(newSectorInfo.getSeats());
            return sectors.save(sector);
    }).orElse(null);

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