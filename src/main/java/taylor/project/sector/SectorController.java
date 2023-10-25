package taylor.project.sector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import taylor.project.sector.exceptions.SectorExistsException;
import taylor.project.sector.exceptions.SectorNotFoundException;

@RestController
public class SectorController {
    private SectorService sectorService;

    public SectorController(SectorService ss){
        this.sectorService = ss;
    }

    /**
     * List all sectors in the system
     * @return list of all sectors
     */
    @GetMapping("/sectors")
    public List<Sector> getSectors(){
        return sectorService.listSectors();
    }

    /**
     * Search for sector with the given id
     * If there is no sector with the given "id", throw a sectorNotFoundException
     * @param id
     * @return sector with the given id
     */
    @GetMapping("/sectors/byId/{id}")
    public Sector getSectorById(@PathVariable String id){
        Sector sector = sectorService.getSectorById(Long.parseLong(id));

        // Need to handle "sector not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(sector == null) throw new SectorNotFoundException(id);
        return sectorService.getSectorById(Long.parseLong(id));

    }

    /**
     * Add a new sector with POST request to "/sectors"
     * Note the use of @RequestBody
     * @param sector
     * @return list of all sectors
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sectors")
    public Sector addSector(@Valid @RequestBody Sector sector) {
        Sector s = null;
        try {
            s = sectorService.addSector(sector);
        } catch (SectorExistsException e){
            System.out.println(e.getMessage());
            return null;
        }
        return s;
    }

    /**
     * If there is no sector with the given "id", throw a sectorNotFoundException
     * @param id
     * @param newsectorInfo
     * @return the updated, or newly added sector
     */
    @PutMapping("/sectors/{id}")
    public Sector updateSector(@PathVariable String id, @Valid @RequestBody Sector newsectorInfo){
        Sector sector = sectorService.updateSector(Long.parseLong(id), newsectorInfo);
        if(sector == null) throw new SectorNotFoundException(id);
        
        return sector;
    }

    /**
     * Remove a sector with the DELETE request to "/sectors/{id}"
     * If there is no sector with the given "id", throw a sectorNotFoundException
     * @param id
     */
    @DeleteMapping("/sectors/{id}")
    public void deleteSector(@PathVariable String id){
        try{
            sectorService.deleteSector(Long.parseLong(id));
         }catch(EmptyResultDataAccessException e) {
            throw new SectorNotFoundException(id);
         }
    }
}