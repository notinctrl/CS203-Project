package taylor.project.concert;

import java.util.List;

import javax.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/concerts")
    public List<Concert> getConcerts(){
        return concertService.listConcerts();
    }

    /**
     * Search for concert with the given id
     * If there is no concert with the given "id", throw a concertNotFoundException
     * @param id
     * @return concert with the given id
     */
    @GetMapping("/concerts/{id}")
    public Concert getConcert(@PathVariable Long id){
        Concert concert = concertService.getConcert(id);

        // Need to handle "concert not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(concert == null) throw new ConcertNotFoundException(id);
        return concertService.getConcert(id);

    }
    /**
     * Add a new concert with POST request to "/concerts"
     * Note the use of @RequestBody
     * @param concert
     * @return list of all concerts
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/concerts")
    public Concert addconcert(@Valid @RequestBody Concert concert) {
        return concertService.addConcert(concert);
    }

    /**
     * If there is no concert with the given "id", throw a concertNotFoundException
     * @param id
     * @param newconcertInfo
     * @return the updated, or newly added concert
     */
    @PutMapping("/concerts/{id}")
    public Concert updateconcert(@PathVariable Long id, @Valid @RequestBody Concert newconcertInfo){
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
    public void deleteconcert(@PathVariable Long id){
        try{
            concertService.deleteConcert(id);
         }catch(EmptyResultDataAccessException e) {
            throw new ConcertNotFoundException(id);
         }
    }
}