package taylor.project.ticket;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class TicketServiceImpl implements TicketService {
   
    private TicketRepository tickets;
    

    public TicketServiceImpl(TicketRepository trep){
        tickets = trep;
    }

    @Override
    public List<Ticket> listTickets() {
        return tickets.findAll();
    }

    
    // @Override
    // public Ticket getTicket(Long id){
    //     return tickets.findById(id).orElse(null);
    // }

    // @Override
    // public Ticket getTicketbySeatNumber(int seatNumber){
    //     return tickets.findBySeatNumber(seatNumber).orElse(null);
    // }

    @Override
    public List<Ticket> getTicketListbySeatNo(Integer seatNo) {
        return tickets.findTicketBySeatNo(seatNo);
    }

    
    @Override
    public Ticket getTicketBySeatRowNameAndSeatNo(String seatRowName, Integer seatNo) {
        return tickets.findBySeatRowNameAndSeatNo(seatRowName, seatNo);
    }

    @Override
    public Character getTicketStatus(Long id) {
        Ticket ticket = tickets.findById(id).orElse(null);
        if(ticket != null) {
            return ticket.getTicketStatus();
        }
        return null;
    }

    @Override
    public Ticket addTicket(Ticket ticket) {
        return tickets.save(ticket);
    }
    
    @Override
    public Ticket updateTicket(Long id, Ticket newTicketInfo){
        return tickets.findById(id).map(ticket -> {ticket.setUserId(newTicketInfo.getUserId());
                                                     ticket.setSectorName(newTicketInfo.getSectorName());
                                                       ticket.setSeatRowName(newTicketInfo.getSeatRowName());
                                                        ticket.setSeatNo(newTicketInfo.getSeatNo());
                                                         ticket.setPrice(newTicketInfo.getPrice());
                                                          ticket.setTicketStatus(newTicketInfo.getTicketStatus());
            return tickets.save(ticket);
    }).orElse(null);

    }

    /**
     * Remove a ticket with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a ticket will also remove all its associated reviews
     */
    @Override
    public void deleteTicket(Long id){
        tickets.deleteById(id);
    }
}