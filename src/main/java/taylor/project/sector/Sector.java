package taylor.project.sector;

import java.util.*;
import java.io.File;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Sector {
    //ID tagged to all sectors
    @Id
    private String id;

    private double ticketPrice;
    private int sectorSize;
    @Column(nullable = true, length = 10000000)
    @NotNull(message = "Error: You must provide a seat layout for the sector.")
    private File seatLayout;
    private TreeMap<String, ArrayList<Character>> seats;

    // @NotNull(message = "Error: Concert name cannot be empty.")
    // @Size(min = 5, max = 200, message = "Error: Concert name should be at least 5 characters long")

    // @NotNull(message = "Error: Concert venue cannot be empty.")
    // @Size(min = 5, max = 200, message = "Error: Concert venue should be at least 5 characters long")

    
    
    //@JsonIgnore

    public Sector(String id, double price, String[] rowNames, int[] totalSeatsInRow, String seatLayoutPath) {
        this.id = id;
        ticketPrice = price;
        seats = new TreeMap<String, ArrayList<Character>>();
        int rowidx = 0;
        for (int row : totalSeatsInRow){
            int i = 0;
            ArrayList<Character> seatAvailability = new ArrayList<>();
            while (i < row){
                seatAvailability.add('A');
                i++;
            }
            seats.put(rowNames[rowidx++], seatAvailability);
        }
        if (seatLayoutPath == null || seatLayoutPath.length() == 0); //do some error
        else this.seatLayout = new File(seatLayoutPath);
    }
    
}