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
		EventEntity entity = EventEntity.builder().typeEvent(event.getTypeEvent())
				.status(event.getStatus())
				.createdTime(Instant.now().toEpochMilli())
				.codAgenda(event.getCodAgenda())
				.agendaBasica(event.getAgendaBasica())
				.agendaDetalle(event.getAgendaDetalle())
				.createDate(event.getCreateDate())
				.updateDate(event.getUpdateDate())
				.codUser(event.getCodUser())
				.build();
		EventEntity savedEntity = repository.save(entity);		
		return Event.builder().id(savedEntity.getId()).typeEvent(savedEntity.getTypeEvent())
				.createdTime(savedEntity.getCreatedTime())
				.status(savedEntity.getStatus())
				.codAgenda(savedEntity.getCodAgenda())
				.agendaBasica(savedEntity.getAgendaBasica())
				.agendaDetalle(savedEntity.getAgendaDetalle())
				.createDate(savedEntity.getCreateDate())
				.updateDate(savedEntity.getUpdateDate())
				.codUser(savedEntity.getCodUser())
				.build();
	}

	@Override
	public Event find(String id) {
		EventEntity entity = repository.findById(id).orElseThrow(()->new EventNotFoundException(String.format(EventSourceErrorCode.EventNotFound.getMessage(), id)));
		return Event.builder().id(entity.getId())
				.typeEvent(entity.getTypeEvent())
				.createdTime(entity.getCreatedTime())
				.status(entity.getStatus())
				.codAgenda(entity.getCodAgenda())
				.agendaBasica(entity.getAgendaBasica())
				.agendaDetalle(entity.getAgendaDetalle())
				.createDate(entity.getCreateDate())
				.updateDate(entity.getUpdateDate())
				.codUser(entity.getCodUser())
				.build();
	}

	@Override
	public List<Event> findAll() {
		List<EventEntity> events = repository.findAll();
		if(events.isEmpty()) {
			throw new NoDataFoundException(EventSourceErrorCode.NoDataFound);
		}		
		return events.stream()
					  .map(evt->Event.builder().id(evt.getId())
							.typeEvent(evt.getTypeEvent())
					  		.createdTime(evt.getCreatedTime())
							.status(evt.getStatus())
							  .codAgenda(evt.getCodAgenda())
							  .agendaBasica(evt.getAgendaBasica())
							  .agendaDetalle(evt.getAgendaDetalle())
							  .createDate(evt.getCreateDate())
							  .updateDate(evt.getUpdateDate())
							  .codUser(evt.getCodUser())
							  .build())
					  .collect(Collectors.toList());
	}

	@Override
	public List<Event> findByCodAgenda(String codAgenda) {
		List<EventEntity> events = repository.findByCodAgenda(codAgenda);
		if(events.isEmpty()) {
			throw new NoDataFoundException(EventSourceErrorCode.NoDataFound);
		}
		return events.stream()
				  .map(evt->Event.builder().id(evt.getId())
						  .typeEvent(evt.getTypeEvent())
				  .createdTime(evt.getCreatedTime())
						  .status(evt.getStatus())
						  .codAgenda(evt.getCodAgenda())
						  .agendaBasica(evt.getAgendaBasica())
						  .agendaDetalle(evt.getAgendaDetalle())
						  .createDate(evt.getCreateDate())
						  .updateDate(evt.getUpdateDate())
						  .codUser(evt.getCodUser())
						  .build())
				  .collect(Collectors.toList());
	}

	@Override
	public List<Event> findByCodAgendaAndEventName(String codAgenda, String eventName) {
		List<EventEntity> events = repository.findByCodAgendaAndTypeEvent(codAgenda,eventName);
		if(events.isEmpty()) {
			throw new NoDataFoundException(EventSourceErrorCode.NoDataFound);
		}
		return events.stream()
				  .map(evt->Event.builder().id(evt.getId()).typeEvent(evt.getTypeEvent())
				  .createdTime(evt.getCreatedTime())
				  .status(evt.getStatus())
						  .codAgenda(evt.getCodAgenda())
						  .agendaBasica(evt.getAgendaBasica())
						  .agendaDetalle(evt.getAgendaDetalle())
						  .createDate(evt.getCreateDate())
						  .updateDate(evt.getUpdateDate())
						  .codUser(evt.getCodUser())
				  .build())
				  .collect(Collectors.toList());
	}
}
