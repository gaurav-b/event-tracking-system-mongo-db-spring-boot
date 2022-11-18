package com.self.eventtracking.eventservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Event {
	
	@Id
	private String id;
	
	@Size(min=8, message = "Event title should have atleast 8 characters")
	private String title;
	private String description;
	
	@Size(min=3, message = "Event region should have atleast 3 characters")
	private String region;
	
	@Future(message = "Event date should not be in the past")
	private LocalDate date;
	
	@Future(message = "Event date time should not be in the past")
	private LocalDateTime startTime;
	
	@Future(message = "Event date time should not be in the past")
	private LocalDateTime endTime;

	@Min(value = 10, message = "Event capacity must be at minimum 10")
	private int eventCapacity;
	
	public Event() {
		
	}
	
	public Event(String id, String title, String description, String Region, LocalDate date, LocalDateTime startTime,
			LocalDateTime endTime, int eventCapacity) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.region = Region;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventCapacity = eventCapacity;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String Region) {
		this.region = Region;
	}
	public int getEventCapacity() {
		return eventCapacity;
	}
	public void setEventCapacity(int eventCapacity) {
		this.eventCapacity = eventCapacity;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", description=" + description + ", Region=" + region + ", date="
				+ date + ", startTime=" + startTime + ", endTime=" + endTime + ", eventCapacity=" + eventCapacity + "]";
	}
}
