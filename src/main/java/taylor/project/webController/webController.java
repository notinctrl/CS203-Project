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


    // @GetMapping("/marketplace")
    // public String showmarketplacePage() {
    //     // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     // if (authentication==null||authentication instanceof AnonymousAuthenticationToken) {
    //     //     return  "login";
    //     // }

    //     // return "redirect:/";
    //     return "marketplace";
    // }
}