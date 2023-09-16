package taylor.project.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserRepository users;
    private BCryptPasswordEncoder encoder;
    private UserService userService;

    

    // public UserController(UserService us){
    //    this.userService = us;
    // }

    public UserController(UserService users, BCryptPasswordEncoder encoder){
        this.userService = users;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username){
        User user = userService.getUser(username);

        // Need to handle "book not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(user == null) throw new UsernameNotFoundException(username);
        return userService.getUser(username);
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
   
}