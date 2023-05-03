package com.myapp.myapp.Service;

import com.myapp.myapp.Data.BalanceRepository;
import com.myapp.myapp.Model.MainBalance;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.engine.spi.Managed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MainBalanceService {
    @Autowired
    private final BalanceRepository balanceRepository;

    public MainBalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }
    public MainBalance getCurrentBalance(Long userId){
        try {
           return balanceRepository.getOne(userId);
        }catch (EntityNotFoundException e){
            return new MainBalance();
        }

    }
    public BigDecimal updateBalance(MainBalance balance){
        return balanceRepository.save(balance).getTotal();
    }
}
