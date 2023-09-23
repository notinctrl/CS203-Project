package taylor.project.sector;

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
    private Long id;

    private int sectorSize;
    @Column(nullable = true, length = 10000000)
    @NotNull(message = "Error: You must provide a seat layout for the sector.")
    private File seatLayout;
    private ArrayList<ArrayList<Boolean>> seats;

    // @NotNull(message = "Error: Concert name cannot be empty.")
    // @Size(min = 5, max = 200, message = "Error: Concert name should be at least 5 characters long")

    // @NotNull(message = "Error: Concert venue cannot be empty.")
    // @Size(min = 5, max = 200, message = "Error: Concert venue should be at least 5 characters long")

    
    
    //@JsonIgnore

    public Sector(Long id, int[] totalSeatsInRow, String seatLayoutPath) {
        this.id = id;
        seats = new ArrayList<ArrayList<Boolean>>();
        for (int row : totalSeatsInRow){
            int i = 0;
            ArrayList<Boolean> seatAvailability = new ArrayList<>();
            while (i < row){
                seatAvailability.add(false);
                i++;
            }
            seats.add(seatAvailability);
        }
        if (seatLayoutPath == null || seatLayoutPath.length() == 0) ;
        else this.seatLayout = new File(seatLayoutPath);
    }
    
}