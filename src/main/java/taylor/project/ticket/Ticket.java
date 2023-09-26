package taylor.project.ticket;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import taylor.project.concert.*;

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
    private Long userId;
    private String ticketType;
    
    private Long sector;
    private String seatDetails;
    private Double price;

    private Character ticketStatus;

    public Ticket(Long userId, String ticketType, Long sector, String seatDetails, Double price, Character ticketStatus) {
        this.userId = userId;
        this.ticketType = ticketType;
        this.sector = sector;
        this.seatDetails = seatDetails;
        this.price = price;
        this.ticketStatus = ticketStatus;
    }
    
    
    //@JsonIgnore
    
    
}