package com.self.eventtracking.eventservice.helper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.self.eventtracking.eventservice.model.EventQuery;

@SpringBootTest
public class EventKeyValueTest {
	
	@Test
	public void test() {
		EventKeyValue keyValue = new EventKeyValue();
		keyValue.setKey(Commands.SAVE);
		keyValue.setEventQuery(new EventQuery());
	}
}
