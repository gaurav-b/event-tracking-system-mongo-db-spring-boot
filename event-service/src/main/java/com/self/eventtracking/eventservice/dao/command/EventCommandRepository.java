package com.self.eventtracking.eventservice.dao.command;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.self.eventtracking.eventservice.model.EventCommand;

@Repository
public interface EventCommandRepository extends MongoRepository<EventCommand, String>{

}
