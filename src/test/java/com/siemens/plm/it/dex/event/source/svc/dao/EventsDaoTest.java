package com.siemens.plm.it.dex.event.source.svc.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.event.source.svc.dao.EventsDao;
import com.event.source.svc.db.entity.EventEntity;
import com.event.source.svc.db.repository.EventsRepository;
import com.event.source.svc.exception.EventSourceErrorCode;
import com.event.source.svc.exception.EventNotFoundException;
import com.event.source.svc.exception.NoDataFoundException;
import com.event.source.svc.model.Event;
import com.siemens.plm.it.dex.event.source.svc.model.TestEvent;

@ExtendWith(MockitoExtension.class)
class EventsDaoTest {
	
	@Mock private EventsRepository eventRepository;
	private static final String EVENT_TYPE = "EVENT";
	private static final String STATUS = "IN_PROGRESS";

	@InjectMocks private EventsDao underTest;

	@Test
	void testSaveIdealCase() {
		when(eventRepository.save(ArgumentMatchers.any())).thenReturn(getValidEventEntity());
		Event actual = underTest.save(getValidEvent());
		assertNotNull(actual);
		assertEquals(EVENT_TYPE, actual.getName());
	}

	@Test
	void testFindIdealCase() {
		EventEntity entity = getValidEventEntity();
		when(eventRepository.findById(ArgumentMatchers.matches(entity.getId()))).thenReturn(Optional.of(entity));
		Event actual = underTest.find(entity.getId());
		assertNotNull(actual);
		assertEquals(EVENT_TYPE, actual.getName());
	}
	
	@Test
	void testFindEventNotFoundCase() {
		when(eventRepository.findById(ArgumentMatchers.any())).thenThrow(new EventNotFoundException(EventSourceErrorCode.EventNotFound));
		assertThrows(EventNotFoundException.class, ()->{
			underTest.find(UUID.randomUUID().toString());
		});
	}

	@Test
	void testFindAllIdealCase() {
		List<EventEntity> events = new ArrayList<>(1);
		events.add(getValidEventEntity());
		when(eventRepository.findAll()).thenReturn(events);
		List<Event> actual = underTest.findAll();
		assertNotNull(actual);
		assertFalse(actual.isEmpty());
		assertEquals(1, actual.size());
	}
	
	@Test
	void testFindAllNoDataFoundCase() {
		when(eventRepository.findAll()).thenReturn(new ArrayList<>(0));
		assertThrows(NoDataFoundException.class, ()->{
			underTest.findAll();
		});
	}
	
	@Test
	void testFindByCorrelationIdIdealCase() {
		List<EventEntity> events = new ArrayList<>(1);
		events.add(getValidEventEntity());
		when(eventRepository.findByCorrelationId(ArgumentMatchers.anyString())).thenReturn(events);
		List<Event> actual = underTest.findByCorrelationId(UUID.randomUUID().toString());
		assertNotNull(actual);
		assertFalse(actual.isEmpty());
		assertEquals(1, actual.size());
	}
	
	@Test
	void testFindByCorrelationIdNoDataFoundCase() {
		when(eventRepository.findByCorrelationId(ArgumentMatchers.anyString())).thenReturn(new ArrayList<>(0));
		assertThrows(NoDataFoundException.class, ()->{
			underTest.findByCorrelationId(UUID.randomUUID().toString());
		});
	}
	
	@Test
	void testFindByCorrelationIdAndEventNameIdealCase() {
		List<EventEntity> events = new ArrayList<>(1);
		events.add(getValidEventEntity());
		when(eventRepository.findByCorrelationIdAndName(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(events);
		List<Event> actual = underTest.findByCorrelationIdAndEventName(UUID.randomUUID().toString(),EVENT_TYPE);
		assertNotNull(actual);
		assertFalse(actual.isEmpty());
		assertEquals(1, actual.size());
	}
	
	@Test
	void testFindByCorrelationIdAndEventNameNoDataFoundCase() {
		when(eventRepository.findByCorrelationIdAndName(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(new ArrayList<>(0));
		assertThrows(NoDataFoundException.class, ()->{
			underTest.findByCorrelationIdAndEventName(UUID.randomUUID().toString(),EVENT_TYPE);
		});
	}
	
	private Event getEmptyEvent() {
		return new Event();
	}
	
	private Event getValidEvent() {
		Event event = getEmptyEvent();
		event.setCreatedTime(Instant.now().toEpochMilli());
		event.setCorrelationId(UUID.randomUUID().toString());
		event.setName(EVENT_TYPE);
		event.setId(UUID.randomUUID().toString());
		event.setStatus(STATUS);
		event.setPayload(new TestEvent());
		return event;
	}
	
	private EventEntity getValidEventEntity() {
		return EventEntity.builder()
				.id(UUID.randomUUID().toString())
				.correlationId(UUID.randomUUID().toString())
				.createdTime(Instant.now().toEpochMilli())
				.name(EVENT_TYPE)
				.status(STATUS)
				.payload(new TestEvent())
				.build();
	}
}
