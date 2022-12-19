package com.self.eventtracking.eventservice.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.self.eventtracking.eventservice.dao.query.EventQueryRepository;
import com.self.eventtracking.eventservice.exception.EventNotFoundException;
import com.self.eventtracking.eventservice.model.EventQuery;

@Service
public class EventQueryService {

	Logger logger = LoggerFactory.getLogger(EventQueryService.class);
	
	private EventQueryRepository eventQueryRepository;
	
	public EventQueryService(EventQueryRepository eventQueryRepository) {
		this.eventQueryRepository=eventQueryRepository;
	}
	
	public void saveCommand(EventQuery eventQuery) {
		logger.info("saving EventCommand object values as EventQuery object in the event_qry collection : {}", eventQuery);
		
		EventQuery reflectedEvent = eventQueryRepository.save(eventQuery);
		logger.info("saved EventQuery object : {}", reflectedEvent.toString());
	}
	
	public void updateCommand(EventQuery eventQuery) {
		logger.info("updating EventCommand object values as EventQuery object in the event_qry collection : {}", eventQuery.getId());
		
		Optional<EventQuery> existingEvent = eventQueryRepository.findById(eventQuery.getId());
		if(existingEvent.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + eventQuery.getId());
		}
		
		BeanUtils.copyProperties(eventQuery, existingEvent.get());
		
		EventQuery reflectedEvent = eventQueryRepository.save(existingEvent.get());
		logger.info("updated EventQuery object : {}", reflectedEvent.toString());
	}
	
	public void deleteCommand(@PathVariable String eventId) {
		logger.info("deleting event with id : {}", eventId);
		
		Optional<EventQuery> existingEvent = eventQueryRepository.findById(eventId);
		if(existingEvent.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + eventId);
		}
		eventQueryRepository.deleteById(existingEvent.get().getId());
	}
	
}
