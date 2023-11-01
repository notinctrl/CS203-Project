package taylor.project.webController;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertService;
import taylor.project.sector.*;
import taylor.project.venue.*;

@Controller
public class HTMLController {

    private ConcertService concertService;
    private SectorService sectorService;
    private Long concertId;
    private String sectorName;
    private List<String> selectedSeats;

    public HTMLController(ConcertService cs, SectorService ss){
        this.concertService = cs;
        sectorService = ss;
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

    // @PostMapping("/selectSectorButton")
    // public String selectSectorButton(@RequestParam String token, HttpSession session) {
    //     // Get the generated one-time token for this session
    //     String sessionToken = (String) session.getAttribute("selectSectorButtonToken");

    //     // Verify that the provided token matches the one from the session
    //     if (sessionToken != null && sessionToken.equals(token)) {
    //         // Token is valid, set the session attribute
    //         session.setAttribute("selectSectorButtonClicked", true);
    //         // Redirect to the select sector page
    //         return "redirect:/concerts/1/sectorLayout";
    //     } else {
    //         // Invalid token, handle accordingly
    //         // For added security, you can invalidate the token here
    //         // and/or log suspicious activity
    //         return "redirect:/error";
    //     }
    // }

    // @GetMapping("/generateToken")
    // public String generateToken(HttpSession session) {
    //     String oneTimeToken = UUID.randomUUID().toString(); // Generate a random token
    //     session.setAttribute("selectSectorButtonToken", oneTimeToken);
    //     return "redirect:/index"; // Redirect back to your index page
    // }

    // @GetMapping("/checkToken")
    // public String checkToken(HttpSession session) {
    //     String sessionToken = (String) session.getAttribute("selectSectorButtonToken");
    //     System.out.println("Generated Token: " + sessionToken);
    //     return "redirect:/index"; // Redirect to the index page or any other page
    // }

    // @RequestMapping(value = "/submit-form", method = RequestMethod.POST)
    // public String submitForm(CsrfToken csrfToken) {
    //     // Check the CSRF token
    //     if (csrfToken != null) {
    //         // Process the form data here
    //         // Redirect to a success page or return a response as needed
    //         return "success";
    //     } else {
    //         // Handle the case where the token is missing or invalid
    //         return "error";
    //     }
    // }

    @GetMapping("concerts/{concertId}/sectorLayout")
    public String getSectorLayout(@PathVariable("concertId") Long concertId, Model model, HttpSession session){
        // if (session.getAttribute("selectSectorButtonClicked") == null || !(boolean) session.getAttribute("selectSectorButtonClicked")) {
        //     // If the session attribute is not set or is false, redirect the user back to the previous page
        //     return "redirect:/../../../login"; // Replace with the URL of the previous page
        // }
        // session.setAttribute("selectSectorButtonClicked", null);
        List<Sector> sectors = concertService.getConcertById(1L).getConcertVenue().getSectors();
        Map<String, Object> attributeMap = new HashMap<>();
        for (Sector s : sectors){

            // find how many seats are avail
            List<String> sectorInfo = iniSectorSeats(s.getSeats());            

            model = addSectorAttributes(model, s, sectorInfo);
            attributeMap = addSectorAttributeMap(attributeMap, s, sectorInfo);

// checker for what the model contains
// System.out.println("model has " + model);
        }
        model.addAttribute("attributeMap", attributeMap);
        // session.setAttribute("seatSelectAllowed", true);
        return "concertStorage/" + concertId + "/sectorLayout.html";
    }

    @PostMapping("/setSelectSectorButtonClicked")
    public ResponseEntity<String> setSelectSectorButtonClicked(HttpSession session) {
        session.setAttribute("selectSectorButtonClicked", true);
        return ResponseEntity.ok("Attribute set to true");
    }

    @GetMapping("error")
    public String errorPage(){
        return "error.html";
    }

    @GetMapping("concerts/{concertId}/sectorLayout/selectSeat/{sectorName}")
    public String seatplan(@PathVariable("concertId") Long concertId, @PathVariable("sectorName") String sectorName, Model model, HttpSession session){
        model.addAttribute("concertId", concertId);
        model.addAttribute("sectorName", sectorName);
        return "concertStorage/seatLayout.html";
    }

    @PostMapping("/bookingSuccess")
    public ResponseEntity<String> handleSeatSelection(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {
        // CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // if (csrfToken != null && csrfToken.getToken().equals(requestBody.get("_csrf"))) {
            this.concertId = Long.valueOf((Integer) requestBody.get("concertId"));
            this.sectorName = (String) requestBody.get("sectorName");
            this.selectedSeats = (List<String>) requestBody.get("selectedSeats");

    // System.out.println("concertid: " + concertId + " sectName:" + sectorName);
            String responseMessage = "Received the following seats: " + selectedSeats.toString();
            System.out.println("now changing concert's selected seats to pending...");
            Venue venue = concertService.getConcertById(concertId).getConcertVenue();
            
            sectorService.updateSelectedSectorSeatsToStatus(venue, selectedSeats, sectorName, 'P');

        // System.out.println("all done!");
            return ResponseEntity.ok(responseMessage);
        // } else {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CSRF Token Validation Failed");
        // }
    }

    @GetMapping("/bookingSuccessDetails")
    public String showBookingDetails(Model model, HttpSession session) {
        session.setAttribute("seatSelectAllowed", false);
        // Now you can use these attributes in your view or processing logic
        model.addAttribute("concertId", concertId);
        model.addAttribute("sectorName", sectorName);
        model.addAttribute("selectedSeats", selectedSeats);
        return "ticket-carted-success.html"; // Return the view where you want to display the data
    }

    @GetMapping("/contact")
    public String contact(Model model){
        return "contact";
    }

    @GetMapping("user/{userId}/purchasedtickets")
    public String portfolio(@PathVariable("userId") Long userId, Model model){
        model.addAttribute("userId", userId);
        return "purchased-tickets";
    }

    @GetMapping("/services")
    public String services(Model model){
        return "services";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
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
    public static List<String> iniSectorSeats(List<String> seatAvailability){
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

    public static Model addSectorAttributes(Model m, Sector s, List<String> sectorInfo){
        m.addAttribute("sector" + s.getSectorName() + "avail", sectorInfo.get(0));
        m.addAttribute("sector" + s.getSectorName() + "total", sectorInfo.get(1));
        m.addAttribute("sector" + s.getSectorName() + "status", sectorInfo.get(2));
        return m;
    }

    public static Map<String, Object> addSectorAttributeMap(Map<String, Object> m, Sector s, List<String> sectorInfo){
        m.put("sector" + s.getSectorName() + "avail", sectorInfo.get(0));
        m.put("sector" + s.getSectorName() + "total", sectorInfo.get(1));
        m.put("sector" + s.getSectorName() + "status", sectorInfo.get(2));
        return m;
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
