package taylor.project.webController;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import taylor.project.user.*;

@Controller
public class webController {

    @GetMapping("/login")
    public String showLoginPage() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
        //     return  "login";
        // }

        // return "redirect:/";
        return "login";
    }

    @GetMapping("/home")
    public String showHomePage() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
        //     return  "login";
        // }

        // return "redirect:/";
        return "home";
    }

    
//     @GetMapping("/index")
//     public String showIndexPage() {
//         // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
//         //     return  "login";
//         // }

        // return "redirect:/";
    //     return "index";
    // }

    @GetMapping("/concert1")
    public String showConcert1Page() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
        //     return  "login";
        // }

        // return "redirect:/";
        return "concert1";
    }

    @GetMapping("/concert2")
    public String showConcert2Page() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
        //     return  "login";
        // }

        // return "redirect:/";
        return "concert2";
    }

    @GetMapping("/concert3")
    public String showConcert3Page() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
        //     return  "login";
        // }

        // return "redirect:/";
        return "concert3";
    }

    // @GetMapping("/testlogin")
    // public String dashboard(Model model) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     // if (authentication.getPrincipal() instanceof User) System.out.println("is a user");
    //     // else if (authentication.getPrincipal() instanceof String) System.out.println("is a string");
    //     // else System.out.println(authentication.getPrincipal().getClass().getName());
    //     System.out.println(authentication.getPrincipal().toString());
    //     if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
    //         User user = (User)authentication.getPrincipal();
    //         String username = user.getUsername(); // Get the username
    //         Long id = user.getId();
    //         System.out.println(username);
    //         Long userId = 2L;
    //         model.addAttribute("loggedInUsername", username); // Set it as an attribute
    //         return "redirect:/user/" + id + "/purchasedtickets";
    //     } else {
    //         return "redirect:/login";
    //     }
    // }

    @PostMapping("/login")
    public String processLogin() {
        // Spring Security will automatically handle the authentication
        // If the authentication is successful, it will redirect to the defaultSuccessUrl
        // If the authentication fails, it will redirect to the login page with an error parameter
        return "redirect:/index"; // Assuming /dashboard is your defaultSuccessUrl
    }
}