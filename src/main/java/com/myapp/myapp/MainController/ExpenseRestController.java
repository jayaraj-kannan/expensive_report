package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.Expensive;
import com.myapp.myapp.Model.User;
import com.myapp.myapp.Service.ExpensiveService;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseRestController {

    @Autowired
    private final ExpensiveService expensiveService;
    @Autowired
    private final UserService userService;

    public ExpenseRestController(ExpensiveService expensiveService, UserService userService) {
        this.expensiveService = expensiveService;
        this.userService = userService;
    }

    @GetMapping("/getall/{id}")
    public ResponseEntity<List<Expensive>> getExpenses(@PathVariable Long id) {
        return new ResponseEntity<List<Expensive>>(expensiveService.findByUser(id),(HttpStatus.OK));
    }

    @PostMapping("/delete")
    public List deleteExpenses(@RequestBody ArrayList<Expensive> expenseData) {
        System.out.println("inside ");
        System.out.println(expenseData);
        return expenseData.stream()
                .map(e -> expensiveService.deleteById(e.getId(),e))
                .collect(Collectors.toList());
    }
    @PostMapping("/add")
    public ResponseEntity<Void> createExpense(@RequestBody Expensive expense, Authentication authentication){
        User user=userService.getCurrentUsername(authentication);
        expense.setUser(user);
        expense.setDate(new SimpleDateFormat("ddmmyyyy hh:mm s").format(new Date()));
        expensiveService.createExpense(expense);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
