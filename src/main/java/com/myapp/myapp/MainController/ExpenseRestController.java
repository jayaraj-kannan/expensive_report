package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.Expensive;
import com.myapp.myapp.Model.ExpensiveCategory;
import com.myapp.myapp.Model.ExpensiveSource;
import com.myapp.myapp.Model.User;
import com.myapp.myapp.Service.ExpensiveService;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        expense.setDate(LocalDate.now());
        expensiveService.createExpense(expense);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @GetMapping("/getcate_total/{id}")
    public ResponseEntity<Map> getExpenseCategoryTotal(@PathVariable Long id){
        List<Expensive> expenses =expensiveService.findByUser(id);
        Map<ExpensiveCategory, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expensive::getExpensiveCategory, Collectors.summingDouble(e -> Double.parseDouble(e.getAmount()))));
        return new ResponseEntity<Map>(categoryTotals,HttpStatus.OK);
    }
    @GetMapping("/getsource_total/{id}")
    public ResponseEntity<Map> getExpenseSourceTotal(@PathVariable Long id){
        List<Expensive> expenses =expensiveService.findByUser(id);
        Map<ExpensiveSource, Double> sourceTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expensive::getExpensiveSource, Collectors.summingDouble(e -> Double.parseDouble(e.getAmount()))));
        return new ResponseEntity<Map>(sourceTotals,HttpStatus.OK);
    }
    @GetMapping("/ranges/{id}/{range}")
    public ResponseEntity<Map> getExpensesAsRange(@PathVariable Long id){
        List<Expensive> expenses =expensiveService.findByUser(id);
        Map<ExpensiveSource, Double> sourceTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expensive::getExpensiveSource, Collectors.summingDouble(e -> Double.parseDouble(e.getAmount()))));
        return new ResponseEntity<Map>(sourceTotals,HttpStatus.OK);

    }
    @GetMapping("/total/{id}")
    public ResponseEntity<Double> getExpensesTotal(@PathVariable Long id){
        int year= LocalDate.now().getYear();
        List<Expensive> expenses =expensiveService.findByYear(id,year);
         return new ResponseEntity<Double>
                 (expenses.stream().mapToDouble(e-> Double.parseDouble(e.getAmount())).sum()
                         ,HttpStatus.OK);

    }
}
