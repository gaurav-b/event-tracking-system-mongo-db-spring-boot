package com.self.eventtracking.eventservice.kafka.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.self.eventtracking.eventservice.controller.EventQueryService;
import com.self.eventtracking.eventservice.helper.EventKeyValue;

@Service
public class EventConsumer {

	@Autowired
	private EventQueryService eventQueryService;
	
	private final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	
	@KafkaListener(topics="events", groupId="group_id")
	public void consume(EventKeyValue keyValue) throws IOException {
		logger.info("consuming message ::::::::: " + keyValue);
		
		switch (keyValue.getKey()) {
		case SAVE: {
			eventQueryService.saveCommand(keyValue.getEventQuery());
			break;
		}
		case UPDATE: {
			eventQueryService.updateCommand(keyValue.getEventQuery());
			break;
		}
		case DELETE: {
			eventQueryService.deleteCommand(keyValue.getEventQuery().getId());
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + keyValue.getKey());
		}
		
		
	}
}
