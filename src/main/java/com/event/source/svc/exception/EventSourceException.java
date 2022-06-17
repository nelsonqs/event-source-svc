package com.event.source.svc.exception;

public class EventSourceException extends RuntimeException {

	private static final long serialVersionUID = 7158644410339194625L;
	
	public EventSourceException(EventSourceErrorCode error) {
		super(error.getMessage());
	}
	
	public EventSourceException(String message) {
		super(message);
	}
	
	public EventSourceException(EventSourceErrorCode error,Throwable cause) {
		super(error.getMessage(), cause);
	}
	
	public EventSourceException(String message,Throwable cause) {
		super(message, cause);
	}

}
