package com.self.eventtracking.eventservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.self.eventtracking.eventservice.dao.command.EventCommandRepository;

@Configuration
@EnableMongoRepositories(basePackageClasses = EventCommandRepository.class, mongoTemplateRef = "commandMongoTemplate")
public class CommandConfig {

	
}
