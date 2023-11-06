package taylor.project.concert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import taylor.project.sector.*;

@RestController
public class ConcertController {
    private ConcertService concertService;

    public ConcertController(ConcertService cs){
        this.concertService = cs;
    }

    /**
     * List all concerts in the system
     * @return list of all concerts
     */
    @GetMapping("/allconcerts")
    public List<Concert> getConcerts(){
        return concertService.listConcerts();
    }

    /**
     * Search for concert with the given id
     * If there is no concert with the given "id", throw a concertNotFoundException
     * @param id
     * @return concert with the given id
     */
    @GetMapping("/concerts/byId/{id}")
    public Concert getConcertById(@PathVariable Long id){
        Concert concert = concertService.getConcertById(id);

        // Need to handle "concert not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(concert == null) throw new ConcertNotFoundException(id);
        return concertService.getConcertById(id);

    }


    /**
     * Search for concerts containing the given name
     * @param concertName
     * @return list of concerts containing the given name
     */
    @GetMapping("/concerts/byName/{concertName}")
    public List<Concert> getConcertsByNameContaining(@PathVariable String concertName) {
        List<Concert> concerts = concertService.getConcertsByName(concertName);

        if(concerts.size() == 0) throw new ConcertNotFoundException(concertName);
        // Handles an empty list by throwing a HTTP 404 exception
        return concerts;
    }


    /**
     * Add a new concert with POST request to "/concerts"
     * Note the use of @RequestBody
     * @param concert
     * @return list of all concerts
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/concerts")
    public Concert addConcert(@Valid @RequestBody Concert concert) {
        return concertService.addConcert(concert);
    }

    @GetMapping("/sectorRowAvailability/{concertId}/{sectorName}")
    public ResponseEntity<List<String>> getSectorRowAvailability(@PathVariable Long concertId, @PathVariable String sectorName) {
        List<String> seatData = concertService.getSectorRowAvailability(concertId, sectorName);
        if (seatData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seatData);
    }

    /**
     * If there is no concert with the given "id", throw a concertNotFoundException
     * @param id
     * @param newconcertInfo
     * @return the updated, or newly added concert
     */
    @PutMapping("/concerts/{id}")
    public Concert updateConcert(@PathVariable Long id, @Valid @RequestBody Concert newconcertInfo){
        Concert concert = concertService.updateConcert(id, newconcertInfo);
        if(concert == null) throw new ConcertNotFoundException(id);
        
        return concert;
    }

    /**
     * Remove a concert with the DELETE request to "/concerts/{id}"
     * If there is no concert with the given "id", throw a concertNotFoundException
     * @param id
     */
    @DeleteMapping("/concerts/{id}")
    public void deleteConcert(@PathVariable Long id){
        try{
            concertService.deleteConcert(id);
         }catch(EmptyResultDataAccessException e) {
            throw new ConcertNotFoundException(id);
         }
    }
}