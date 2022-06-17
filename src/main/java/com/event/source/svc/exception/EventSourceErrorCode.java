package com.event.source.svc.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Optional;

public enum EventSourceErrorCode {
	
	//Invalid input error codes
	ValidationFailed(1001,FailureType.InvalidInput,"Validation failed."),
	EmptyEventName(1002,FailureType.InvalidInput,"Event name cannot be null or empty."),
	EmptyEventStatus(1003,FailureType.InvalidInput,"Event status cannot be null or empty."),
	EmptyEventCorrelationId(1004,FailureType.InvalidInput,"Event correlationId cannot be null or empty."),
	EmptyEventPayload(1005,FailureType.InvalidInput,"Event payload cannot be null or empty."),	
	
	//Event related error codes
	EventNotFound(2001,FailureType.ResourceNotFound,"Event with id %s not found."),
	NoDataFound(2002,FailureType.ResourceNotFound,"No Data found."),
	
	DefaultException(6000, FailureType.Other, "Error while processing the request");
	
	
	//failure type
	public enum FailureType {
			InvalidInput, Security, Other, BeanInitialization, ResourceNotFound, Metadata,
			InvalidConfiguration, GenericError
	}
		
	private static Map<Integer, EventSourceErrorCode> lookup = new HashMap<>();

		/** set the lookup map */
		static {
			Stream.of(EventSourceErrorCode.values())
					.forEach(iotErrorCode -> lookup.put(iotErrorCode.code, iotErrorCode));
		}

		private final int code;
		private final FailureType failureType;
		private final String message;
		
		EventSourceErrorCode(int code, FailureType failureType, String message) {
			this.code = code;
			this.failureType = failureType;
			this.message = message;
		}

		public String getMessage() {
			return "[" + code + "] " + message;
		}

		public int getCode() {
			return code;
		}

		public static EventSourceErrorCode getCode(Integer code) {
			return Optional.ofNullable(lookup.get(code)).orElse(DefaultException);
		}

		public FailureType getFailureType() {
			return failureType;
		}

}
