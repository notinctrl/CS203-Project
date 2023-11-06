// package taylor.project.shoppingCart;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
// import java.util.*;

// import taylor.project.user.User;

// /**
//  * We only need this interface declaration
//  * Spring will automatically generate an implementation of the repo
//  * 
//  * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
//  */
// @Repository
// public interface ShoppingCartRepository extends JpaRepository <ShoppingCart, Long> {
//     Optional<ShoppingCart> findByUser(User user);
// }
