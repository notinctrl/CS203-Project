package taylor.project.ticket;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    User user;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    @JsonManagedReference
    Concert concert;

    private String sectorName;
    private String seatRowName;
    private Integer seatNo;
    private Double price;

    private Character ticketStatus;

    public Ticket(Concert concert, String sectorName, String seatRowName, Integer seatNo, Double price) {
        this.user = null;
        this.concert = concert;
        this.sectorName = sectorName;
        this.seatRowName = seatRowName;
        this.seatNo = seatNo;
        this.price = price;
        this.ticketStatus = 'A';
    }
    
    
    
}