package com.myapp.myapp.Data;

import com.myapp.myapp.Model.MainBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface BalanceRepository extends JpaRepository<MainBalance,Long> {

}
