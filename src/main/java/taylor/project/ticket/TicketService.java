package taylor.project.ticket;

import java.util.List;
import java.util.Optional;

import taylor.project.concert.Concert;

import javax.validation.Valid;

public interface TicketService {
    List<Ticket> listTickets();

    Ticket getTicketById(Long id);

    Ticket getTicketBySeatRowNameAndSeatNo(String seatRowName, Integer seatNo);
    //Ticket getTicketbySeatNumber(int seatNumber);
    List<Ticket> getTicketListbySeatNo(Integer seatNo);
   //Ticket getTicket(Long id);
    Ticket addTicket(Ticket ticket);
    Ticket updateTicket(Long id, Ticket ticket);
    Character getTicketStatus(Long id);

    Optional<Ticket> findSpecificTicket(Concert c, String sectName, String rowName, Integer seatNo);

    List<Ticket> getTicketsByConcertAndSectorName(Concert c, String sectorName);

    void changeTicketStatusToPending(Long concertId, String sectorName, List<String> selectedSeats, Long userId);

    void checkoutTicket(Long ticketId, Long userId);

    void addTicketToMarketplace(Long ticketId);

    void rmvTicketFromMarketplace(Long ticketId, Long userId);

    void buyFromMarketplace(Long ticketId, Long buyerUserId);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteTicket(Long id);

     //for ticket to be shown as sold
    Ticket pendingStatus(Long id, Character newStatus);

}