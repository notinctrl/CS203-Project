package taylor.project.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taylor.project.concert.Concert;

/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 */
@Repository
public interface TicketRepository extends JpaRepository <Ticket, Long> {
    Ticket findBySeatRowNameAndSeatNo(String seatRowName, Integer seatNo);

    List<Ticket> findTicketBySeatNo(Integer seatNo);
    Optional<Ticket> findBySeatNo(Integer seatNo);
    Optional<Ticket> findTicketByConcertAndSectorNameAndSeatRowNameAndSeatNo(Concert concert, String sectName, String rowName, Integer seatNo);

}
