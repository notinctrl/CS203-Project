// package taylor.project.shoppingCart;

// import java.util.List;

// import javax.transaction.Transactional;

// import org.springframework.stereotype.Service;

// import taylor.project.ticket.Ticket;
// import taylor.project.ticket.TicketRepository;
// import taylor.project.ticket.TicketService;
// import taylor.project.user.User;
// import taylor.project.user.UserRepository;


// @Service
// public class ShoppingCartServiceImpl implements ShoppingCartService {
   
//     private ShoppingCartRepository shoppingCarts;
//     private TicketRepository ticketRepository;
//     private TicketService ticketService;
//     private UserRepository ur;

//     public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCarts, TicketRepository ticketRepository, TicketService ticketService, UserRepository ur) {
//         this.shoppingCarts = shoppingCarts;
//         this.ticketRepository = ticketRepository;
//         this.ticketService = ticketService;
//         this.ur = ur;
//     }

//     @Override
//     public List<ShoppingCart> listShoppingCarts() {
//         return shoppingCarts.findAll();
//     }

    
//     @Override
//     public ShoppingCart getShoppingCart(Long id){
//         return shoppingCarts.findById(id).orElse(null);
//     }
    
//     @Override
//     public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
//         return shoppingCarts.save(shoppingCart);
//     }
    
//     @Override
//     public ShoppingCart updateShoppingCart(Long id, ShoppingCart newShoppingCartInfo){
//         return shoppingCarts.findById(id).map(shoppingCart -> {shoppingCart.setUser(newShoppingCartInfo.getUser());
//                                                                 shoppingCart.setTicketList(newShoppingCartInfo.getTicketList());
//                                                                  shoppingCart.setTotalPrice(newShoppingCartInfo.getTotalPrice());
//             return shoppingCarts.save(shoppingCart);
//     }).orElse(null);

//     }

//     @Override
//     public void addTicketById(Long id, Ticket ticket) {
//         ShoppingCart shoppingCart = shoppingCarts.findById(id).orElse(null);
//         if (shoppingCart != null) {
//             shoppingCart.getTicketList().add(ticket);
//             shoppingCarts.save(shoppingCart);
//         }
//     }

//     @Override
//     public void addTicketBySeatRowNameAndSeatNo(Long id, String seatRowName, Integer seatNo){
//         ShoppingCart shoppingCart = shoppingCarts.findById(id).orElse(null);
//         Ticket ticket = ticketRepository.findBySeatRowNameAndSeatNo(seatRowName, seatNo);

//         try {
//             ticketService.setUserIdAndStatus(ticket.getId(), shoppingCart.getUser().getId(), 'P');
//             if(shoppingCart != null && ticket != null) {
//                 shoppingCart.getTicketList().add(ticket);
//                 shoppingCarts.save(shoppingCart);
//             }
//         } catch (Exception e){
//             System.out.println(e.getMessage());
//         }
//     }

//     @Transactional
//     public List<Ticket> getCartTickets(Long userId){
//         User u = ur.findById(userId).get();
//         System.out.println(shoppingCarts.findByUser(u).get().getTotalPrice());
//         return shoppingCarts.findByUser(u).get().getTicketList();
//     }

//     /**
//      * Remove a ticket with the given id
//      * Spring Data JPA does not return a value for delete operation
//      * Cascading: removing a ticket will also remove all its associated reviews
//      */
//     @Override
//     public void deleteShoppingCart(Long id){
//         shoppingCarts.deleteById(id);
//     }
// }