package taylor.project.ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import taylor.project.concert.Concert;
// import taylor.project.shoppingCart.ShoppingCart;
import taylor.project.user.User;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Ticket {
    //ID tagged to all concerts
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    /*TODO: make ticket a weak entity by having its partial identifiers to concertID.
        follow business logic in google docs.
    */
    

    /*
     * Tracks which user has this ticket in their cart.
     * If ticket is avail, MUST BE NULL.
     * Will be null if boughtUser is NON-NULL. 
     */
    @ManyToOne
    @JoinColumn(name = "carted_user")
    @JsonManagedReference
    User cartedUser;

    /*
     * Tracks which user has already bought this ticket.
     *  If ticket is in marketplace, this field remains non-null until the ticket is sold
     *  to the new user.
     * this field can be non-null when . 
     */
    @ManyToOne
    @JoinColumn(name = "bought_user")
    @JsonManagedReference
    User boughtUser;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    @JsonManagedReference
    Concert concert;

    // @ManyToOne
    // @JoinColumn(name = "shoppingCart_id")
    // @JsonManagedReference
    // ShoppingCart shoppingCart;

    private String sectorName;
    private String seatRowName;
    private Integer seatNo;
    private Double price;

    private Character ticketStatus;

    public Ticket(Concert concert, String sectorName, String seatRowName, Integer seatNo, Double price) {
        cartedUser = null;
        boughtUser = null;
        this.concert = concert;
        this.sectorName = sectorName;
        this.seatRowName = seatRowName;
        this.seatNo = seatNo;
        this.price = price;
        this.ticketStatus = 'A';
    }
    
    
    
}