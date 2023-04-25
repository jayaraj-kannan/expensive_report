package com.myapp.myapp;

import com.myapp.myapp.Model.Expensive;
import com.myapp.myapp.Service.ExpensiveService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
class MyappApplicationTests {
	@Autowired
	private final ExpensiveService expensiveService;

	MyappApplicationTests(ExpensiveService expensiveService) {
		this.expensiveService = expensiveService;
	}


	@Test
	void contextLoads() {
	}


}
