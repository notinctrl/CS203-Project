package taylor.project.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import taylor.project.user.*;


@Service
public class TicketServiceImpl implements TicketService {
   
    private TicketRepository tickets;
    private UserRepository users;
    

    public TicketServiceImpl(TicketRepository trep, UserRepository users){
        tickets = trep;
        this.users = users;
    }

    @Override
    public List<Ticket> listTickets() {
        return tickets.findAll();
    }

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
        return tickets.findById(id).map(ticket -> {ticket.setCartedUser(newTicketInfo.getCartedUser());
                                                    ticket.setBoughtUser(newTicketInfo.getBoughtUser());
                                                    ticket.setConcert(newTicketInfo.getConcert());
                                                     ticket.setSectorName(newTicketInfo.getSectorName());
                                                       ticket.setSeatRowName(newTicketInfo.getSeatRowName());
                                                        ticket.setSeatNo(newTicketInfo.getSeatNo());
                                                         ticket.setPrice(newTicketInfo.getPrice());
                                                          ticket.setTicketStatus(newTicketInfo.getTicketStatus());
            return tickets.save(ticket);
        }).orElse(null);
    }

    /**
     * Sets the ticket userId (if null) and its status to the specified status.
     * Status is ONE CHARACTER, IN UPPERCASE.
     * @param userId
     * @param status
     */
    public void setUserIdAndStatus(Long ticketId, Long userid, char status){
        Optional<Ticket> t = tickets.findById(ticketId);
        Optional<User> u = users.findById(userid);
        char statusUppercased = Character.toUpperCase(status);
        if (!t.isPresent() || !!u.isPresent()){

            throw new RuntimeException("Ticket/User not found");

        } else if (isValidStatus(statusUppercased)){
            Ticket ticket = t.get();
            User user = u.get();

            ticket.setTicketStatus(statusUppercased);

            if (statusUppercased == 'U') {
                List<Ticket> userPurTickets = user.getPurchasedTickets();
                ticket.setBoughtUser(user);
                ticket.setCartedUser(null);
                userPurTickets.add(ticket);
                user.setPurchasedTickets(userPurTickets);
            } else if (statusUppercased == 'P'){
                ticket.setCartedUser(user);
                List<Ticket> userShoppingCart = user.getShoppingCart();
                userShoppingCart.add(ticket);
                user.setPurchasedTickets(userShoppingCart);
            }

            // update repo
            users.save(user);
            tickets.save(ticket);

        } else throw new RuntimeException("Provide a valid status. (A/P/U/M)");

        /**Possible Ticket statuses (in capital char)
         * A - Available    : seat is available for booking and is not reserved by anybody
         *                      Ticket userid is NULL.
         * 
         * P - Pending      : seat is in someone's shopping cart and not avail for purchase.
         *                      Ticket userid is NON-NULL.
         * 
         * U - Unavailable  : seat has been purchased after a successful transaction.
         *                      Ticket userid is NON-NULL.
         * 
         * M - Marketplace  : seat is available for purchase, but Ticket is still within the
         *                      ownership of the selling user. 
         *                      Ticket userid is NON-NULL.
         */
    }

    public boolean isValidStatus(char status){
        return status == 'P' || status == 'U' || status == 'M' || status == 'A';
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