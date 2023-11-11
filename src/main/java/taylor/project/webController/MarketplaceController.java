package taylor.project.webController;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import taylor.project.ticket.TicketService;
import taylor.project.user.UserService;

@Controller
public class MarketplaceController {

    private TicketService ticketService;
    private UserService userService;

    public MarketplaceController(TicketService ts, UserService us){
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


    /**Function that adds a purchased ticket to the marketplace.
     * 
     * @param ticketId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/add-to-marketplace/{ticketId}")
    public String handleMarketplaceAddition(@PathVariable Long ticketId, Model model, Authentication authentication) {
        model = checkAuth(authentication, model);
        try {
            ticketService.addTicketToMarketplace(ticketId);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return "add-to-marketplace-success";
    }


    /**Function that removes a marketplace ticket from the marketplace.
     * 
     * @param ticketId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/rmv-from-marketplace/{ticketId}")
    public String handleMarketplaceRemoval(@PathVariable Long ticketId, Model model, Authentication authentication) {
        model = checkAuth(authentication, model);
        Long userId = (Long)model.getAttribute("userId");

        try {
            ticketService.rmvTicketFromMarketplace(ticketId, userId);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return "rmv-from-marketplace-success";
    }


    /**Page that shows all tickets listed on the marketplace.
     * 
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/marketplace")
    public String marketplace(Authentication authentication, Model model){
        model = checkAuth(authentication, model);
        return "marketplace/marketplace.html";
    }


    /**Page that shows a specific ticket listed on the marketplace.
     * 
     * @param ticketId
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/marketplace/{ticketId}")
    public String marketplaceViewTicket(@PathVariable Long ticketId, Authentication authentication, Model model){
        model = checkAuth(authentication, model);
        model.addAttribute("ticketId", ticketId);
        return "marketplace/marketplace-ticket.html";
    }


    /**Function that handles the purchase of a marketplace ticket.
     * 
     * @param auth
     * @param ticketId
     * @param model
     * @return
     */
    @GetMapping("/marketplace/{ticketId}/purchase-marketplace-ticket")
    public String marketplaceHandlePurchase(Authentication auth, @PathVariable("ticketId") Long ticketId, Model model){
        Long userId;
        if (auth != null) {
            UserDetails userDetails =(UserDetails)auth.getPrincipal();
            userId = userService.getUser(userDetails.getUsername()).getId();
            model.addAttribute("userId", userId);
            try {
                ticketService.buyFromMarketplace(ticketId, userId);
            } catch (RuntimeException e){
                System.out.println(e.getMessage());
                return "redirect:/marketplace/error";
            }
            return "redirect:/marketplace/" + ticketId + "/purchase-success";
        }
        else return "redirect:/login";
    }


    /**Redirect when marketplace ticket has been successfully purchased.
     * 
     * @param ticketId
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/marketplace/{ticketId}/purchase-success")
    public String marketplacePurchaseSuccess(@PathVariable Long ticketId, Model model, Authentication authentication){
        model = checkAuth(authentication, model);
        model.addAttribute("ticketId", ticketId);
        return "/marketplace/marketplace-purch-success.html";
    }


    /**Universal error page when ticket had already been bought by another user/unintended behaviour observed.
     * 
     * @param model
     * @return
     */
    @GetMapping("/marketplace/error")
    public String marketplaceError(Model model){
        return "marketplace/marketplace-error.html";
    }
}
