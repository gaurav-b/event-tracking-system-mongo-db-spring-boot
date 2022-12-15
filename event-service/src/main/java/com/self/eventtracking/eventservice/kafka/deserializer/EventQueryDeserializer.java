package com.self.eventtracking.eventservice.kafka.deserializer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.self.eventtracking.eventservice.helper.EventKeyValue;

public class EventQueryDeserializer implements Deserializer<EventKeyValue> {

	private Logger logger = LoggerFactory.getLogger(EventQueryDeserializer.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public EventKeyValue deserialize(String topic, byte[] data) {
		objectMapper.registerModule(new JavaTimeModule());
		try {
			if (data==null) {
				logger.info("Null received at deserializing !!");
				return null;
			}
			
			logger.info("Deserializing... !!");
			return objectMapper.readValue(new String(data, "UTF-8"), EventKeyValue.class);
		} catch (Exception e) {
			throw new SerializationException("Error when deserializing byte[] to EventQuery !!");
		}
	}
}
