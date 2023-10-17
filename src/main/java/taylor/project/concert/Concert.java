package taylor.project.concert;

import java.io.File;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import taylor.project.venue.Venue;

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

    @NotNull(message = "Error: Concert name cannot be empty.")
    @Size(min = 5, max = 200, message = "Error: Concert name should be at least 5 characters long")
    private String concertName;

    private int ticketQuantity;

    private boolean isSoldOut = false;

    // for which day of the concert
    @NotNull(message = "Error: Concert date range cannot be empty.")
    private String dateRange;

    // starting time for concert
    @NotNull(message =  "Error: Concert time cannot be empty.")
    private LocalTime startTime;

    @NotNull(message = "Error: Concert venue cannot be empty.")
    @OneToOne(mappedBy="concert",
            cascade = CascadeType.ALL)
    private Venue concertVenue;

    //for handling gallery uploads
    @Column(nullable = true, length = 10000000)
    private File photo;

    
    
    //@JsonIgnore

    public Concert(String concertName, int ticketQuantity, String dateRange, String startTime, Venue concertVenue, String photoPath) {
        this.concertName = concertName;
        this.ticketQuantity = ticketQuantity;
        this.dateRange = dateRange;
        this.startTime = LocalTime.parse(startTime);
        this.concertVenue = concertVenue;
        if (photoPath == null || photoPath.length() == 0) this.photo = new File("src/main/resources/static/concert_posters/Poster_Placeholder.png");
        else this.photo = new File(photoPath);
    }    
    
}