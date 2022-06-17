package com.event.source.svc.api.error;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.event.source.svc.exception.EventSourceErrorCode;
import com.event.source.svc.exception.EventSourceException;
import com.event.source.svc.exception.EventNotFoundException;
import com.event.source.svc.exception.NoDataFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler{
	
	public final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex,WebRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(er -> er.getDefaultMessage())
				.collect(Collectors.toList());
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), EventSourceErrorCode.ValidationFailed.getMessage(), errors);
		return error;
	}
	
	@ExceptionHandler(EventNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleEventNotFoundException(EventNotFoundException exp,WebRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), exp.getMessage(), null);
		return error;
	}
	
	@ExceptionHandler(NoDataFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleNoDataFoundException(NoDataFoundException exp,WebRequest request){
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), exp.getMessage(), null);
		return error;
	}
	
	@ExceptionHandler(EventSourceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiError handleDexEventSourceException(EventSourceException exp,WebRequest request){
		logger.error(exp.getMessage(),exp);
		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), EventSourceErrorCode.DefaultException.getMessage(), null);
		return error;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiError handleAllExceptions(Exception exp, WebRequest request){
		logger.error(exp.getMessage(),exp);
		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), EventSourceErrorCode.DefaultException.getMessage(), null);
		return error;
	}
}
