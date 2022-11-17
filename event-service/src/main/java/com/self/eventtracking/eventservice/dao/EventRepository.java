package com.self.eventtracking.eventservice.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.self.eventtracking.eventservice.model.Event;

@Repository
public interface EventRepository extends MongoRepository<Event, String>{
	public List<Event> findByRegion(String region);
}
