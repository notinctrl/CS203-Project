package taylor.project.concert;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 */
@Repository
public interface ConcertRepository extends JpaRepository <Concert, Long> {
    List<Concert> findByConcertNameContainingIgnoreCase(String concertName);
}
