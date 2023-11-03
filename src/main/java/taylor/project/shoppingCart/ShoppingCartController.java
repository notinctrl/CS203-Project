// package taylor.project.shoppingCart;

// import java.util.List;

// import javax.transaction.Transactional;
// import javax.validation.Valid;

// import org.springframework.dao.EmptyResultDataAccessException;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestController;

// import taylor.project.ticket.Ticket;

// @RestController
// public class ShoppingCartController {
//     private ShoppingCartService shoppingCartService;

//     public ShoppingCartController(ShoppingCartService shoppingCartService){
//         this.shoppingCartService = shoppingCartService;
//     }

//     /**
//      * List all shopping carts in the system that belongs to the user
//      * @return list of all shopping carts
//      */
//     @GetMapping("/shoppingcarts")
//     public List<ShoppingCart> getShoppingCarts(){
//         return shoppingCartService.listShoppingCarts();
//     }

//     /**
//      * Search for shopping cart with the given id
//      * If there is no shopping cart with the given "id", throw a ShoppingCartNotFoundException
//      * @param id
//      * @return shopping cart with the given id
//      */
//     @GetMapping("/shoppingcarts/{id}")
//     public ShoppingCart getShoppingCart(@PathVariable Long id){
//         ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(id);

//         // Need to handle "shopping cart not found" error using proper HTTP status code
//         // In this case, it should be HTTP 404
//         if(shoppingCart == null) throw new ShoppingCartNotFoundException(id);
//         return shoppingCartService.getShoppingCart(id);

//     }
//     /**
//      * Add a new shopping cart with POST request to "/shoppingcarts"
//      * Note the use of @RequestBody
//      * @param shoppingCart
//      * @return list of all shopping carts
//      */
//     @ResponseStatus(HttpStatus.CREATED)
//     @PostMapping("/shoppingcarts")
//     public ShoppingCart addShoppingCart(@Valid @RequestBody ShoppingCart shoppingCart) {
//         return shoppingCartService.addShoppingCart(shoppingCart);
//     }

//     /**
//      * If there is no shopping cart with the given "id", throw a ShoppingCartNotFoundException
//      * @param id
//      * @param newShoppingCartInfo
//      * @return the updated, or newly added shopping cart
//      */
//     @PutMapping("/shoppingcarts/{id}")
//     public ShoppingCart updateShoppingCart(@PathVariable Long id, @Valid @RequestBody ShoppingCart newShoppingCartInfo){
//         ShoppingCart shoppingCart = shoppingCartService.updateShoppingCart(id, newShoppingCartInfo);
//         if(shoppingCart == null) throw new ShoppingCartNotFoundException(id);
        
//         return shoppingCart;
//     }

//     /**
//      * Remove a shopping cart with the DELETE request to "/shoppingcarts/{id}"
//      * If there is no shoping cart with the given "id", throw a ShoppingCartNotFoundException
//      * @param id
//      */
//     @DeleteMapping("/shoppingcarts/{id}")
//     public void deleteShoppingCart(@PathVariable Long id){
//         try{
//             shoppingCartService.deleteShoppingCart(id);
//          }catch(EmptyResultDataAccessException e) {
//             throw new ShoppingCartNotFoundException(id);
//          }
//     }

//     @GetMapping("/user/{userId}/shoppingcart")
//     public ResponseEntity<List<Ticket>> getCartTickets(@PathVariable Long userId) {
//         List<Ticket> ticketData = shoppingCartService.getCartTickets(userId);
//         if (ticketData == null) {
//             return ResponseEntity.notFound().build();
//         }
//         return ResponseEntity.ok(ticketData);
//     }

//     // @GetMapping("/shoppingcarts/{id}/tickets")
//     // @Transactional
//     // public List<Ticket> getTicketsForShoppingCart(@PathVariable Long id) {
//     //     return shoppingCartService.getTicketList(id);
//     // }
// }