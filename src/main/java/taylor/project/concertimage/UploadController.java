package taylor.project.concertimage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertService;

@Controller
public class UploadController {

    private ConcertService concertService;

    public UploadController(ConcertService cs){
        this.concertService = cs;
    }

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F://temp//";

    @GetMapping("/index")
    public String index(Model model) {
        List<Concert> concerts = concertService.listConcerts(); 
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

        int num = 1;
        for (Concert c : concerts){
            model.addAttribute("Concert" + num + "Name", c.getConcertName());
            StringBuilder sb = new StringBuilder(c.getPhoto().getPath());
            sb.delete(0, 25);
            String correctPath = sb.toString();
            model.addAttribute("Concert" + num + "Image", correctPath);
            model.addAttribute("Concert" + num + "Date", c.getDate());
            num++;
        }
        return "index";
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

}