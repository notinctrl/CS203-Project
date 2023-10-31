package taylor.project.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}