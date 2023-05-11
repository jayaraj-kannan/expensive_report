package com.myapp.myapp.MainController;

import com.myapp.myapp.Model.MainBalance;
import com.myapp.myapp.Model.User;
import com.myapp.myapp.Service.MainBalanceService;
import com.myapp.myapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/blc")
public class BalanceController {
    @Autowired
    private final MainBalanceService mainBalanceService;
    @Autowired
    private final UserService userService;

    public BalanceController(MainBalanceService mainBalanceService, UserService userService) {
        this.mainBalanceService = mainBalanceService;
        this.userService = userService;
    }

    @GetMapping("/getbalance/{id}")
    public ResponseEntity<MainBalance> getCurrentBalance(@PathVariable Long id, Authentication authentication){
        User user=userService.getCurrentUsername(authentication);
        return new ResponseEntity<MainBalance>(mainBalanceService.getCurrentBalance(id,user), HttpStatus.OK);
    }
}
