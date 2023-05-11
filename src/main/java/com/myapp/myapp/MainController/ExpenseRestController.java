package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.*;
import com.myapp.myapp.Service.ExpensiveService;
import com.myapp.myapp.Service.MainBalanceService;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseRestController {

    @Autowired
    private final ExpensiveService expensiveService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final MainBalanceService mainBalanceService;

    public ExpenseRestController(ExpensiveService expensiveService, UserService userService, MainBalanceService mainBalanceService) {
        this.expensiveService = expensiveService;
        this.userService = userService;
        this.mainBalanceService = mainBalanceService;
    }

    @GetMapping("/getall/{id}")
    public ResponseEntity<List<Expensive>> getExpenses(@PathVariable Long id) {
        return new ResponseEntity<List<Expensive>>(expensiveService.findByUser(id),(HttpStatus.OK));
    }

    @PostMapping("/delete")
    public ResponseEntity<List<String>> deleteExpenses(@RequestBody ArrayList<Expensive> expenseData,Authentication authentication) {
        List<String> lStatus = new ArrayList<>();
         expenseData.stream()
                .forEach(e->{
                  String status=expensiveService.deleteById(e.getId(),e);
                    lStatus.add(status);
                    if (status.equals("success")) {
                        User user=userService.getCurrentUsername(authentication);
                        MainBalance currentBalance= mainBalanceService.getCurrentBalance(user.getId(),user);
                        if(e.getExpensiveType().toString().equals(ExpensiveType.CREDIT.toString())){
                            currentBalance.setTotal(currentBalance.getTotal().subtract(new BigDecimal(e.getAmount())));
                        } else if (e.getExpensiveType().toString().equals(ExpensiveType.DEBIT.toString())) {
                            currentBalance.setTotal(currentBalance.getTotal().add(new BigDecimal(e.getAmount())));
                        }
                        mainBalanceService.updateBalance(currentBalance);
                    }

                });
         return new ResponseEntity<List<String>>(lStatus,HttpStatus.OK);

    }
    @PostMapping("/add")
    public ResponseEntity<Void> createExpense(@RequestBody Expensive expense, Authentication authentication){
        User user=userService.getCurrentUsername(authentication);
        expense.setUser(user);
        expense.setDate(LocalDate.now());
        expensiveService.createExpense(expense);
        MainBalance currentBalance= mainBalanceService.getCurrentBalance(user.getId(),user);
        if(expense.getExpensiveType().toString().equals(ExpensiveType.CREDIT.toString())){
            currentBalance.setTotal(currentBalance.getTotal().add(new BigDecimal(expense.getAmount())));
        }else{
            currentBalance.setTotal(currentBalance.getTotal().subtract(new BigDecimal(expense.getAmount())));
        }
        mainBalanceService.updateBalance(currentBalance);
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
    @GetMapping("/getsource_/{id}")
    public ResponseEntity<Map> getExpensesAsRange(@PathVariable Long id){
        List<Expensive> expenses =expensiveService.findByUser(id);
        Map<ExpensiveSource, Double> sourceTotals = expenses.stream()
                .collect(Collectors.groupingBy(Expensive::getExpensiveSource, Collectors.summingDouble(e -> Double.parseDouble(e.getAmount()))));
        return new ResponseEntity<Map>(sourceTotals,HttpStatus.OK);

    }
    @GetMapping("/getbyyear/{id}/{year}")
    public ResponseEntity<List<Expensive>> getExpensesByYear(@PathVariable Long id,@PathVariable String year){
        if(year.equals("ALL")){
            return new ResponseEntity<List<Expensive>>(expensiveService.findByUser(id)
                    .stream().sorted(Comparator.comparing(Expensive::getDate).reversed()).toList(),HttpStatus.OK);
        }else{
            return new ResponseEntity<List<Expensive>>(expensiveService.findByYear(id,Integer.valueOf(year))
                    .stream().sorted(Comparator.comparing(Expensive::getDate).reversed()).toList(),HttpStatus.OK);
        }

    }
    @GetMapping("/total/{id}/{year}")
    public ResponseEntity<BigDecimal> getExpensesTotal(@PathVariable Long id, @PathVariable String year){
        List<Expensive> expenses;
        if(year.equals("ALL")){
           return new ResponseEntity<BigDecimal>(expensiveService.getTotalByAll(id),HttpStatus.OK);
        }else {
            return new ResponseEntity<BigDecimal>(expensiveService.getTotalByYear(id,Integer.valueOf(year)),HttpStatus.OK);
        }

    }
    @GetMapping("/dist_years/{id}")
    public ResponseEntity<List<Integer>> getDistinctYears(@PathVariable Long id){
        return new ResponseEntity<List<Integer>>(
                expensiveService.findDistinctYears(id),
                HttpStatus.OK);
    }
}
