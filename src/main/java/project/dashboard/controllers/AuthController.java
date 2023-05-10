package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.dashboard.models.User;
import project.dashboard.services.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController( UserService userService)
    { this.userService = userService; }

    @GetMapping("/registration")
    public String registration()
    {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model)
    {
        try
        {
            userService.addUser(user);
            return "redirect:/login";
        }
        catch (Exception ex)
        {
            model.addAttribute("message", "User exists");
            ex.printStackTrace();
            return "registration";
        }
    }


}
