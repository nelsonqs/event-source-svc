package com.event.source.svc.dao;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.event.source.svc.db.entity.EventEntity;
import com.event.source.svc.db.repository.EventsRepository;
import com.event.source.svc.exception.EventSourceErrorCode;
import com.event.source.svc.exception.EventNotFoundException;
import com.event.source.svc.exception.NoDataFoundException;
import com.event.source.svc.model.Event;
import com.event.source.svc.service.EventService;

@Service
public class EventsDao implements EventService<Event, String> {
	
	@Autowired
	private EventsRepository repository;

	@Override
	public Event save(Event event) {		
		EventEntity entity = EventEntity.builder().name(event.getName())
				.status(event.getStatus()).createdTime(Instant.now().toEpochMilli())
				.correlationId(event.getCorrelationId()).payload(event.getPayload()).build();
		EventEntity savedEntity = repository.save(entity);		
		return Event.builder().id(savedEntity.getId()).name(savedEntity.getName())
				.createdTime(savedEntity.getCreatedTime()).status(savedEntity.getStatus())
				.correlationId(savedEntity.getCorrelationId()).payload(savedEntity.getPayload()).build();
	}

	@Override
	public Event find(String id) {
		EventEntity entity = repository.findById(id).orElseThrow(()->new EventNotFoundException(String.format(EventSourceErrorCode.EventNotFound.getMessage(), id)));
		return Event.builder().id(entity.getId()).name(entity.getName())
				.createdTime(entity.getCreatedTime()).status(entity.getStatus())
				.correlationId(entity.getCorrelationId()).payload(entity.getPayload()).build();
	}

	@Override
	public List<Event> findAll() {
		List<EventEntity> events = repository.findAll();
		if(events.isEmpty()) {
			throw new NoDataFoundException(EventSourceErrorCode.NoDataFound);
		}		
		return events.stream()
					  .map(evt->Event.builder().id(evt.getId()).name(evt.getName())
					  .createdTime(evt.getCreatedTime()).status(evt.getStatus()).correlationId(evt.getCorrelationId()).payload(evt.getPayload()).build())
					  .collect(Collectors.toList());
	}

	@Override
	public List<Event> findByCorrelationId(String correlationId) {
		List<EventEntity> events = repository.findByCorrelationId(correlationId);
		if(events.isEmpty()) {
			throw new NoDataFoundException(EventSourceErrorCode.NoDataFound);
		}
		return events.stream()
				  .map(evt->Event.builder().id(evt.getId()).name(evt.getName())
				  .createdTime(evt.getCreatedTime()).status(evt.getStatus()).correlationId(evt.getCorrelationId()).payload(evt.getPayload()).build())
				  .collect(Collectors.toList());
	}

	@Override
	public List<Event> findByCorrelationIdAndEventName(String correlationId, String eventName) {
		List<EventEntity> events = repository.findByCorrelationIdAndName(correlationId,eventName);
		if(events.isEmpty()) {
			throw new NoDataFoundException(EventSourceErrorCode.NoDataFound);
		}
		return events.stream()
				  .map(evt->Event.builder().id(evt.getId()).name(evt.getName())
				  .createdTime(evt.getCreatedTime()).status(evt.getStatus()).correlationId(evt.getCorrelationId()).payload(evt.getPayload()).build())
				  .collect(Collectors.toList());
	}
}
