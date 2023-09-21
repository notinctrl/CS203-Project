package taylor.project.concert;

import java.util.List;
import java.time.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import org.apache.commons.io.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Concert {
    //ID tagged to all concerts
    private @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;

    private int ticketQuantity;
    private boolean isSoldOut = false;
    //for handling gallery uploads
    @Column(nullable = true, length = 10000000)
    private File photo;
    // private byte[] photo;

    private String date;

    @NotNull(message = "Error: Concert name cannot be empty.")
    // null elements are considered valid, so we need a size constraints too
    @Size(min = 5, max = 200, message = "Error: Concert name should be at least 5 characters long")
    private String concertName;

    @NotNull(message = "Error: Concert start date time cannot be empty.")
    private LocalDateTime startDateTime;

    @NotNull(message = "Error: Concert end date time cannot be empty.")
    private LocalDateTime endDateTime;

    @NotNull(message = "Error: Concert venue cannot be empty.")
    @Size(min = 5, max = 200, message = "Error: Concert venue should be at least 5 characters long")
    private String concertVenue;

    
    
    //@JsonIgnore

    public Concert(String concertName, int ticketQuantity, String startDateTime, String endDateTime, String concertVenue, String photoPath) {
        this.concertName = concertName;
        this.ticketQuantity = ticketQuantity;
        this.startDateTime = LocalDateTime.parse(startDateTime);
        this.endDateTime = LocalDateTime.parse(endDateTime);
        this.concertVenue = concertVenue;
        if (photoPath == null || photoPath.length() == 0) this.photo = new File("src/main/resources/static/concert_posters/Poster_Placeholder.png");
        else this.photo = new File(photoPath);
    }

    // public Concert(String concertName, int ticketQuantity) {
    //     this.concertName = concertName;
    //     this.ticketQuantity = ticketQuantity;
    // }

    // public Concert(String concertName, int ticketQuantity, String date, String photoPath) throws IOException{
    //     this.concertName = concertName;
    //     this.ticketQuantity = ticketQuantity;
    //     // this.dateTime = dateTime;
    //     this.date = date;
    //     if (photoPath == null || photoPath.length() == 0) this.photo = new File("src/main/resources/static/concert_posters/Poster_Placeholder.png");
    //     else this.photo = new File(photoPath);
    // }
    
    
}