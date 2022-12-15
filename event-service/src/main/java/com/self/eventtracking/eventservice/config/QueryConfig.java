package com.self.eventtracking.eventservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.self.eventtracking.eventservice.dao.query.EventQueryRepository;

@Configuration
@EnableMongoRepositories(basePackageClasses = EventQueryRepository.class, mongoTemplateRef = "queryMongoTemplate")
public class QueryConfig {

}
