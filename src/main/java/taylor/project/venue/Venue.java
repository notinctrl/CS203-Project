package taylor.project.venue;

import java.util.*;
import java.io.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import taylor.project.sector.*;
import taylor.project.concert.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Venue {
    //ID tagged to all concerts
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    @NotNull(message = "Error: Venue cannot be empty.")
    @Size(min = 5, max = 200, message = "Error: Venue should be at least 5 characters long")
    private String venueName;

    private int venueSize;

    @OneToOne
    @JoinColumn (name = "concert_id")
    @JsonIgnore
    private Concert concert;

    @OneToMany(mappedBy = "venue",
                cascade = CascadeType.ALL)
    private List<Sector> sectors;

    //for handling gallery uploads
    private File overviewPhoto;

    public Venue(String vName, int vSize, String photoPath){
        venueName = vName;
        venueSize = vSize;
        overviewPhoto = new File(photoPath);
        // this.sectors = sectors;
        // if (photoPath == null || photoPath.length() == 0);
        // // throw new Exception("Please provide a photo for the venue showing the seat sectors.");
        // else
    }
    
    
}