package com.self.eventtracking.eventservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.self.eventtracking.eventservice.dao.command.EventCommandRepository;
import com.self.eventtracking.eventservice.dao.query.EventQueryRepository;
import com.self.eventtracking.eventservice.exception.EventNotFoundException;
import com.self.eventtracking.eventservice.helper.Commands;
import com.self.eventtracking.eventservice.helper.EventKeyValue;
import com.self.eventtracking.eventservice.kafka.producer.EventProducer;
import com.self.eventtracking.eventservice.model.EventCommand;
import com.self.eventtracking.eventservice.model.EventQuery;

@RestController
public class EventController {

	Logger logger = LoggerFactory.getLogger(EventController.class);
	
	private EventCommandRepository eventCommandRepository;
	private EventQueryRepository eventQueryRepository;
	
	private EventProducer eventProducer;
	
	public EventController(EventCommandRepository eventCommandRepository, EventQueryRepository eventQueryRepository) {
		this.eventCommandRepository=eventCommandRepository;
		this.eventQueryRepository=eventQueryRepository;
	}
	
	@Autowired
	public void setEventProducer(EventProducer eventProducer) {
		this.eventProducer=eventProducer;
	}
	
	@GetMapping("/events")
	public List<EventQuery> findAllEvents() {
		logger.info("fetching all events");
		return eventQueryRepository.findAll();
	}
	
	@GetMapping("/events/{eventId}")
	public EntityModel<EventQuery> findEventById(@PathVariable String eventId) {
		logger.info("fetching event by id : {}", eventId);
		Optional<EventQuery> event = eventQueryRepository.findById(eventId);
		if(event.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + eventId);
		}
		logger.info("event returned : {}", event.get());
		
		EntityModel<EventQuery> entityModel = EntityModel.of(event.get());
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findAllEvents());
		entityModel.add(link.withRel("events"));
		return entityModel;
	}
	
	@GetMapping("/events/regions/{region}")
	public List<EventQuery> findEventByRegion(@PathVariable String region) {
		logger.info("fetching event by region : {}", region);
		List<EventQuery> events = eventQueryRepository.findByRegion(region);
		logger.info("event returned : {}", events);
		return events;
	}
	
	@PostMapping("/events")
	public ResponseEntity<EventCommand> createEvent(@Valid @RequestBody EventCommand event) {
		logger.info("event to be created : {}", event);
		event.setId(UUID.randomUUID().toString()); // generating a random id for this event
		EventCommand savedEvent = eventCommandRepository.save(event);
		
		EventQuery eventQuery = new EventQuery();
		BeanUtils.copyProperties(savedEvent, eventQuery);
		
		eventProducer.sendMessage(new EventKeyValue(Commands.SAVE, eventQuery)); // sending event to message queue using kafka
		
		// location below is holding the path of the new user created
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // will return the path of the current request
				.path("/{id}") // will append a placeholder to the request
				.buildAndExpand(savedEvent.getId()) // will replace the placeholder with this value
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/events")
	public ResponseEntity<EventCommand> updateEvent(@RequestBody EventCommand event) {
		logger.info("updating event with id : {}", event.getId());
		
		Optional<EventCommand> existingEvent = eventCommandRepository.findById(event.getId());
		if(existingEvent.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + event.getId());
		}
		
		BeanUtils.copyProperties(event, existingEvent.get());
		
		EventCommand updatedEvent = eventCommandRepository.save(existingEvent.get());
		
		EventQuery eventQuery = new EventQuery();
		BeanUtils.copyProperties(existingEvent.get(), eventQuery);
		
		eventProducer.sendMessage(new EventKeyValue(Commands.UPDATE, eventQuery)); // sending event to message queue using kafka
		
		// location below is holding the path of the new user created
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // will return the path of the current request
				.path("/{id}") // will append a placeholder to the request
				.buildAndExpand(updatedEvent.getId()) // will replace the placeholder with this value
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/events/{eventId}")
	public String deleteEvent(@PathVariable String eventId) {
		logger.info("deleting event with id : {}", eventId);
		
		Optional<EventCommand> existingEvent = eventCommandRepository.findById(eventId);
		if(existingEvent.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + eventId);
		}
		
		EventQuery eventQuery = new EventQuery();
		BeanUtils.copyProperties(existingEvent.get(), eventQuery);
		
		eventCommandRepository.deleteById(existingEvent.get().getId());
		
		logger.info("in event controller ::::::::: existingEvent ------> " + existingEvent);
		logger.info("in event controller ::::::::: eventQuery.toString() ------> " + eventQuery);
		
		eventProducer.sendMessage(new EventKeyValue(Commands.DELETE, eventQuery)); // sending event to message queue using kafka
		
		return "event with id " + eventId + " got deleted";
	}
	
}
