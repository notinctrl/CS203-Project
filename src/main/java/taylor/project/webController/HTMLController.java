package taylor.project.webController;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertService;
import taylor.project.sector.*;

@Controller
public class HTMLController {

    private ConcertService concertService;
    private Long concertId;
    private String sectorName;
    private List<String> selectedSeats;

    public HTMLController(ConcertService cs){
        this.concertService = cs;
    }

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F://temp//";

    @GetMapping("/index")
    public String index(Model model) throws IOException{
        List<Concert> concerts = concertService.listConcerts();
        int num = 1;
        for (Concert c : concerts){
            model.addAttribute("Concert" + num + "Name", c.getConcertName());
            StringBuilder sb = new StringBuilder(c.getPhoto().getPath());
            sb.delete(0, 25);
            String correctPath = sb.toString();
            model.addAttribute("Concert" + num + "Image", correctPath);
            model.addAttribute("Concert" + num + "Date", c.getDateRange());
            model.addAttribute("Concert" + num + "Time", c.getStartTime().toString());
            num++;
        }
        
        return "index";
    }

    @GetMapping("concerts/{concertId}/sectorLayout/selectSeat/{sectorName}")
    public String seatplan(@PathVariable("concertId") Long concertId, @PathVariable("sectorName") String sectorName, Model model){
        model.addAttribute("concertId", concertId);
        model.addAttribute("sectorName", sectorName);
        return "concertStorage/seatLayout.html";
    }

    // @GetMapping("/sectortest")
    // public String sectortest(){
    //     return "sectortest";
    // }

    @GetMapping("concerts/{concertId}/sectorLayout")
    public String getSectorLayout(@PathVariable("concertId") Long concertId, Model model){
        List<Sector> sectors = concertService.getConcertById(1L).getConcertVenue().getSectors();
        for (Sector s : sectors){

            // find how many seats are avail
            List<String> sectorInfo = getSectorInfo(s.getSeats());            

            model.addAttribute("sector" + s.getSectorName() + "avail", sectorInfo.get(0));
            model.addAttribute("sector" + s.getSectorName() + "total", sectorInfo.get(1));
            model.addAttribute("sector" + s.getSectorName() + "status", sectorInfo.get(2));

// checker for what the model contains
// System.out.println("model has " + model);
        }
        return "concertStorage/" + concertId + "/sectorLayout.html";
    }

    @PostMapping("/bookingSuccess")
    public ResponseEntity<String> handleSeatSelection(@RequestBody Map<String, Object> requestBody) {
        this.concertId = Long.valueOf((Integer) requestBody.get("concertId"));
        this.sectorName = (String) requestBody.get("sectorName");
        this.selectedSeats = (List<String>) requestBody.get("selectedSeats");

// System.out.println("concertid: " + concertId + " sectName:" + sectorName);
        String responseMessage = "Received the following seats: " + selectedSeats.toString();
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/bookingSuccessDetails")
    public String showBookingDetails(Model model) {

        // Now you can use these attributes in your view or processing logic
        model.addAttribute("concertId", concertId);
        model.addAttribute("sectorName", sectorName);
        model.addAttribute("selectedSeats", selectedSeats);
        return "yay.html"; // Return the view where you want to display the data
    }

    @GetMapping("/contact")
    public String contact(Model model){
        return "contact";
    }

    @GetMapping("/portfolio")
    public String portfolio(Model model){
        return "portfolio";
    }

    @GetMapping("/services")
    public String services(Model model){
        return "services";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }


    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @RequestMapping("/shoppingcart.html")
    public String viewShoppingCart(){
        return "shoppingcart";
    }


/**
 * 
 * @param seatAvailability contains all the rows in a sector and each string's character represents a seat.
 * @return Information about the sector in list form: {<number of avail seats>, <total seats in this sector>, sectorStatus}
 */
public static List<String> getSectorInfo(List<String> seatAvailability){
    double totalSeats = 0;
    double availSeats = 0;

    for (String row : seatAvailability){
        totalSeats += row.length();
        char[] seats = row.toCharArray();
        for (char seat : seats) {
            if (seat == 'A') availSeats++;
        }
    }

    List<String> result = new ArrayList<>(List.of(Double.toString(availSeats), Double.toString(totalSeats)));

    double seatAvailPercent =  availSeats / totalSeats;

    /* status reference:
		H (High)    = 66% to 100% seats available (green)
		M (Medium)  = 33% to 65% seats available (blue)
		L (Low)     = (non-zero)% to 32% seats available (orange)
		U (Unavail) = 0% seats available (red)
	*/
    if (seatAvailPercent > 0.66) result.add("H");
    else if (seatAvailPercent > 0.33) result.add("M");
    else if (seatAvailPercent > 0) result.add("L");
    else result.add("U");

    return result;
}

}

// hard code: retrieval of concert attributes
        // Concert c1 = concerts.get(0);
        // model.addAttribute("Concert_1_Name", c1.getConcertName());
        // StringBuilder sb1 = new StringBuilder(c1.getPhoto().getPath());
        // sb1.delete(0, 25);
        // String correctPath1 = sb1.toString();
        // model.addAttribute("Concert1Image", correctPath1);

        // Concert c2 = concerts.get(1);
        // model.addAttribute("Concert_2_Name", c2.getConcertName());
        // StringBuilder sb2 = new StringBuilder(c2.getPhoto().getPath());
        // sb2.delete(0, 25);
        // String correctPath2 = sb2.toString();
        // model.addAttribute("Concert2Image", correctPath2);

        // Concert c3 = concerts.get(2);
        // model.addAttribute("Concert_3_Name", c3.getConcertName());
        // StringBuilder sb3 = new StringBuilder(c3.getPhoto().getPath());
        // sb3.delete(0, 25);
        // String correctPath3 = sb3.toString();
        // model.addAttribute("Concert3Image", correctPath3);


        // int num = 1;

        // for (Concert c : concerts){
        //     model.addAttribute("Concert" + num + "Name", c.getConcertName());
        //     String createPoster = "src/main/resources/static/concert_posters/concert" + num + "poster.jpg";
        //     FileUtils.writeByteArrayToFile(new File(createPoster), c.getPhoto());
        //     String accessPoster = "concert_posters/concert" + num + "poster.jpg";
        //     model.addAttribute("Concert" + num + "Image", accessPoster);
        //     model.addAttribute("Concert" + num + "Date", c.getDate());
        //     num++;
        // }
