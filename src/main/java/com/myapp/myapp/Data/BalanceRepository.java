package com.myapp.myapp.Data;

import com.myapp.myapp.Model.MainBalance;
import com.sun.tools.javac.Main;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<MainBalance,Long> {
    Optional<MainBalance> findByUserId (Long Id);
}
