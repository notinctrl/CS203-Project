package taylor.project.shoppingCart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShoppingCartNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ShoppingCartNotFoundException(Long id) {
        super("Could not find shopping cart with id " + id);
    }
}
