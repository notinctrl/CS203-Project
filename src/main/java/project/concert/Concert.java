package project.concert;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Concert {
    //ID tagged to all concerts
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    private int ticketQuantity;
    private boolean isSoldOut = false;

    @NotNull(message = "Error: Concert name cannot be empty.")
    // null elements are considered valid, so we need a size constraints too
    @Size(min = 5, max = 200, message = "Error: Concert name should be at least 5 characters long")
    private String concertName;
    
    
    //@JsonIgnore

    public Concert(String concertName, int ticketQuantity) {
        this.concertName = concertName;
        this.ticketQuantity = ticketQuantity;
    }
    
}