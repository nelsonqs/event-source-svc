package com.event.source.svc.api.error;

import java.time.Instant;
import java.util.List;

import lombok.Getter;

@Getter
public class ApiError {
	
	private String time;
	private int code;
	private String type;
	private String message;
	private List<String> errors;
	
	public ApiError(int code, String type, String message,List<String> errors) {
		super();
		this.time = Instant.now().toString();
		this.code = code;
		this.type = type;
		this.message = message;
		this.errors = errors;
	}
}
