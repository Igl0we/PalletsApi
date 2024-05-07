package lt.javau9.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "A global error occurred. Please try again.");
        return "error-page";
    }

    public String getErrorPath() {
        return PATH;
    }
}