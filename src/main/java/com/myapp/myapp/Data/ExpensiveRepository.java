package com.myapp.myapp.Data;

import com.myapp.myapp.Model.Expensive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpensiveRepository extends JpaRepository<Expensive,String> {

    List<Expensive> findByUserId(Long id);
    void deleteById(Long id);
    List<Expensive> findByUserIdAndDateBetween(Long id,LocalDate start, LocalDate end);
    @Query("SELECT DISTINCT DATE_PART('year', CAST(e.date AS DATE)) AS years FROM Expensive e WHERE e.date IS NOT NULL")
    List<Integer> findDistinctYears();

    List<Expensive> findByUserIdAndExpensiveSource(Long id,String source);

}
