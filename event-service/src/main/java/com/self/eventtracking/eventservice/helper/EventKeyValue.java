package com.self.eventtracking.eventservice.helper;

import com.self.eventtracking.eventservice.model.EventQuery;

public class EventKeyValue {

	private Commands command;
	private EventQuery eventQuery;
	
	public EventKeyValue() {
		// TODO Auto-generated constructor stub
	}

	public EventKeyValue(Commands key, EventQuery eventQuery) {
		this.command = key;
		this.eventQuery = eventQuery;
	}

	public Commands getKey() {
		return command;
	}

	public void setKey(Commands key) {
		this.command = key;
	}

	public EventQuery getEventQuery() {
		return eventQuery;
	}

	public void setEventQuery(EventQuery eventCommand) {
		this.eventQuery = eventCommand;
	}

	@Override
	public String toString() {
		return "EventKeyValue [key=" + command + ", eventQuery=" + eventQuery + "]";
	}
}
