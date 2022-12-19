package com.self.eventtracking.eventservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.self.eventtracking.eventservice.dao.query.EventQueryRepository;
import com.self.eventtracking.eventservice.exception.EventNotFoundException;
import com.self.eventtracking.eventservice.model.EventQuery;

@SpringBootTest
public class EventQueryServiceTest {

	@Mock
	private EventQueryRepository eventQueryRepository;
	
	@Test
	public void test_saveCommand() {
		EventQuery event = new EventQuery("013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10);
		EventQueryService eventQueryService = new EventQueryService(eventQueryRepository);
		Mockito.when(eventQueryRepository.save(event)).thenReturn(event);
	    eventQueryService.saveCommand(event);
	}
	
	@Test
	public void test_updateCommand_EventNotExist() {
		Optional<EventQuery> event = Optional.empty();
		EventQuery eventToBeUpdated = new EventQuery("", "dummy", "dummy", "dummy", null, null, null, 10);
		
		EventQueryService eventQueryService = new EventQueryService(eventQueryRepository);
		
		Mockito.when(eventQueryRepository.findById(Mockito.anyString())).thenReturn(event);
		assertThrows(EventNotFoundException.class,
				() -> {
					eventQueryService.updateCommand(eventToBeUpdated);	
				});
	}

	@Test
	public void test_updateEvent_EventExist() {
		Optional<EventQuery> eventToBeUpdated = Optional.ofNullable(new EventQuery(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		
		EventQueryService eventQueryService = new EventQueryService(eventQueryRepository);
		
		Mockito.when(eventQueryRepository.findById(Mockito.anyString())).thenReturn(eventToBeUpdated);
	    Mockito.when(eventQueryRepository.save(eventToBeUpdated.get())).thenReturn(eventToBeUpdated.get());
	    
	    eventQueryService.updateCommand(eventToBeUpdated.get());
	}
	
	@Test
	public void test_deleteCommand_EventNotExist() {
		Optional<EventQuery> event = Optional.empty();
		
		EventQueryService eventQueryService = new EventQueryService(eventQueryRepository);
		
		Mockito.when(eventQueryRepository.findById(Mockito.anyString())).thenReturn(event);
		assertThrows(EventNotFoundException.class,
				() -> {
					eventQueryService.deleteCommand("013543e9-1eed-419b-93fb-4ad752d3391c");	
				});
	}
	
	@Test
	public void test_deleteCommand_EventExist() {
		Optional<EventQuery> eventToBeUpdated = Optional.ofNullable(new EventQuery(
				"013543e9-1eed-419b-93fb-4ad752d3391c", "dummy", "dummy", "dummy", null, null, null, 10));
		
		EventQueryService eventQueryService = new EventQueryService(eventQueryRepository);
		
		Mockito.when(eventQueryRepository.findById(Mockito.anyString())).thenReturn(eventToBeUpdated);
		Mockito.doNothing().when(eventQueryRepository).deleteById(Mockito.anyString());
	    
		eventQueryService.deleteCommand("013543e9-1eed-419b-93fb-4ad752d3391c");
	}
	
}
