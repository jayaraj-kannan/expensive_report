package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.User;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/add")
    public String createPerson(@ModelAttribute("user") User user, Model model){
        userService.createUser(user);
        model=defaultMethod(model);
        return "persons";
    }
    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") String id, Model model){
        userService.deleteUser(id);
        model=defaultMethod(model);
        return "redirect:/user/getall";
    }
    public Model defaultMethod(Model model){
        User user=new User();
        model.addAttribute("user",user);
        model.addAttribute("persons",userService.getAllUsers());
        return model;
    }
}
