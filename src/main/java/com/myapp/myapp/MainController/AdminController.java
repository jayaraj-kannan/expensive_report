package com.myapp.myapp.MainController;


import com.myapp.myapp.Model.User;
import com.myapp.myapp.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/adm")
public class AdminController {
    private static final Logger LOGGER = LogManager.getLogger(AdminController.class);
    @Autowired
    UserService userService;

    private static final String TITLE = "Admin Dashboard";
    @RequestMapping("/dashboard")
    public String homePage(Authentication authentication, Model model) {
        User user = userService.getCurrentUsername(authentication);
        model.addAttribute("current", user);
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);
        model.addAttribute("title",TITLE);
        //model.addAttribute("currentuser", user);
        model.addAttribute("user",new User());
        return "dashboard";
    }

    @RequestMapping("/getall")
    public String getAllPersons(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "persons";
    }




}
