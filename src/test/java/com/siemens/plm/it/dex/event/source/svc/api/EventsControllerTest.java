package com.siemens.plm.it.dex.event.source.svc.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.event.source.svc.api.EventsController;
import com.event.source.svc.exception.EventSourceErrorCode;
import com.event.source.svc.exception.EventSourceException;
import com.event.source.svc.exception.NoDataFoundException;
import com.event.source.svc.model.Event;
import com.event.source.svc.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.plm.it.dex.event.source.svc.model.TestEvent;

@WebMvcTest(EventsController.class)
class EventsControllerTest {
	
	@MockBean private EventService<Event, String> eventService;
	@Autowired private ObjectMapper mapper;
	@Autowired private MockMvc mockMvc;	
	private static final String EVENT_TYPE = "EVENT";
	private static final String STATUS = "IN_PROGRESS";
	private static final String USER = "JAG";

	@Test
	void testPushEventIdealCase() throws JsonProcessingException, Exception {
		when(eventService.save(ArgumentMatchers.any())).thenReturn(getValidEvent());
		this.mockMvc.perform(post("/events")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.content(mapper.writeValueAsString(getValidEvent()))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is2xxSuccessful());		
	}
	
	@Test
	void testPushEventBadRequestCase() throws JsonProcessingException, Exception {
		when(eventService.save(ArgumentMatchers.any())).thenReturn(getValidEvent());
		this.mockMvc.perform(post("/events")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.content(mapper.writeValueAsString(getInValidEvent()))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString(EventSourceErrorCode.ValidationFailed.getMessage())));	
	}

	@Test
	void testPushEventInternalServerErrorCase() throws JsonProcessingException, Exception {
		when(eventService.save(ArgumentMatchers.any())).thenThrow(new EventSourceException(EventSourceErrorCode.DefaultException));
		this.mockMvc.perform(post("/events")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.content(mapper.writeValueAsString(getValidEvent()))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(EventSourceErrorCode.DefaultException.getMessage())));	
	}

	@Test
	void testRetrieveEventsByCodAgendaIdealCase() throws Exception {
		when(eventService.findByCodAgenda(ArgumentMatchers.anyString())).thenReturn(getEventList());
		this.mockMvc.perform(get("/events/codAgenda/{codAgenda}",UUID.randomUUID().toString())
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is2xxSuccessful());	
	}
	
	@Test
	void testRetrieveEventsByCodAgendaAndEventNameIdealCase() throws Exception {
		when(eventService.findByCodAgenda(ArgumentMatchers.anyString())).thenReturn(getEventList());
		this.mockMvc.perform(get("/events/codAgenda/{codAgenda}/eventname/{eventName}",UUID.randomUUID().toString(),"sap_validation")
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is2xxSuccessful());	
	}
	
	@Test
	void testRetrieveEventsByCodAgendaNotDataFoundCase() throws Exception {
		when(eventService.findByCodAgenda(ArgumentMatchers.anyString())).thenThrow(new NoDataFoundException(EventSourceErrorCode.NoDataFound));
		this.mockMvc.perform(get("/events/codAgenda/{codAgenda}",UUID.randomUUID().toString())
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString(EventSourceErrorCode.NoDataFound.getMessage())));	
	}
	
	@Test
	void testEventsByCodAgendaInternelServerErrorCase() throws JsonProcessingException, Exception {
		when(eventService.findByCodAgenda(ArgumentMatchers.anyString())).thenThrow(new EventSourceException(EventSourceErrorCode.DefaultException));
		this.mockMvc.perform(get("/events/codAgenda/{codAgenda}",UUID.randomUUID().toString())
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(EventSourceErrorCode.DefaultException.getMessage())));	
	}

	@Test
	void testRetrieveAllEventsIdealCase() throws Exception {
		when(eventService.findAll()).thenReturn(getEventList());
		this.mockMvc.perform(get("/events")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is2xxSuccessful())
				.andExpect(content().string(containsString(EVENT_TYPE)));
	}
	
	@Test
	void testRetrieveAllEventsNoDataFoundCase() throws Exception {
		when(eventService.findAll()).thenThrow(new NoDataFoundException(EventSourceErrorCode.NoDataFound));
		this.mockMvc.perform(get("/events")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().string(containsString(EventSourceErrorCode.NoDataFound.getMessage())));
	}
	
	@Test
	void testRetrieveAllEventsDexExceptionCase() throws Exception {
		when(eventService.findAll()).thenThrow(new EventSourceException(EventSourceErrorCode.DefaultException));
		this.mockMvc.perform(get("/events")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is5xxServerError())
				.andExpect(content().string(containsString(EventSourceErrorCode.DefaultException.getMessage())));
	}
	
	private Event getEmptyEvent() {
		return new Event();
	}
	
	private Event getValidEvent() {
		Event event = getEmptyEvent();
		event.setCreatedTime(Instant.now().toEpochMilli());
		event.setCodAgenda(UUID.randomUUID().toString());
		event.setTypeEvent(EVENT_TYPE);
		event.setId(UUID.randomUUID().toString());
		event.setStatus(STATUS);
		event.setAgendaBasica(new TestEvent());
		event.setAgendaDetalle(new TestEvent());
		event.setCreateDate(new Date());
		event.setUpdateDate(new Date());
		event.setCodUser(USER);
		return event;
	}
	
	private Event getInValidEvent() {
		Event event = getEmptyEvent();
		event.setCreatedTime(Instant.now().toEpochMilli());
		event.setCodAgenda(UUID.randomUUID().toString());
		event.setId(UUID.randomUUID().toString());
		event.setAgendaBasica(new TestEvent());
		event.setAgendaDetalle(new TestEvent());
		event.setCreateDate(new Date());
		event.setUpdateDate(new Date());
		event.setCodUser(USER);
		return event;
	}
	
	private List<Event> getEventList(){
		List<Event> events = new ArrayList<>(1);
		events.add(getValidEvent());
		return events;
	}
}
