package taylor.project.ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> listTickets();

    Ticket getTicketBySeatRowNameAndSeatNo(String seatRowName, Integer seatNo);
    //Ticket getTicketbySeatNumber(int seatNumber);
    List<Ticket> getTicketListbySeatNo(Integer seatNo);
   //Ticket getTicket(Long id);
    Ticket addTicket(Ticket ticket);
    Ticket updateTicket(Long id, Ticket ticket);
    Character getTicketStatus(Long id);

    void setUserIdAndStatus(Long ticketid, Long userid, char status);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteTicket(Long id);
}