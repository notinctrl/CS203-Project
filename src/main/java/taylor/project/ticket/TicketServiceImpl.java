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

    @Override
    public Ticket getTicketById(Long id){
        return tickets.findById(id).orElse(null);
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
        if (!t.isPresent() || !u.isPresent()){

            throw new RuntimeException("Ticket/User not found");

        } else if (isValidStatus(statusUppercased)){
            Ticket ticket = t.get();
            User user = u.get();

            // ticket.setTicketStatus(statusUppercased);

            // 1) if changing the ticket to unavailable...
            if (statusUppercased == 'U') {
                // 2 scenarios: (1) from the cart to purchased (2) from the marketplace to purchased
                // firstly, get the list of new user's purchased ticekts
                List<Ticket> userPurTickets = user.getPurchasedTickets();

                // (1) check if carted. if it is, remove ticket from cart and place in purch
                if (ticket.getCartedUser() != null && ticket.getTicketStatus() == 'P'){
                    // get the cart
                    List<Ticket> shoppingCart = user.getShoppingCart();

                    //change the ticket status and change cartedUser to boughtUser
                    ticket.setTicketStatus('U');
                    ticket.setCartedUser(null);
                    ticket.setBoughtUser(user);

                    // remove the carted ticket from the cart and add it to purchased
                    shoppingCart.remove(ticket);
                    userPurTickets.add(ticket);

                    // set the cart and purch after use
                    user.setShoppingCart(shoppingCart);
                    user.setPurchasedTickets(userPurTickets);
                } 
                // (2) if it isnt in someones cart AND it has status M, ticket is from marketplace
                else if (ticket.getTicketStatus() == 'M'){
                    // get the selling user, because we need to remove his sold ticket frm purch ticket
                    User sellingUser = ticket.getBoughtUser();
                    List<Ticket> sellerPurTickets = sellingUser.getPurchasedTickets();

                    // remove the ticket from the seller and set it + commit changes
                    sellerPurTickets.remove(ticket);
                    sellingUser.setPurchasedTickets(sellerPurTickets);
                    users.save(sellingUser);

                    //change the ticket status, and change boughtUser to user(the buyer)
                    ticket.setTicketStatus('U');
                    ticket.setBoughtUser(user);

                    // add the ticket to the buyer's purTickets and set it
                    List<Ticket> buyerPurTickets = user.getPurchasedTickets();
                    buyerPurTickets.add(ticket);
                    user.setPurchasedTickets(buyerPurTickets);
                }
                // runtime exception if unexpected behaviour occured
                else throw new RuntimeException("Illegal status change operation: from " + ticket.getTicketStatus() + "to U");
            } 
            
            //  2) if putting the ticket into cart
            else if (statusUppercased == 'P'){
                // check if ticket is A, because that is the only status it can be before it goes to P
                if (ticket.getTicketStatus() == 'A'){
                    ticket.setCartedUser(user);
                    ticket.setTicketStatus('P');
                    List<Ticket> userShoppingCart = user.getShoppingCart();
                    userShoppingCart.add(ticket);
                    user.setShoppingCart(userShoppingCart);
                }
                // runtime exception if unexpected behaviour occured
                else throw new RuntimeException("Illegal status change operation: from " + ticket.getTicketStatus() + "to P");
            }
            
            // 3) making the ticket available again (timer expired)
            else if (statusUppercased == 'A'){
                // check if ticket is A, because that is the only status it can be before it goes to P
                if (ticket.getTicketStatus() == 'P'){
                    ticket.setCartedUser(null);
                    ticket.setTicketStatus('A');
                    List<Ticket> userShoppingCart = user.getShoppingCart();
                    userShoppingCart.remove(ticket);
                    user.setShoppingCart(userShoppingCart);
                }
                // runtime exception if unexpected behaviour occured
                else throw new RuntimeException("Illegal status change operation: from " + ticket.getTicketStatus() + "to A");
            }
            
            // 4) if putting ticket into marketplace
            else if (statusUppercased == 'M'){
                // check if ticket is U, because that is the only status it can be before it goes to M
                if (ticket.getTicketStatus() == 'U'){
                    ticket.setCartedUser(null);
                    ticket.setTicketStatus('M');
                    List<Ticket> userShoppingCart = user.getShoppingCart();
                    userShoppingCart.remove(ticket);
                    user.setShoppingCart(userShoppingCart);
                }
                // runtime exception if unexpected behaviour occured
                else throw new RuntimeException("Illegal status change operation: from " + ticket.getTicketStatus() + "to M");
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