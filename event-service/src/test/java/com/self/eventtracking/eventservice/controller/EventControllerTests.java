package com.self.eventtracking.eventservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.self.eventtracking.eventservice.dao.EventRepository;
import com.self.eventtracking.eventservice.model.Event;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private EventRepository eventRepository;
	
	@Test
	@Disabled
	public void test_findAllEvents1() throws Exception {
		mockMvc.perform(get("/events"))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void test_defaultConstructor() {
		new Event();
	}
	
	@Test
	public void test_findAllEvents() throws Exception {
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findAll()).thenReturn(new ArrayList<Event>());
		List<Event> events = controller.findAllEvents();
		assertThat(events.isEmpty());
	}
	
	@Test
	public void test_findEventById_EventExist() throws Exception {
		
		Optional<Event> event = Optional.ofNullable(new Event(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(event);
		EntityModel<Event> entityModel = controller.findEventById("013543e9-1eed-419b-93fb-4ad752d3391c");
		assertThat(entityModel.getContent().getTitle().equals("dummy"));
	}
	
	@Test
	public void test_findEventById_EventNotExist() throws Exception {
		
		Optional<Event> event = Optional.empty();
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(event);
		
		assertThrows(EventNotFoundException.class,
						() -> {
							controller.findEventById("013543e9-1eed-419b-93fb-4ad752d3391c");	
						});
	}
	
	@Test
	public void test_findEventByRegion_ListAvail() throws Exception {
		
		List<Event> eventList = new ArrayList<>();
		eventList.add(new Event(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "Mumbai", null, null, null, 10));
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findByRegion(Mockito.anyString())).thenReturn(eventList);
		List<Event> result = controller.findEventByRegion("Mumbai");
		assertThat(result.get(0).getRegion().equals("Mumbai"));
	}
	
	/**
	 * @throws Exception
	 * need to check the response of actual method
	 */
	@Test
	@Disabled
	public void test_findEventByRegion_ListNotAvail() throws Exception {
		
		List<Event> eventList = new ArrayList<>();
		eventList.add(new Event(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "Mumbai", null, null, null, 10));
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findByRegion(Mockito.anyString())).thenReturn(eventList);
		List<Event> result = controller.findEventByRegion("dummy");
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void test_deleteEvent_EventNotExist() throws Exception {
		
		Optional<Event> event = Optional.empty();
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(event);
		
		assertThrows(EventNotFoundException.class,
						() -> {
							controller.deleteEvent("013543e9-1eed-419b-93fb-4ad752d3391c");	
						});
	}
	
	@Test
	public void test_deleteEvent_EventExist() throws Exception {
		
		Optional<Event> event = Optional.ofNullable(new Event(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(event);
		Mockito.doNothing().when(eventRepository).deleteById(Mockito.anyString());
		
		controller.deleteEvent("013543e9-1eed-419b-93fb-4ad752d3391c");
	}
	
	@Test
	public void test_createEvent() {
		
		Event eventToBeSaved = new Event("", "dummy", "dummy", "dummy", null, null, null, 10);
		EventController controller = new EventController(eventRepository);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
	            buildAndExpand(eventToBeSaved.getId()).toUri();
	    ResponseEntity<Event> response = ResponseEntity.created(location).build();
	    
	    Mockito.when(eventRepository.save(eventToBeSaved)).thenReturn(eventToBeSaved);
	    
	    ResponseEntity<Event> result =  controller.createEvent(eventToBeSaved);
	    
	    String locationHeaderValue = result.getHeaders().get("location").get(0);
		assertFalse(locationHeaderValue.substring(locationHeaderValue.lastIndexOf('/')).isEmpty());
	}
	
	@Test
	public void test_updateEvent_EventNotExist() {
	
		Optional<Event> event = Optional.empty();
		Event eventToBeUpdated = new Event("", "dummy", "dummy", "dummy", null, null, null, 10);
		
		EventController controller = new EventController(eventRepository);
		Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(event);
		
		assertThrows(EventNotFoundException.class,
						() -> {
							controller.updateEvent(eventToBeUpdated);	
						});
	}

	@Test
	public void test_updateEvent_EventExist() {
		
		Optional<Event> eventToBeUpdated = Optional.ofNullable(new Event(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		EventController controller = new EventController(eventRepository);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
	            buildAndExpand(eventToBeUpdated.get().getId()).toUri();
	    ResponseEntity<Event> response = ResponseEntity.created(location).build();
	    
	    Mockito.when(eventRepository.findById(Mockito.anyString())).thenReturn(eventToBeUpdated);
	    Mockito.when(eventRepository.save(eventToBeUpdated.get())).thenReturn(eventToBeUpdated.get());
	    
		assertThat(controller.updateEvent(eventToBeUpdated.get())).isEqualTo(response);
	}
}
