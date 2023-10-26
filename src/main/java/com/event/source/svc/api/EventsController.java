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
		logger.info("Colocar event request :"+objectMapper.writeValueAsString(event));
		Event eventResponse =  eventService.save(event);
		return ResponseEntity.ok(eventResponse);
	}
	
	@GetMapping("/codAgenda/{codAgenda}")
	public ResponseEntity<?> retrieveEventsByCodAgenda(@PathVariable(name = "codAgenda" ,required = true) String codAgenda) {
		logger.info("Obtener event request for codAgenda :"+codAgenda);
		List<Event> eventResponse =  eventService.findByCodAgenda(codAgenda);
		return ResponseEntity.ok(eventResponse);
	}
	
	@GetMapping("/codAgenda/{codAgenda}/eventname/{eventName}")
	public ResponseEntity<?> retrieveEventsByCodAgendaAndEventName(@PathVariable(name = "codAgenda" ,required = true) String codAgenda,
																   @PathVariable(name = "eventName",required = true) String eventName) {
		logger.info("Obtener request for codAgenda :"+codAgenda+" and eventname :"+eventName);
		List<Event> eventResponse =  eventService.findByCodAgendaAndEventName(codAgenda,eventName);
		return ResponseEntity.ok(eventResponse);
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveAllEvents() {
		List<Event> eventResponse =  eventService.findAll();
		return ResponseEntity.ok(eventResponse);
	}
	
}
