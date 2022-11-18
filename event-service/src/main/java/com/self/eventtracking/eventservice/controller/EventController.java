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

import com.self.eventtracking.eventservice.dao.EventRepository;
import com.self.eventtracking.eventservice.model.Event;

@RestController
public class EventController {

	Logger logger = LoggerFactory.getLogger(EventController.class);
	
	private EventRepository eventRepository;
	
	public EventController(EventRepository eventRepository) {
		this.eventRepository=eventRepository;
	}
	
	@PostMapping("/events")
	public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
		logger.info("event to be created : {}", event);
		event.setId(UUID.randomUUID().toString()); // generating a random id for this event
//		Event savedEvent = eventRepository.save(event);
		Event savedEvent = eventRepository.insert(event);
		
		// location below is holding the path of the new user created
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // will return the path of the current request
				.path("/{id}") // will append a placeholder to the request
				.buildAndExpand(savedEvent.getId()) // will replace the placeholder with this value
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/events")
	public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
		logger.info("updating event with id : {}", event.getId());
		
		Optional<Event> existingEvent = eventRepository.findById(event.getId());
		if(existingEvent.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + event.getId());
		}
		
		BeanUtils.copyProperties(event, existingEvent.get());
		
		Event updatedEvent = eventRepository.save(existingEvent.get());
		
		// location below is holding the path of the new user created
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // will return the path of the current request
				.path("/{id}") // will append a placeholder to the request
				.buildAndExpand(updatedEvent.getId()) // will replace the placeholder with this value
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/events")
	public List<Event> findAllEvents() {
		logger.info("fetching all events");
		return eventRepository.findAll();
	}
	
	@GetMapping("/events/{eventId}")
	public EntityModel<Event> findEventById(@PathVariable String eventId) {
		logger.info("fetching event by id : {}", eventId);
		Optional<Event> event = eventRepository.findById(eventId);
		if(event.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + eventId);
		}
		logger.info("event returned : {}", event.get());
		
		EntityModel<Event> entityModel = EntityModel.of(event.get());
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).findAllEvents());
		entityModel.add(link.withRel("events"));
		return entityModel;
	}
	
	@GetMapping("/events/regions/{region}")
	public List<Event> findEventByRegion(@PathVariable String region) {
		logger.info("fetching event by region : {}", region);
		List<Event> events = eventRepository.findByRegion(region);
		logger.info("event returned : {}", events);
		return events;
	}
	
	@DeleteMapping("/events/{eventId}")
	public String deleteEvent(@PathVariable String eventId) {
		logger.info("deleting event with id : {}", eventId);
		
		Optional<Event> existingEvent = eventRepository.findById(eventId);
		if(existingEvent.isEmpty()) {
			throw new EventNotFoundException("event not found with event id : " + eventId);
		}
		eventRepository.deleteById(existingEvent.get().getId());
		
		return "event with id " + eventId + " got deleted";
	}
	
}
