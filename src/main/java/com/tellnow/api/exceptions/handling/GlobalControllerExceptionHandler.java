package com.tellnow.api.exceptions.handling;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.NestedServletException;

import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	Logger logger = LoggerFactory.getLogger(getClass());

	private MessageSource messageSource;

	@Autowired
	public GlobalControllerExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Handles validation messages for controller methods that don't handle them directly.
	 * 
	 * @param exception
	 *            the exception that is thrown when validations errors are not handled by the controller method
	 * @return an object containing the validation messages
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationMessages handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		ValidationMessages validationMessages = new ValidationMessages();
		BindingResult result = exception.getBindingResult();

		// process the field validations
		for (FieldError fieldError : result.getFieldErrors()) {
			validationMessages.addFieldError(fieldError.getObjectName(), fieldError.getField(), messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
		}

		// process the global validations
		for (ObjectError globalError : result.getGlobalErrors()) {
			validationMessages.addFieldError(globalError.getObjectName(), globalError.getCode(), globalError.getDefaultMessage());
		}

		return validationMessages;
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	@ResponseBody
	public ResponseEntity<TellnowResponse> resolveRequestException(Exception ex) {

		logger.error(ex.getMessage(), ex);

		TellnowResponse error = new TellnowResponse();
		error.setError(new TellnowError(GlobalErrorCodes.bad_request.getErrorCode(), messageSource.getMessage(GlobalErrorCodes.bad_request.getErrorMessageCode(), null, Locale.getDefault())));
		ResponseEntity<TellnowResponse> resp = new ResponseEntity<TellnowResponse>(error, HttpStatus.BAD_REQUEST);
		return resp;
	}

	@ExceptionHandler({ NestedServletException.class })
	@ResponseBody
	public ResponseEntity<TellnowResponse> resolveException(Exception ex) {

		logger.error(ex.getMessage(), ex);

		TellnowResponse error = new TellnowResponse();
		error.setError(new TellnowError(GlobalErrorCodes.internal_server_error.getErrorCode(), messageSource.getMessage(GlobalErrorCodes.internal_server_error.getErrorMessageCode(), null, Locale.getDefault())));
		ResponseEntity<TellnowResponse> resp = new ResponseEntity<TellnowResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		return resp;
	}
}