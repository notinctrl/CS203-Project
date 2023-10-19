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
import taylor.project.user.*;

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
    
    private User userId;

    private String sectorName;
    private String seatDetails;
    private Double price;

    private Character ticketStatus;

    public Ticket(String sectorName, String seatDetails, Double price) {
        this.userId = null;
        this.sectorName = sectorName;
        this.seatDetails = seatDetails;
        this.price = price;
        this.ticketStatus = 'A';
    }
    
    public void setSector(String sectorName) {
        this.sectorName = sectorName;
    }
    
    //@JsonIgnore
    
    
}