package taylor.project.venue;

import java.util.*;
import java.io.*;
import org.apache.commons.io.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

import taylor.project.sector.*;

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

    private ArrayList<Sector> sectors;

    //for handling gallery uploads
    private File venueOverview;

    public Venue(String vName, int vSize, ArrayList<Sector> sectors, String photoPath) throws Exception{
        venueName = vName;
        venueSize = vSize;
        this.sectors = sectors;
        if (photoPath == null || photoPath.length() == 0) throw new Exception("Please provide a photo for the venue showing the seat sectors.");
        else venueOverview = new File(photoPath);
    }
    
    
}