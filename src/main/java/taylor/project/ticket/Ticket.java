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
    private String ticketType;
    private String seatDetails;
    private Character status;

    public Ticket(String ticketType, String seatDetails) {
        this.ticketType = ticketType;
        this.seatDetails = seatDetails;
    }
    
    
    //@JsonIgnore
    
    
}