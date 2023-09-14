package taylor.project.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 */
@Repository
public interface TicketRepository extends JpaRepository <Ticket, Long> {

}
