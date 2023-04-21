package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.User;
import com.myapp.myapp.Service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class AuthenticationController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @RequestMapping("/index")
    public String landingPage() {
        return "index";
    }

    @RequestMapping("/access-denied")
    public String accesDenied() {
        return "access-denied";
    }

    @GetMapping("/signup")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, Model model, BindingResult bindingResult, Authentication authentication) {
        String returnModel="redirect:/dashboard";

        if (bindingResult.hasErrors()) {
            model.addAttribute("successMessage", "User registered successfully!");
            model.addAttribute("bindingResult", bindingResult);
            return returnModel;
        }
        Map<String, Object> userPresentObj = userService.isUserPresent(user);
        System.out.println(userPresentObj);
        if ((Boolean) userPresentObj.get("isexists")) {
            model.addAttribute("successMessage", userPresentObj.get("message"));
            return returnModel;
        }

        userService.createUser(user);
        System.out.println("User registered successfully!");
        model.addAttribute("successMessage", "User registered successfully!");

        return returnModel;
    }
}
