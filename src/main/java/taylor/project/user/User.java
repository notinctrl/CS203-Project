package taylor.project.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
// import taylor.project.shoppingCart.ShoppingCart;
import taylor.project.ticket.Ticket;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

/* Implementations of UserDetails to provide user information to Spring Security, 
e.g., what authorities (roles) are granted to the user and whether the account is enabled or not
*/
public class User implements UserDetails{

    private static final long serialVersionUID = 1L;
    // private static final AtomicLong counter = new AtomicLong();
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    
    @NotNull(message = "Username should not be null")
    @Size(min = 5, max = 20, message = "Username should be between 5 and 20 characters")
    private String username;
    
    @NotNull(message = "Password should not be null")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

    @NotNull(message = "Authorities should not be null")
    // We define two roles/authorities: ROLE_USER or ROLE_ADMIN
    private String authorities;

    //@Past(message = "Invalid date. Use format \"dd-MM-yyyy\"")
    @NotNull(message = "Birthday should not be null")
    private String birthday;
    
    @NotNull(message = "Email should not be null") @Email(message = "Please provide a valid email")

    @Email(message = "Please provide a valid email")
    @NotNull(message = "Email address should not be null")
    private String emailAddress;

    @NotNull(message = "Address should not be null")
    private String address;

    @JsonBackReference
    @OneToMany(mappedBy = "boughtUser")
    private List<Ticket> purchasedTickets;

    @OneToMany(mappedBy = "cartedUser")
    @JsonBackReference
    private List<Ticket> shoppingCart;

    public User(String username, String password, String birthday, String emailAddress, String address, String authorities){
        // this.emailAddress = emailAddress;
        // this.username = username;
        // this.password = password;
        // this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // this.id = counter.incrementAndGet();
        this.username = username;
        this.password = password;
        //this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.birthday = birthday;
        this.emailAddress=emailAddress;
        this.address = address;
        this.authorities = authorities;
        this.purchasedTickets = new ArrayList<Ticket>();
        this.shoppingCart = new ArrayList<Ticket>();
    }


    /* Return a collection of authorities (roles) granted to the user.
    */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
    }

    public List<Ticket> getShoppingCart(){
        if (shoppingCart == null){
            this.shoppingCart = new ArrayList<Ticket>();
        }

        return this.shoppingCart;
    }
    
    /*
    The various is___Expired() methods return a boolean to indicate whether
    or not the user’s account is enabled or expired.
    */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isPresent() {
        return false;
    }

}