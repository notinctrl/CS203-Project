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
    private int sectorSize;
    // @Column(nullable = true, length = 10000000)
    @NotNull(message = "Error: You must provide a seat layout for the sector.")
    private File seatLayout;

    @ElementCollection
    @CollectionTable(name="listOfRows")
    private List<String> rowNames;

    @ElementCollection
    @CollectionTable(name="seatAvailability")
    private List<String> seats;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    //@JsonIgnore

    public Sector(Venue v, String sectorName, double ticketPrice, String[] rowNames, Integer[] totalSeatsInRow, String seatLayoutPath){
        venue = v;
        this.sectorName = sectorName;
        this.ticketPrice = ticketPrice;
        this.rowNames = Arrays.asList(rowNames);

        this.seats = new ArrayList<>();

        for (int row : totalSeatsInRow){
            int i = 0;
            String avail = "";
            while (i < row){
                avail += "A";
                i++;
            }
            seats.add(avail);
        }

        this.seatLayout = new File(seatLayoutPath);
    }

    //for testing
    public String toString(){
        return getSectorName();
    }
    
}