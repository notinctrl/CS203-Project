package taylor.project.webController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import java.nio.file.*;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertService;
import taylor.project.sector.Sector;
import taylor.project.sector.SectorService;
import taylor.project.ticket.TicketService;
import taylor.project.user.UserService;

@Controller
public class HTMLController {

    private ConcertService concertService;
    private SectorService sectorService;
    private TicketService ticketService;
    private UserService userService;
    private Long concertId;
    private String sectorName;
    private List<String> selectedSeats;

    public HTMLController(ConcertService cs, SectorService ss, TicketService ts, UserService us){
        this.concertService = cs;
        sectorService = ss;
        ticketService = ts;
        userService=us;
    }

    /**Method to obtain the userId of the user that is logged in. Used to redirect non-logged users to login page.
     * 
     * @param auth
     * @param model
     * @return
     */
    public Model checkAuth(Authentication auth, Model model){
        Long userId;
        if (auth != null) {
            UserDetails userDetails =(UserDetails)auth.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            userId = userService.getUser(userDetails.getUsername()).getId();
            model.addAttribute("userId", userId);
        }

        return model;
    }


    /**Homepage. Loads all concert related assets to be used.
     * 
     * @param model
     * @param authentication
     * @return
     * @throws IOException
     */
    @GetMapping("/index")
    public String index(Model model, Authentication authentication) throws IOException{
        model = checkAuth(authentication, model);
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


    /**Page to show all concerts. Relevant concert information is placed into the model.
     * 
     * @param model
     * @param authentication
     * @return
     * @throws IOException
     */
    @GetMapping("/concerts")
    public String allConcerts(Model model, Authentication authentication) throws IOException{
        model = checkAuth(authentication, model);
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
        
        return "concertStorage/concerts.html";
    }


    /**Page to view specific concert.
     * 
     * @param concertId
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("concerts/{concertId}")
    public String viewConcert(@PathVariable Long concertId, Authentication authentication, Model model){
        model = checkAuth(authentication, model);
        System.out.println(model);
        return "concertStorage/" + concertId + "/concert" + concertId + ".html";
    }


    /**Page to view concert's sector layout. Users should not be able to access this without clicking on the
     * "BUY TICKETS NOW" button on the viewConcert page.
     * 
     * @param concertId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("concerts/{concertId}/sectorLayout")
    public String getSectorLayout(@PathVariable("concertId") Long concertId, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        List<Sector> sectors = concertService.getConcertById(concertId).getConcertVenue().getSectors();
        Map<String, Object> attributeMap = new HashMap<>();

        for (Sector s : sectors){
            // find how many seats are avail. 
            List<String> sectorInfo = iniSectorInfo(s);            
            model = addSectorAttributes(model, s, sectorInfo);
            attributeMap = addSectorAttributeMap(attributeMap, s, sectorInfo);
        }
        model.addAttribute("attributeMap", attributeMap);
        if (model.getAttribute("userId") == null){
            return "redirect:/login";
        }
System.out.println("Model contains: " + model);
        return "concertStorage/" + concertId + "/sectorLayout.html";
    }


    /**Page to view specific sector selected's available/unavailable seats.
     * 
     * @param concertId
     * @param sectorName
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("concerts/{concertId}/sectorLayout/selectSeat/{sectorName}")
    public String getSeatPlan(@PathVariable("concertId") Long concertId, @PathVariable("sectorName") String sectorName, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        model.addAttribute("concertId", concertId);
        model.addAttribute("sectorName", sectorName);
        if (model.getAttribute("userId") == null){
            return "redirect:/login";
        }
        return "concertStorage/seatLayout.html";
    }


    /**Function to handle final confirmations of seat selection. Changes seat statuses in sector and ticket statuses.
     * 
     * @param request
     * @param requestBody
     * @param auth
     * @param model
     * @return
     */
    @PostMapping("/bookingSuccess")
    public ResponseEntity<String> handleSeatSelection(HttpServletRequest request, @RequestBody Map<String, Object> requestBody, Authentication auth, Model model) {
        model = checkAuth(auth, model);
        Long userId = (Long) model.getAttribute("userId");
            this.concertId = Long.valueOf((Integer) requestBody.get("concertId"));
            this.sectorName = (String) requestBody.get("sectorName");
            this.selectedSeats = (List<String>) requestBody.get("selectedSeats");

            String responseMessage = "Received the following seats: " + selectedSeats.toString();
            System.out.println("now changing concert's selected seats to pending...");

            sectorService.updateSectorSeatsToPending(concertId, sectorName, selectedSeats);
            ticketService.changeTicketStatusToPending(concertId, sectorName, selectedSeats, userId);

            System.out.println("all done!");
            return ResponseEntity.ok(responseMessage);
    }


    /**Redirect after successful ticket carting. Shows details of tickets carted.
     * 
     * @param model
     * @param session
     * @param authentication
     * @return
     */
    @GetMapping("/bookingSuccessDetails")
    public String showBookingDetails(Model model, HttpSession session, Authentication authentication) {
        model = checkAuth(authentication, model);
        session.setAttribute("seatSelectAllowed", false);
        model.addAttribute("concertId", concertId);
        model.addAttribute("sectorName", sectorName);
        model.addAttribute("selectedSeats", selectedSeats);
        return "ticket-carted-success.html"; // Return the view where you want to display the data
    }

    /**Page to view carted (non-checked out) tickets. Users can checkout their tickets from here.
     * 
     * @param userId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("user/{userId}/shoppingcart")
    public String getShoppingCart(@PathVariable Long userId, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        if (model.getAttribute("userId") == null){
            return "redirect:/login";
        }
        // to deal with forbidden requests
        if (userId != model.getAttribute("userId")){
            return "redirect:/error-403";
        }
        return "shoppingcart";
    }


    /**Function that handles ticket checkout. Changes seat statuses in sector and ticket statuses.
     * 
     * @param userId
     * @param ticketId
     * @param model
     * @return
     */
    @GetMapping("/user/{userId}/shoppingcart/purchase-carted-ticket/{ticketId}")
    public String shoppingCartHandlePurchase(@PathVariable("userId") Long userId, @PathVariable("ticketId") Long ticketId, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        // to deal with forbidden requests
        if (userId != model.getAttribute("userId")){
            return "redirect:/error-403";
        }
        try {
            sectorService.updateSectorSeatsToUnavail(ticketId, userId);
            ticketService.checkoutTicket(ticketId, userId);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "redirect:/errorpage";
        }
        
        return "redirect:/user/" + userId + "/shoppingcart/" + ticketId + "/purchase-success";
    }


    /**Redirect after user successfully checks out ticket. Shows details of purchased ticket.
     * 
     * @param userId
     * @param ticketId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/user/{userId}/shoppingcart/{ticketId}/purchase-success")
    public String shoppingCartPurchaseSuccess(@PathVariable("userId") Long userId, @PathVariable("ticketId") Long ticketId, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        if (userId != model.getAttribute("userId")){
            return "redirect:/error-403";
        }
        model.addAttribute("ticketId", ticketId);
        return "shoppingcart-purch-success.html";
    }


    /**Page to view checked out/purchased tickets. Users can add tickets to marketplace from here.
     * 
     * @param userId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("user/{userId}/purchasedtickets")
    public String puchasedTickets(@PathVariable("userId") Long userId, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        if (userId != model.getAttribute("userId")){
            return "redirect:/error-403";
        }
        return "purchased-tickets";
    }


    /**Universal error page.
     * 
     * @return
     */
    @GetMapping("/errorpage")
    public String errorPage(){
        return "error-page";
    }


    @GetMapping("/error-403")
    public String forbiddenRequest(){
        return "error-403";
    }

/** Methods for data retrieval
 * 
 * @param seatAvailability contains all the rows in a sector and each string's character represents a seat.
 * @return Information about the sector in list form: {<number of avail seats>, <total seats in this sector>, sectorStatus}
 */
    public static List<String> iniSectorInfo(Sector s){

        List<String> result = new ArrayList<>();
        double seatAvailPercent = 0;

        if (s.isGeneralStanding()){
            result = new ArrayList<>(List.of(Double.toString(s.getSeatsLeft()), Double.toString(s.getSectorSize())));
            seatAvailPercent =  s.getSeatsLeft() / s.getSectorSize();
            System.out.println(seatAvailPercent);
        } else {
            double totalSeats = 0;
            double availSeats = 0;

            for (String row : s.getSeats()){
                totalSeats += row.length();
                char[] seats = row.toCharArray();
                for (char seat : seats) {
                    if (seat == 'A') availSeats++;
                }
            }
            result = new ArrayList<>(List.of(Double.toString(availSeats), Double.toString(totalSeats)));
            seatAvailPercent =  availSeats / totalSeats;
        }

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
    
