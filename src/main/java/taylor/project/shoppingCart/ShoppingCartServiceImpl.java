package taylor.project.shoppingCart;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
   
    private ShoppingCartRepository shoppingCarts;
    

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
                                                                shoppingCart.setCartID(newShoppingCartInfo.getCartID());
            return shoppingCarts.save(shoppingCart);
    }).orElse(null);

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