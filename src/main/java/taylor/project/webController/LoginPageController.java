package taylor.project.webController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

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

//         // return "redirect:/";
//         return "index";
//     }
}
