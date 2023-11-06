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

    void setUserIdAndStatus(Long ticketid, Long userid, char status);

    Optional<Ticket> findSpecificTicket(Concert c, String sectName, String rowName, Integer seatNo);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteTicket(Long id);

     //for ticket to be shown as sold
    Ticket pendingStatus(Long id, Character newStatus);

}