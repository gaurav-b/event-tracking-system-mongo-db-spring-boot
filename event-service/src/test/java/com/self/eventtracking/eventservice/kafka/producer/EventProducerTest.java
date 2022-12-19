package com.self.eventtracking.eventservice.kafka.producer;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import com.self.eventtracking.eventservice.helper.Commands;
import com.self.eventtracking.eventservice.helper.EventKeyValue;
import com.self.eventtracking.eventservice.model.EventQuery;

@SpringBootTest
public class EventProducerTest {

	@Mock
	private KafkaTemplate<String, EventKeyValue> kafkaTemplate;
	
	@Test
	public void test_consume_save() throws IOException {
		EventQuery event = new EventQuery("013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10);
		EventKeyValue keyValue = new EventKeyValue(Commands.SAVE, event);
		EventProducer eventProducer = new EventProducer(kafkaTemplate);
		
		Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.any(EventKeyValue.class))).thenReturn(null);
		
		eventProducer.sendMessage(keyValue);
	}
}
