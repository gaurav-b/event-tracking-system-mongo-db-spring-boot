package com.self.eventtracking.eventservice.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.self.eventtracking.eventservice.helper.EventKeyValue;

@Service
public class EventProducer {

	private final Logger logger = LoggerFactory.getLogger(EventProducer.class);
	private static final String TOPIC = "events";
	
	@Autowired
	private KafkaTemplate<String, EventKeyValue> kafkaTemplate;
	
	public void sendMessage(EventKeyValue keyValue) {
		logger.info("kafkaTemplate producing to consumer :::: " + keyValue);
		kafkaTemplate.send(TOPIC, keyValue);
	}
}
