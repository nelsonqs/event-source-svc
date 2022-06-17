package com.event.source.svc.exception;

public class EventNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 9220059906808040206L;
	
	public EventNotFoundException(EventSourceErrorCode error) {
		super(error.getMessage());
	}
	
	public EventNotFoundException(String message) {
		super(message);
	}
	
	public EventNotFoundException(EventSourceErrorCode error,Throwable cause) {
		super(error.getMessage(), cause);
	}
	
	public EventNotFoundException(String message,Throwable cause) {
		super(message, cause);
	}

}
