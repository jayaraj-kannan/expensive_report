package com.myapp.myapp.Data;

import com.myapp.myapp.Model.Expensive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpensiveRepository extends JpaRepository<Expensive,String> {

    List<Expensive> findByUserId(Long id);
    void deleteById(Long id);
    List<Expensive> findByIdAndDate(Long id, LocalDateTime date);
    @Query("SELECT e FROM Expensive e WHERE e.id = :id AND YEAR(e.date) = :year")
    List<Expensive> findByIdAndDateYear(Long id, int year);

    List<Expensive> findByIdAndDateBetween(Long id, LocalDateTime start,LocalDateTime end);
}
