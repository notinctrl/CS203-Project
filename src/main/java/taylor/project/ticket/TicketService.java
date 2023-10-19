package taylor.project.ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> listTickets();
    Ticket getTicket(Long id);
    Ticket addTicket(Ticket ticket);
    Ticket updateTicket(Long id, Ticket ticket);
    Character getTicketStatus(Long id);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteTicket(Long id);
}