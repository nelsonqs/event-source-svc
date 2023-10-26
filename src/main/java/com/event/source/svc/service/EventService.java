package com.event.source.svc.service;

import java.util.List;

public interface EventService<T,EVENTID> {

	public T save(T event);
	
	public T find(EVENTID id);
	
	public List<T> findAll();
	
	public List<T> findByCodAgenda(String correlationId);
	
	public List<T> findByCodAgendaAndEventName(String correlationId, String eventName);
}
