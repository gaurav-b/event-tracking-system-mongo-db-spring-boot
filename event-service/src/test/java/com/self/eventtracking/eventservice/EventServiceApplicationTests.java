package com.self.eventtracking.eventservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	// Test class added ONLY to cover main() invocation not covered by application tests.
   @Test
   public void main() {
	   EventServiceApplication.main(new String[] {});
   }
}
