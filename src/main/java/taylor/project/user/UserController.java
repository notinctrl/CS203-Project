package taylor.project.user;

import java.security.Principal;
import java.util.*;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import taylor.project.ticket.Ticket;

@RestController
public class UserController {
    @Autowired
    private UserRepository users;
    private BCryptPasswordEncoder encoder;
    private UserService userService;


    public UserController(UserService us, UserRepository ur, BCryptPasswordEncoder encoder){
        this.userService = us;
        this.users = ur;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {
        return users.findById(userId).get();
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username){
        User user = userService.getUser(username);

        // Need to handle "book not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(user == null) throw new UsernameNotFoundException(username);
        return userService.getUser(username);
    }

    @GetMapping("/users/{userId}/shoppingcart")
    public ResponseEntity<List<Ticket>> getShoppingCart(@PathVariable String userId){
        Optional<User> user = users.findById(Long.parseLong(userId));

        // if no user found, return null
        if (user == null) return ResponseEntity.notFound().build();

        // retrive purchased tickets
        return ResponseEntity.ok(user.get().getShoppingCart());
    }

    @GetMapping("/users/{userId}/purchasedtickets")
    public ResponseEntity<List<Ticket>> purchasedTickets(@PathVariable String userId){
        User user = users.findById(Long.parseLong(userId)).get();

        // if no user found, return null
        if (user == null) return ResponseEntity.notFound().build();

        // retrive purchased tickets
        return ResponseEntity.ok(user.getPurchasedTickets());
    }

    /**
    * Using BCrypt encoder to encrypt the password for storage 
    * ENCODE!!!
    * @param user
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userService.addUser(user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/adminUsers")
    public User addAdminUser(@Valid @RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userService.addUser(user);
    }

    @GetMapping("/currentDetail")
    public Principal currentDetail(Principal principal){
        return principal;
    }

    /**
    * @param id
     * @return
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        try{
            users.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }
   
}