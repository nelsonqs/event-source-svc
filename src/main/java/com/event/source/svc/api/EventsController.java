package com.event.source.svc.api;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.source.svc.model.Event;
import com.event.source.svc.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/events")
public class EventsController {
	
	private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

	@Autowired
	private EventService<Event, String> eventService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping
	public ResponseEntity<?> pushEvent(@Valid @RequestBody  Event event) throws JsonProcessingException {
		logger.info("Push event request :"+objectMapper.writeValueAsString(event));
		Event eventResponse =  eventService.save(event);
		return ResponseEntity.ok(eventResponse);
	}
	
	@GetMapping("/correlationid/{correlationId}")
	public ResponseEntity<?> retrieveEventsByCorrelationId(@PathVariable(name = "correlationId" ,required = true) String correlationId) {
		logger.info("Get event request for correlationId :"+correlationId);
		List<Event> eventResponse =  eventService.findByCorrelationId(correlationId);
		return ResponseEntity.ok(eventResponse);
	}
	
	@GetMapping("/correlationid/{correlationId}/eventname/{eventName}")
	public ResponseEntity<?> retrieveEventsByCorrelationIdAndEventName(@PathVariable(name = "correlationId" ,required = true) String correlationId,
			@PathVariable(name = "eventName",required = true) String eventName) {
		logger.info("Get event request for correlationId :"+correlationId+" and eventname :"+eventName);
		List<Event> eventResponse =  eventService.findByCorrelationIdAndEventName(correlationId,eventName);
		return ResponseEntity.ok(eventResponse);
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveAllEvents() {
		List<Event> eventResponse =  eventService.findAll();
		return ResponseEntity.ok(eventResponse);
	}
	
}
