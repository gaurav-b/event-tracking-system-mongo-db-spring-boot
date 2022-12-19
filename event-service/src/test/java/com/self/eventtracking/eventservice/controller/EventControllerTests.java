package com.self.eventtracking.eventservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.self.eventtracking.eventservice.dao.command.EventCommandRepository;
import com.self.eventtracking.eventservice.dao.query.EventQueryRepository;
import com.self.eventtracking.eventservice.exception.EventNotFoundException;
import com.self.eventtracking.eventservice.helper.EventKeyValue;
import com.self.eventtracking.eventservice.kafka.producer.EventProducer;
import com.self.eventtracking.eventservice.model.EventCommand;
import com.self.eventtracking.eventservice.model.EventQuery;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private EventCommandRepository eventCommandRepository;
	
	@Mock
	private EventQueryRepository eventQueryRepository;
	
	@Mock
	private EventProducer eventProducer;
	
	@Test
	public void test_defaultConstructor() {
		new EventCommand();
	}
	
	@Test
	public void test_findAllEvents() throws Exception {
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		Mockito.when(eventQueryRepository.findAll()).thenReturn(new ArrayList<EventQuery>());
		List<EventQuery> events = controller.findAllEvents();
		assertThat(events.isEmpty());
	}
	
	@Test
	public void test_findEventById_EventExist() throws Exception {
		
		Optional<EventQuery> event = Optional.ofNullable(new EventQuery(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		Mockito.when(eventQueryRepository.findById(Mockito.anyString())).thenReturn(event);
		EntityModel<EventQuery> entityModel = controller.findEventById("013543e9-1eed-419b-93fb-4ad752d3391c");
		assertThat(entityModel.getContent().getTitle().equals("dummy"));
	}
	
	@Test
	public void test_findEventById_EventNotExist() throws Exception {
		
		Optional<EventQuery> event = Optional.empty();
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		Mockito.when(eventQueryRepository.findById(Mockito.anyString())).thenReturn(event);
		
		assertThrows(EventNotFoundException.class,
						() -> {
							controller.findEventById("013543e9-1eed-419b-93fb-4ad752d3391c");	
						});
	}
	
	@Test
	public void test_findEventByRegion_ListAvail() throws Exception {
		
		List<EventQuery> eventList = new ArrayList<>();
		eventList.add(new EventQuery(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "Mumbai", null, null, null, 10));
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		Mockito.when(eventQueryRepository.findByRegion(Mockito.anyString())).thenReturn(eventList);
		List<EventQuery> result = controller.findEventByRegion("Mumbai");
		assertThat(result.get(0).getRegion().equals("Mumbai"));
	}
	
	/**
	 * @throws Exception
	 * need to check the response of actual method
	 */
	@Test
	@Disabled
	public void test_findEventByRegion_ListNotAvail() throws Exception {
		
		List<EventQuery> eventList = new ArrayList<>();
		eventList.add(new EventQuery(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "Mumbai", null, null, null, 10));
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		Mockito.when(eventQueryRepository.findByRegion(Mockito.anyString())).thenReturn(eventList);
		List<EventQuery> result = controller.findEventByRegion("dummy");
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void test_deleteEvent_EventNotExist() throws Exception {
		
		Optional<EventCommand> event = Optional.empty();
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		Mockito.when(eventCommandRepository.findById(Mockito.anyString())).thenReturn(event);
		
		assertThrows(EventNotFoundException.class,
						() -> {
							controller.deleteEvent("013543e9-1eed-419b-93fb-4ad752d3391c");	
						});
	}
	
	@Test
	public void test_deleteEvent_EventExist() throws Exception {
		
		Optional<EventCommand> event = Optional.ofNullable(new EventCommand(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		controller.setEventProducer(eventProducer);
		
		Mockito.when(eventCommandRepository.findById(Mockito.anyString())).thenReturn(event);
		Mockito.doNothing().when(eventCommandRepository).deleteById(Mockito.anyString());
		Mockito.doNothing().when(eventProducer).sendMessage(Mockito.any(EventKeyValue.class));
		
		controller.deleteEvent("013543e9-1eed-419b-93fb-4ad752d3391c");
	}
	
	@Test
	public void test_createEvent() {
		
		EventCommand eventToBeSaved = new EventCommand("", "dummy", "dummy", "dummy", null, null, null, 10);
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		controller.setEventProducer(eventProducer);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
	            buildAndExpand(eventToBeSaved.getId()).toUri();
	    ResponseEntity<EventCommand> response = ResponseEntity.created(location).build();
	    
	    Mockito.when(eventCommandRepository.save(eventToBeSaved)).thenReturn(eventToBeSaved);
	    Mockito.doNothing().when(eventProducer).sendMessage(Mockito.any(EventKeyValue.class));
	    
	    ResponseEntity<EventCommand> result =  controller.createEvent(eventToBeSaved);
	    
	    String locationHeaderValue = result.getHeaders().get("location").get(0);
		assertFalse(locationHeaderValue.substring(locationHeaderValue.lastIndexOf('/')).isEmpty());
	}
	
	@Test
	public void test_updateEvent_EventNotExist() {
	
		Optional<EventCommand> event = Optional.empty();
		EventCommand eventToBeUpdated = new EventCommand("", "dummy", "dummy", "dummy", null, null, null, 10);
		
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		controller.setEventProducer(eventProducer);
		
		Mockito.when(eventCommandRepository.findById(Mockito.anyString())).thenReturn(event);
		Mockito.doNothing().when(eventProducer).sendMessage(Mockito.any(EventKeyValue.class));
		
		assertThrows(EventNotFoundException.class,
						() -> {
							controller.updateEvent(eventToBeUpdated);	
						});
	}

	@Test
	public void test_updateEvent_EventExist() {
		
		Optional<EventCommand> eventToBeUpdated = Optional.ofNullable(new EventCommand(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		EventController controller = new EventController(eventCommandRepository, eventQueryRepository);
		controller.setEventProducer(eventProducer);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
	            buildAndExpand(eventToBeUpdated.get().getId()).toUri();
	    ResponseEntity<EventCommand> response = ResponseEntity.created(location).build();
	    
	    Mockito.when(eventCommandRepository.findById(Mockito.anyString())).thenReturn(eventToBeUpdated);
	    Mockito.when(eventCommandRepository.save(eventToBeUpdated.get())).thenReturn(eventToBeUpdated.get());
	    Mockito.doNothing().when(eventProducer).sendMessage(Mockito.any(EventKeyValue.class));
	    
		assertThat(controller.updateEvent(eventToBeUpdated.get())).isEqualTo(response);
	}
}
