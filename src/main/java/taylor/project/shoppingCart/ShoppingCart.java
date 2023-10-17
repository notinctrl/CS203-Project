package taylor.project.shoppingCart;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import taylor.project.concert.*;
import taylor.project.ticket.*;
import taylor.project.user.*;
 
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
// public class ShoppingCart {
//     //ID tagged to all concerts
//     private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

//     private ArrayList<Ticket> ticketList;
//     private double totalPrice;

//     public ShoppingCart(ArrayList<Ticket> ticketList, double totalPrice) {
//         this.ticketList = ticketList;
//         this.totalPrice = totalPrice;
//     }

//     //@JsonIgnore
// }

public class ShoppingCart {
    //ID tagged to all concerts
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    @NotNull(message = "Error: User ID cannot be empty.")
    @Min(value = 1, message = "Error: User ID should be greater than or equal to 1.")
    private Long userID;

    private ArrayList<Ticket> ticketList;
    private double totalPrice;
    // private boolean isGuestCart;
    // private boolean isPurchased;

    @OneToOne
    @JoinColumn (name = "user_id")
    @JsonIgnore
    private User user;

    public ShoppingCart(Long userID) {
        this.userID = userID;
        this.ticketList = new ArrayList<Ticket>();
        this.totalPrice = 0.0;
    }
    
    //@JsonIgnore
}