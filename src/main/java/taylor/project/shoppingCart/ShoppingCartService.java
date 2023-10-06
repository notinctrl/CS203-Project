package taylor.project.shoppingCart;

import java.util.List;
import taylor.project.ticket.*;

public interface ShoppingCartService {
    List<ShoppingCart> listShoppingCarts();
    ShoppingCart getShoppingCart(Long id);
    ShoppingCart addShoppingCart(ShoppingCart shoppingCart);
    ShoppingCart updateShoppingCart(Long id, ShoppingCart shoppingCart);

    void addTicketById(Long id, Ticket ticket);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteShoppingCart(Long id);
}
