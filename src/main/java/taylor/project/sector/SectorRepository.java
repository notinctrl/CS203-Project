package taylor.project.sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taylor.project.venue.Venue;


/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 */
@Repository
public interface SectorRepository extends JpaRepository <Sector, Long> {
    boolean existsBySectorNameAndVenue(String sName, Venue v);
}
