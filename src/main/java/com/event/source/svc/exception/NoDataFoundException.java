package com.event.source.svc.exception;

public class NoDataFoundException extends RuntimeException {

	private static final long serialVersionUID = 8838411942504067626L;
	
	public NoDataFoundException(EventSourceErrorCode error) {
		super(error.getMessage());
	}
	
	public NoDataFoundException(String message) {
		super(message);
	}
	
	public NoDataFoundException(EventSourceErrorCode error,Throwable cause) {
		super(error.getMessage(), cause);
	}	
	
	public NoDataFoundException(String message,Throwable cause) {
		super(message, cause);
	}
}
