package com.self.eventtracking.eventservice.kafka.serializer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.self.eventtracking.eventservice.helper.EventKeyValue;

public class EventQuerySerializer implements Serializer<EventKeyValue> {

	private Logger logger = LoggerFactory.getLogger(EventQuerySerializer.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public byte[] serialize(String topic, EventKeyValue keyValue) {
		objectMapper.registerModule(new JavaTimeModule());
		try {
			if (keyValue==null) {
				logger.info("Null received at serializing !!");
				return null;
			}
			
			logger.info("Serializing... !!");
			return objectMapper.writeValueAsBytes(keyValue);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new SerializationException("Error when serializing EventKeyValue to byte[] !!");
		}
	}
}
