package com.self.eventtracking.eventservice.dao.query;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.self.eventtracking.eventservice.model.EventCommand;
import com.self.eventtracking.eventservice.model.EventQuery;

@Repository
public interface EventQueryRepository extends MongoRepository<EventQuery, String>{
	public List<EventQuery> findByRegion(String region);
}
