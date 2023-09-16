package taylor.project.concert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
public class Concert {
    //ID tagged to all concerts
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    private int ticketQuantity;
    private boolean isSoldOut = false;
    //for handling gallery uploads
    @Column(nullable = true, length = 10000000)
    private byte[] photo;

    @NotNull(message = "Error: Concert name cannot be empty.")
    // null elements are considered valid, so we need a size constraints too
    @Size(min = 5, max = 200, message = "Error: Concert name should be at least 5 characters long")
    private String concertName;
    
    
    //@JsonIgnore

    // public Concert(String concertName, int ticketQuantity) {
    //     this.concertName = concertName;
    //     this.ticketQuantity = ticketQuantity;
    // }

    public Concert(String concertName, int ticketQuantity, byte[] photo) throws IOException{
        this.concertName = concertName;
        this.ticketQuantity = ticketQuantity;
        if (photo == null || photo.length == 0) this.photo = Files.readAllBytes(Paths.get("src/main/resources/static/concert_posters/Poster_Placeholder.png"));
        else this.photo = photo;
    }
    
}