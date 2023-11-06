// package taylor.project.shoppingCart;

// import java.util.ArrayList;
// import java.util.List;

// import javax.persistence.CascadeType;
// import javax.persistence.Entity;
// import javax.persistence.FetchType;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.OneToMany;
// import javax.persistence.OneToOne;
// import javax.transaction.Transactional;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import com.fasterxml.jackson.annotation.JsonManagedReference;

// import lombok.AllArgsConstructor;
// import lombok.EqualsAndHashCode;
// import lombok.Getter;
// import lombok.Setter;
// import lombok.ToString;
// import taylor.project.ticket.Ticket;
// import taylor.project.user.User;
 
// @Entity
// @Getter
// @Setter
// @ToString
// @AllArgsConstructor
// @EqualsAndHashCode
// // public class ShoppingCart {
// //     //ID tagged to all concerts
// //     private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

// //     private ArrayList<Ticket> ticketList;
// //     private double totalPrice;

// //     public ShoppingCart(ArrayList<Ticket> ticketList, double totalPrice) {
// //         this.ticketList = ticketList;
// //         this.totalPrice = totalPrice;
// //     }

// //     //@JsonIgnore
// // }

// public class ShoppingCart {
//     //ID tagged to all concerts
//     private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

//     // @NotNull(message = "Error: User ID cannot be empty.")
//     // @Min(value = 1, message = "Error: User ID should be greater than or equal to 1.")
//     // private Long userID;

//     // @OneToMany(mappedBy = "shoppingCart",
//     //             cascade = CascadeType.ALL)
//     // @JsonManagedReference
//     // private List<Ticket> ticketList;

//     private double totalPrice;
//     // private boolean isGuestCart;
//     // private boolean isPurchased;

//     @OneToOne
//     @JoinColumn (name = "user_id")
//     // @JsonBackReference
//     private User user;

//     // public ShoppingCart() {
//     //     this.ticketList = new ArrayList<Ticket>();
//     //     this.totalPrice = 0.0;
//     // }

//     public Long getShoppingCartUserId() {
//         return this.user.getId();
//     }

//     // @Transactional
//     // public List<Ticket> getTicketList(){
//     //     return this.ticketList;
//     // }

// }