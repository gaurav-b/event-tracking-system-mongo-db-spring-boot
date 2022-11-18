package com.self.eventtracking.eventservice.exception;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ErrorDetailsTests {

	@Test
	public void test_defaultConstructor() {
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),"dummy","dummy");
		errorDetails.getTimestamp();
		errorDetails.getMessage();
		errorDetails.getDetails();
	}
}
