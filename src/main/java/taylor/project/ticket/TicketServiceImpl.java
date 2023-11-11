package taylor.project.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import taylor.project.user.*;
import taylor.project.concert.*;
import taylor.project.sector.Sector;


@Service
public class TicketServiceImpl implements TicketService {
   
    private TicketRepository tickets;
    private UserRepository users;
    private ConcertService concertService;

    public TicketServiceImpl(TicketRepository trep, UserRepository users, ConcertService cs){
        tickets = trep;
        this.users = users;
        concertService = cs;
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

    public void changeTicketStatusToPending(Long concertId, String sectorName, List<String> selectedSeats, Long userId){
        Concert c = concertService.getConcertById(concertId);
        // find out if this sector is a general standing sector. if it is, redirect to function below.
        List<Sector> sectors = c.getConcertVenue().getSectors();
        for (Sector s : sectors){
            if (s.getSectorName().equals(sectorName) && s.isGeneralStanding()){
                changeTicketStatusToPendingGeneral(concertId, s, selectedSeats, userId);
                return;
            }
        }
        // 1. go thru the selected seat list and find the ticket based on the seat details
        for (String seat : selectedSeats){
            String[] seatDetails = seat.split(":");
            String rowName = seatDetails[0];
            Integer seatNo = Integer.parseInt(seatDetails[1]);
            Ticket ticket = findSpecificTicket(c, sectorName, rowName, seatNo).get();
            if (ticket == null){
                throw new RuntimeException("Exception in ticksvcimpl, changeTicketStatusToPending: ticket not found");
            }

            // 2. check if the ticket is valid to change from available to pending. refer below for function.
            updateTicketToPending(ticket, userId);
        }
    }

    /**Deals with general standing sector seats and turns tickets to pending.
     * 
     * @param concertId
     * @param sector
     * @param selectedSeats
     * @param userId
     */
    public void changeTicketStatusToPendingGeneral(Long concertId, Sector sector, List<String> selectedSeats, Long userId){
        double seatsLeft = sector.getSeatsLeft();
        int seatsToBook = Integer.parseInt(selectedSeats.get(0));

        if (seatsToBook > seatsLeft){
            throw new RuntimeException("Seats to book is greater than total seats left.");
        }
        Concert c = concertService.getConcertById(concertId);
        List<Ticket> sectorTickets = tickets.findTicketsByConcertAndSectorName(c, sector.getSectorName());
        User user = users.findById(userId).get();

        if (user == null){
            throw new RuntimeException("User not found");
        }

        int count = 0;
        for (Ticket t : sectorTickets){
            if (t.getTicketStatus() == 'A'){
                t.setTicketStatus('P');
                List<Ticket> shoppingCart = user.getShoppingCart();
                shoppingCart.add(t);
                user.setShoppingCart(shoppingCart);
                users.save(user);
                t.setCartedUser(user);
                tickets.save(t);
                count++;
            }
            if (count == seatsToBook) break;
        }
    }


    /**Checks if ticket is valid to change to Pending before executing the update [NOTE BELOW]
     * NOTE: the ticket can only be changed to Pending if:  1) BOTH its cartedUser and boughtUser fields are null.
     *                                                      2) Its original ticketStatus is 'A'.
     * An exception will be thrown if the ticket does not fulfill these.
     * If valid to change, the ticket status will be changed and the cartedUser will be set to the ticket buyer.
     * 
     * @param t
     * @param userId
     */
    public void updateTicketToPending(Ticket ticket, Long userId){
        if (ticket.getBoughtUser() != null || ticket.getCartedUser() != null || ticket.getTicketStatus() != 'A'){
            throw new RuntimeException("Illegal ticket status change from " + ticket.getTicketStatus() + " to P");
        } else {
            // Ticket finalTicket = t;
            User user = users.findById(userId).get();
            if (user == null){
                throw new RuntimeException("User not found");
            }
            
            ticket.setCartedUser(user);
            ticket.setTicketStatus('P');
            List<Ticket> shoppingCart = user.getShoppingCart();
            if (!shoppingCart.contains(ticket)){
                shoppingCart.add(ticket);
                user.setShoppingCart(shoppingCart);
            } else {
                throw new RuntimeException("Illegal operation: Ticket already exists in shopping cart.");
            }

            // persist the changes to the ticket repository.
            tickets.save(ticket);
            users.save(user);
        }
    }


    /**Checks out selected ticket using the specified userID, changing ticket status from Pending[P] to Unavailable[U].
     * 
     * @param ticketId
     * @param userId
     */
    public void checkoutTicket(Long ticketId, Long userId){
        Ticket t = tickets.findById(ticketId).get();
        if (t == null) throw new RuntimeException("Ticket not found");

        if (isValidForCheckout(t, userId)){
            // 1. Get user so that we cna edit his purchased ticket list and shoppingCart
            User user = users.findById(userId).get();
            if (user == null) throw new RuntimeException("Checkout: User not found.");
            List<Ticket> shoppingCart = user.getShoppingCart();
            List<Ticket> purchasedTickets = user.getPurchasedTickets();

            if (shoppingCart.contains(t)){
                shoppingCart.remove(t);
            } else throw new RuntimeException("Ticket not found in shopping cart");

            t.setBoughtUser(user);
            t.setCartedUser(null);
            t.setTicketStatus('U');

            if (!purchasedTickets.contains(t)){
                purchasedTickets.add(t);
            } else throw new RuntimeException("Ticket already exists in purchased tickets");

            tickets.save(t);
            user.setShoppingCart(shoppingCart);
            user.setPurchasedTickets(purchasedTickets);
            users.save(user);
        }
    }

    /**Helps the function above it check if the ticket is valid to be added into the marketplace. [NOTE BELOW]
     * Acceptance criteria: 1) Ticket must have initial status of Pending[P].
     *                      2) Ticket must have a NULL boughtUser and NON-NULL cartedUser field.
     * An exception will be thrown if either/both are not met.
     * @param t
     * @return
     */
    public boolean isValidForCheckout(Ticket t, Long userId){
        User user = users.findById(userId).get();
        if (t.getTicketStatus() != 'P'){
            throw new RuntimeException("Illegal ticket status change: from " + t.getTicketStatus() + " to U");
        } else if (t.getBoughtUser() != null || t.getCartedUser() == null){
            String boughtUserName = null;
            String cartedUserName = null;
            if (t.getBoughtUser() != null) boughtUserName = t.getBoughtUser().getUsername();
            if (t.getBoughtUser() != null) cartedUserName = t.getCartedUser().getUsername();

            throw new RuntimeException("Invalid ticket state for pending ticket: boughtUser= " 
                                        + boughtUserName + " ticket cartedUser= " + cartedUserName);
        } else if (user == null){
            throw new RuntimeException("User not found");
        } else if (!user.equals(t.getCartedUser())){
            throw new RuntimeException("Illegal operation: carted user: " + t.getCartedUser().getUsername()
                                        + " does not match current user: " + user.getUsername());
        }
        return true;
    }


    /**Adds a ticket into marketplace, changing its status to Marketplace[M]. [NOTE BELOW]
     * NOTE: Only tickets that are Unavailable (status == U) are able to be placed on the marketplace.
     *       The ticket must have a NON-NULL boughtUser field and a NULL cartedUser field. 
     *       Else, an exception will be thrown.
     * 
     * @param t
     */
    public void addTicketToMarketplace(Long ticketId){
        Ticket t = tickets.findById(ticketId).get();
        if (t == null){
            throw new RuntimeException("Ticket to place in marketplace not found.");
        }
        if (isValidForMarketplace(t)){
            // 1. get the user who initially bought this ticket, to alter his purchased tickets list
            User user = t.getBoughtUser();
            List<Ticket> purchasedTickets = user.getPurchasedTickets();

            // 2. check if ticket is inside his purchased tickets. if not, how on earth did he manage to ini this cmd?
            if (!purchasedTickets.contains(t)){
                throw new RuntimeException("Add to marketplace failed: ticket not found in purchased tickets.");
            }

            // 3. remove the ticket from the purchased list and change the status of the ticket
            purchasedTickets.remove(t);
            t.setTicketStatus('M');

            // 4. commit both changes for the user and the ticket
            tickets.save(t);
            user.setPurchasedTickets(purchasedTickets);
            users.save(user);
        }
    }

    /**Helps the function above it check if the ticket is valid to be added into the marketplace. [NOTE BELOW]
     * Acceptance criteria: 1) Ticket must have initial status of Unavailable[U].
     *                      2) Ticket must have a NON-NULL boughtUser and NULL cartedUser field.
     * An exception will be thrown if either/both are not met.
     * @param t
     * @return
     */
    public boolean isValidForMarketplace(Ticket t){
        if (t.getTicketStatus() != 'U'){
            throw new RuntimeException("Illegal ticket status change: from " + t.getTicketStatus() + " to M");
        } else if (t.getBoughtUser() == null || t.getCartedUser() != null){
            throw new RuntimeException("Invalid ticket state for unavail. ticket boughtUser= " 
                                        + t.getBoughtUser() + " ticket cartedUser= " + t.getCartedUser().getUsername());
        }
        return true;
    }
    

    /**Removes a ticket from the marketplace by setting the ticket status back to Unavailable[U]. [NOTE BELOW]
     * NOTE: this method will throw an exception either: 1) if this ticket does not exist on the database.
     *                                                   2) if the ticket had just been bought by another user.
     * NOTE2: this method should only be used for sellers looking to remove the ticket from the marketplace themselves.
     * After the ticket status has been changed, the ticket will then be added back to the original owner's purchasedTickets list.
     * 
     * @param ticketId
     */
    public void rmvTicketFromMarketplace(Long ticketId, Long userId){
        Ticket t = tickets.findById(ticketId).get();
        User originalUser = users.findById(userId).get();

        if (t == null){
            throw new RuntimeException("Ticket not found");
        } else if (t.getTicketStatus() == 'U' && !t.getBoughtUser().equals(originalUser)){
            throw new RuntimeException("Error: ticket has just been bought by another user.");
        }

        // 1. get the user who initially bought this ticket, to alter his purchased tickets list
        User user = t.getBoughtUser();
        List<Ticket> purchasedTickets = user.getPurchasedTickets();

        // 2. add the ticket back to the purchased list and change the status of the ticket
        purchasedTickets.add(t);
        t.setTicketStatus('U');

        // 3. commit both changes for the user and the ticket
        tickets.save(t);
        user.setPurchasedTickets(purchasedTickets);
        users.save(user);
    }


    /**Removes a ticket from marketplace after A USER BUYS IT. Sets the ticket status to Unavailable[U]. [NOTE BELOW]
     * NOTE: this method will throw an exception either: 1) if this ticket does not exist on the database.
     *                                                   2) if the ticket had just been bought by another user.
     *                                                   3) if the ticket does not have a Marketplace[M] status
     *                                                   4) if the buyerUserId equals the boughtUser id on ticket.
     * NOTE2: this method should only be used when users buy from the marketplace.
     * Since the seller's purchased tickets list does not contain the marketplace ticket already,
     * the ticket will only have to be added to the buyer's purchasedTickets list.
     * @param ticketId
     * @param buyerUserId
     */
    @Override
    public void buyFromMarketplace(Long ticketId, Long buyerUserId){
        Ticket t = tickets.findById(ticketId).get();
        User buyerUser = users.findById(buyerUserId).get();

        if (t == null){
            throw new RuntimeException("Ticket not found");
        } else if (t.getTicketStatus() == 'U'){
            throw new RuntimeException("Error: ticket has just been bought by another user.");
        } else if (t.getBoughtUser().equals(buyerUser)){
            throw new RuntimeException("Illegal operation: buyer cannot buy his own ticket.");
        } else if (t.getTicketStatus() != 'M'){
            throw new RuntimeException("Illegal status change: cannot change ticket status from " + t.getTicketStatus() + " to U");
        }

        // 1. get buyer's purchased tickets to add the ticket in
        List<Ticket> buyerPurchasedTickets = buyerUser.getPurchasedTickets();

        // 2. remove the ticket from the purchased list and change the status of the ticket
        t.setTicketStatus('U');
        buyerPurchasedTickets.add(t);
        t.setBoughtUser(buyerUser);

        // 3. commit both changes for the user and the ticket
        tickets.save(t);
        buyerUser.setPurchasedTickets(buyerPurchasedTickets);
        users.save(buyerUser);
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

    public Optional<Ticket> findSpecificTicket(Concert c, String sectName, String rowName, Integer seatNo){
        return tickets.findTicketByConcertAndSectorNameAndSeatRowNameAndSeatNo(c, sectName, rowName, seatNo);
    }

    public List<Ticket> getTicketsByConcertAndSectorName(Concert c,  String sectName){
        return tickets.findTicketsByConcertAndSectorName(c, sectName);
    }
}