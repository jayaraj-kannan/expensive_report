package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.MainBalance;
import com.myapp.myapp.Service.MainBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/blc")
public class BalanceController {
    @Autowired
    private final MainBalanceService mainBalanceService;

    public BalanceController(MainBalanceService mainBalanceService) {
        this.mainBalanceService = mainBalanceService;
    }

    @GetMapping("/getbalance/{id}")
    public ResponseEntity<MainBalance> getCurrentBalance(@PathVariable Long id){
        return new ResponseEntity<MainBalance>(mainBalanceService.getCurrentBalance(id), HttpStatus.OK);
    }
}
