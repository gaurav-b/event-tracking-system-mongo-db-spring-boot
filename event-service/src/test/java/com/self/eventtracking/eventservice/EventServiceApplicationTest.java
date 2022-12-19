package com.self.eventtracking.eventservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventServiceApplicationTest {

	@Test
	void contextLoads() {
	}

	// Test class added ONLY to cover main() invocation not covered by application tests.
   @Test
   @Disabled
   public void main() {
	   EventServiceApplication.main(new String[] {});
   }
}
