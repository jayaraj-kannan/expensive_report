package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.*;
import com.myapp.myapp.Service.ExpensiveService;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exp")
public class ExpensiveController {
    @Autowired
    private final ExpensiveService expensiveService;
    @Autowired
    private final UserService userService;
    private static final String TITLE = "User Dashboard";
    public ExpensiveController(ExpensiveService expensiveService, UserService userService) {
        this.expensiveService = expensiveService;
        this.userService = userService;
    }
    @PostMapping("/addexpense")
    public String addExpesive(@ModelAttribute("expense") Expensive expense, Model model,Authentication authentication) {
        User user=userService.getCurrentUsername(authentication);
        System.out.println("userid"+user.getId());
        expense.setUser(user);
        expense.setDate(LocalDate.now());
        expensiveService.createExpense(expense);
        return "redirect:/exp/dashboard";
    }
    @GetMapping("/dashboard")
    public String expensesDashboard(Model model, Authentication authentication){
        User user= userService.getCurrentUsername(authentication);
        model.addAttribute("current", user);
        model.addAttribute("title",TITLE);
        model.addAttribute("expense", new Expensive());
        model.addAttribute("expenses",expensiveService.findByUser(user.getId()));
        model.addAttribute("e_category", Arrays.asList(ExpensiveCategory.values())
                .stream().map(i -> i.toString()).collect(Collectors.toList()));
        model.addAttribute("e_source", Arrays.asList(ExpensiveSource.values())
                .stream().map(i -> i.toString()).collect(Collectors.toList()));
        model.addAttribute("e_type", Arrays.asList(ExpensiveType.values())
                .stream().map(i -> i.toString()).collect(Collectors.toList()));
        return "expenses";
    }
    @GetMapping("/getall")
    public String getAllExpenses(Model model){
        model.addAttribute("expenses",expensiveService.getAllExpenses());
        return "expense";
    }
    @GetMapping("/jqexp")
    public String ajx(Model model){
        return "expenses_jq";
    }

    public Model defaultMethod(Model model){
       Expensive expensive=new Expensive();
        model.addAttribute("expense",expensive);
        return model;
    }
}
