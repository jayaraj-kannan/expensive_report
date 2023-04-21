package com.myapp.myapp.Data;

import com.myapp.myapp.Model.Expensive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensiveRepository extends JpaRepository<Expensive,String> {

    List<Expensive> findByUserId(Long id);
    String deleteById(Long id);
}
