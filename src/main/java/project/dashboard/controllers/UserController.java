package project.dashboard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dashboard.services.UserService;

import java.security.Principal;
import java.util.Base64;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String GetUserPage(Principal principal, Model model) {
        var userName = principal.getName();
        var user = userService.getUserByName(userName);
        if (user == null)
            return "errorpage404";

        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getNickname());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("about", user.getAdditionalInfo());
//        model.addAttribute("avatar", Base64.getEncoder().encode(user.getAvatar()));

        return "userpage";
    }
}
