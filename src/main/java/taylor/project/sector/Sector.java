package taylor.project.sector;

import java.io.File;
import java.util.*;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import taylor.project.venue.Venue;
import taylor.project.concert.Concert;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

// @IdClass(SectorId.class)
public class Sector{
    //ID tagged to all sectors
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //sectorName != id.
    private String sectorName;

    private double ticketPrice;
    private double sectorSize;
    
    private boolean isGeneralStanding = false;
    private double seatsLeft;

    // due to sql limitations, a map cannot be inserted into a database.
    // as a result, the rowNames (key) and the seats in each row (value)
    // have to be stored as separate attributes.
    @ElementCollection
    @CollectionTable(name="listOfRows")
    private List<String> rowNames; // key

    @ElementCollection
    @CollectionTable(name="seatAvailability")
    private List<String> seats; // value

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnore
    private Venue venue;

    public Sector(Venue v, String sectorName, double ticketPrice, String[] rowNames, Integer[] totalSeatsInRow){
        venue = v;
        this.sectorName = sectorName;
        this.ticketPrice = ticketPrice;
        this.rowNames = Arrays.asList(rowNames);

        this.seats = new ArrayList<>();

        if (rowNames.length != 1 && totalSeatsInRow.length != 1){
            for (int row : totalSeatsInRow){
                int i = 0;
                String avail = "";
                while (i < row){
                    avail += "A";
                    i++;
                }
                    seats.add(avail);
            }
        } else {
            isGeneralStanding = true;
            seatsLeft = totalSeatsInRow[0];
            sectorSize = totalSeatsInRow[0];
        }
    
    }

    //for testing
    public String toString(){
        return getSectorName();
    }
    
}