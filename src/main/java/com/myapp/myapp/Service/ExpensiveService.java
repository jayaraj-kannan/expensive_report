package com.myapp.myapp.Service;

import com.myapp.myapp.Data.ExpensiveRepository;
import com.myapp.myapp.Model.Expensive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpensiveService {
    @Autowired
    private final ExpensiveRepository expensiveRepository;

    public ExpensiveService(ExpensiveRepository expensiveRepository) {
        this.expensiveRepository = expensiveRepository;
    }
    public List<Expensive> getAllExpenses(){
       return expensiveRepository.findAll();
    }
    public String createExpense(Expensive expensive){
        try {
            expensiveRepository.save(expensive);
            return "Success";
        }catch (Exception e){
            return e.getMessage();
        }

    }

    public List<Expensive> findByUser(Long id){
        return expensiveRepository.findByUserId(id);
    }

    @Transactional
    public String deleteById(Long id,Expensive expense){
        try {
            expensiveRepository.deleteById(id);
            return "success";
            // delete operation was successful
        } catch (EmptyResultDataAccessException e) {
            return e.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }

    }
    public List<Expensive> findByYear(Long id,int year){
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return expensiveRepository.findByUserIdAndDateBetween(id,start, end);
    }
    public List<Integer> findDistinctYears(){
        return expensiveRepository.findDistinctYears();
    }
    public List<Expensive> findBySource(Long id,String source){
        return expensiveRepository.findByUserIdAndExpensiveSource(id,source);
    }
}
