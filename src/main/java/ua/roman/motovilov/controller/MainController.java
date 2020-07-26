package ua.roman.motovilov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.roman.motovilov.agile.Image;

@Controller
public class MainController {

    private Image image;
    private String currentToken;

    @Autowired
    public void setImage(Image image) {
        this.image = image;
    }

    @GetMapping
    public String getToken(Model model) {
        currentToken = image.getToken();
        model.addAttribute("token", currentToken);
        return "main";
    }

    @GetMapping("/images")
    public String getPage(@RequestParam String page, Model model) {
        model.addAttribute("token", currentToken);
        model.addAttribute("page", image.getPhotoPage(currentToken, page));
        return "main";
    }

    @GetMapping("/images/{id}")
    public String getDetails(@PathVariable String id, Model model) {
        model.addAttribute("token", currentToken);
        model.addAttribute("details", image.getDetails(currentToken, id));
        return "main";
    }

    @PostMapping("/images")
    public String getImages(Model model) {
        model.addAttribute("token", currentToken);
        model.addAttribute("image", image.getPhotos(currentToken));
        return "main";
    }
}
