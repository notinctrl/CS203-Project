package taylor.project.shoppingCart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import taylor.project.ticket.Ticket;
import taylor.project.ticket.TicketRepository;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
   
    private ShoppingCartRepository shoppingCarts;
    private TicketRepository ticketRepository;

    @Autowired
    public ShoppingCartServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCarts){
        this.shoppingCarts = shoppingCarts;
    }

    @Override
    public List<ShoppingCart> listShoppingCarts() {
        return shoppingCarts.findAll();
    }

    
    @Override
    public ShoppingCart getShoppingCart(Long id){
        return shoppingCarts.findById(id).orElse(null);
    }
    
    @Override
    public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCarts.save(shoppingCart);
    }
    
    @Override
    public ShoppingCart updateShoppingCart(Long id, ShoppingCart newShoppingCartInfo){
        return shoppingCarts.findById(id).map(shoppingCart -> {shoppingCart.setUserID(newShoppingCartInfo.getUserID());
                                                                shoppingCart.setTicketList(newShoppingCartInfo.getTicketList());
                                                                 shoppingCart.setTotalPrice(newShoppingCartInfo.getTotalPrice());
            return shoppingCarts.save(shoppingCart);
    }).orElse(null);

    }

    @Override
    public void addTicketById(Long id, Ticket ticket) {
        ShoppingCart shoppingCart = shoppingCarts.findById(id).orElse(null);
        if (shoppingCart != null) {
            shoppingCart.getTicketList().add(ticket);
            shoppingCarts.save(shoppingCart);
        }
    }

    @Override
    public void addTicketBySeatRowNameAndSeatNo(Long id, String seatRowName, Integer seatNo) {
        ShoppingCart shoppingCart = shoppingCarts.findById(id).orElse(null);
        Ticket ticket = ticketRepository.findBySeatRowNameAndSeatNo(seatRowName, seatNo);
        if(shoppingCart != null && ticket != null) {
            shoppingCart.getTicketList().add(ticket);
            shoppingCarts.save(shoppingCart);
        }
    }

    /**
     * Remove a ticket with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a ticket will also remove all its associated reviews
     */
    @Override
    public void deleteShoppingCart(Long id){
        shoppingCarts.deleteById(id);
    }
}