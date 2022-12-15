package com.self.eventtracking.eventservice.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoDbConfig {

	@Primary
    @Bean(name = "commandProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.command")
    public MongoProperties getCommandProps() throws Exception {
        return new MongoProperties();
    }
	
    @Bean(name = "queryProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.query")
    public MongoProperties getQueryProps() throws Exception {
        return new MongoProperties();
    }
	
	@Primary
	@Bean(name="commandMongoTemplate")
	@ConfigurationProperties("spring.data.mongodb.command")
	public MongoTemplate commandMongoTemplate() throws Exception {
		return new MongoTemplate(commandMongoDbFactory(getCommandProps()));
	}
	
	@Bean(name="queryMongoTemplate")
	@ConfigurationProperties("spring.data.mongodb.query")
	public MongoTemplate queryMongoTemplate() throws Exception {
		return new MongoTemplate(queryMongoDbFactory(getQueryProps()));
	}
	
	@Primary
	@Bean
	public MongoDatabaseFactory commandMongoDbFactory(MongoProperties commandProps) throws Exception {
		return new SimpleMongoClientDatabaseFactory(getCommandProps().getUri());
	}
	
	@Bean
	public MongoDatabaseFactory queryMongoDbFactory(MongoProperties queryProps) throws Exception {
		return new SimpleMongoClientDatabaseFactory(getQueryProps().getUri());
	}
}
