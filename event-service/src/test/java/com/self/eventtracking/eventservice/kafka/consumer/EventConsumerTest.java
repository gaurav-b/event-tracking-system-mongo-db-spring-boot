package com.self.eventtracking.eventservice.kafka.consumer;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.self.eventtracking.eventservice.helper.Commands;
import com.self.eventtracking.eventservice.helper.EventKeyValue;
import com.self.eventtracking.eventservice.model.EventQuery;
import com.self.eventtracking.eventservice.service.EventQueryService;

@SpringBootTest
public class EventConsumerTest {

	@Mock
	private EventQueryService eventQueryService;
	
	@Test
	public void test_consume_save() throws IOException {
		EventQuery event = new EventQuery("013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10);
		EventKeyValue keyValue = new EventKeyValue(Commands.SAVE, event);
		EventConsumer eventConsumer = new EventConsumer(eventQueryService);
		
		Mockito.doNothing().when(eventQueryService).saveCommand(event);
		eventConsumer.consume(keyValue);
	}
	
	@Test
	public void test_consume_update() throws IOException {
		EventQuery event = new EventQuery("013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10);
		EventKeyValue keyValue = new EventKeyValue(Commands.UPDATE, event);
		EventConsumer eventConsumer = new EventConsumer(eventQueryService);
		
		Mockito.doNothing().when(eventQueryService).updateCommand(event);
		eventConsumer.consume(keyValue);
	}
	
	@Test
	public void test_consume_delete() throws IOException {
		EventQuery event = new EventQuery("013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10);
		EventKeyValue keyValue = new EventKeyValue(Commands.DELETE, event);
		EventConsumer eventConsumer = new EventConsumer(eventQueryService);
		
		Mockito.doNothing().when(eventQueryService).deleteCommand(event.getId());
		eventConsumer.consume(keyValue);
	}
	
}
