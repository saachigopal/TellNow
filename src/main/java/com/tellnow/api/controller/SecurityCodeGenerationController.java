package com.tellnow.api.controller;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.persistence.Transient;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tellnow.api.TellnowError;
import com.tellnow.api.TellnowResponse;
import com.tellnow.api.Tropo.TropoException;
import com.tellnow.api.Tropo.TropoMessage;
import com.tellnow.api.Tropo.TropoResponse;
import com.tellnow.api.domain.VerificationSecurityCode;
import com.tellnow.api.exceptions.handling.GlobalErrorCodes;
import com.tellnow.api.repository.VerificationSecurityCodeRepository;
import com.wordnik.swagger.annotations.Api;

@PropertySource("classpath:properties/securitycode.properties")
@RestController
@Api(value = "Security Code API", description = "Security Code related Rest operations")
public class SecurityCodeGenerationController {

	private static final String ERROR_HTTP_STATUS_NOT_OK = "The response from Tropo was not OK";
	private static final String ERROR_TEMPLATE = "%s. The actual code was %d. Additional info (if any): %s";

	@Autowired
	Environment env;

	@Autowired
	VerificationSecurityCodeRepository verificationSecurityCodeRepository;

	@Autowired
	private MessageSource messages;

	@RequestMapping(value = "/api/securitycode", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<TellnowResponse> securityCode(@RequestParam(value = "phoneNumber", required = true) String phoneNumber) {

		VerificationSecurityCode securityCode = verificationSecurityCodeRepository.findByPhoneNumber(phoneNumber);

		String generatedSecurityCode = this.generateRandomSecurityCode(new Integer(env.getProperty("securitycode.length")).intValue());
		String hashedSecurityCode = DigestUtils.md5DigestAsHex(generatedSecurityCode.getBytes());
		int timeToLive = new Integer(env.getProperty("securitycode.timetolive")).intValue();
		Date expiryDate = VerificationSecurityCode.calculateExpiryDate(timeToLive);

		if (securityCode == null) {
			securityCode = new VerificationSecurityCode(phoneNumber, hashedSecurityCode);
			securityCode.setExpiryDate(expiryDate);
			verificationSecurityCodeRepository.save(securityCode);
		} else {
			securityCode.setSecurityCode(hashedSecurityCode);
			securityCode.setExpiryDate(expiryDate);
			verificationSecurityCodeRepository.save(securityCode);
		}

		ResponseEntity<TellnowResponse> response = null;

		try {
			this.sendSMS(phoneNumber, generatedSecurityCode, timeToLive);
		} catch (Exception e) {
			if (e.getClass().equals(TropoException.class)) {
				System.out.println(e.getMessage());
			}
			int errorCode = GlobalErrorCodes.unauthorized.getErrorCode();
			String errorMessage = messages.getMessage(GlobalErrorCodes.internal_server_error.getErrorMessageCode(), null, Locale.getDefault());

			TellnowError error = new TellnowError(errorCode, errorMessage);
			TellnowResponse respBody = new TellnowResponse(error);
			response = new ResponseEntity<TellnowResponse>(respBody, HttpStatus.INTERNAL_SERVER_ERROR);

			return response;
		}

		String msgTemplate = env.getProperty("securitycode.messagetoapplication");
		String token = String.format(msgTemplate, hashedSecurityCode);

		TellnowResponse respBody = new TellnowResponse(token);
		response = new ResponseEntity<TellnowResponse>(respBody, HttpStatus.OK);

		return response;
	}

	@Transient
	private String generateRandomSecurityCode(int length) {
		StringBuilder nmbrString = new StringBuilder();
		Random generator = new Random();
		for (int i = 0; i < length; i++) {
			int nmbr = generator.nextInt(10);
			nmbrString.append(new Integer(nmbr).toString());
		}
		return nmbrString.toString();
	}

	@Transient
	private void sendSMS(String numberToDial, String code, int timeToLive) throws UnsupportedCharsetException, ClientProtocolException, IOException, TropoException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postMethod = new HttpPost(env.getProperty("tropo.url"));
		ObjectMapper objectMapper = new ObjectMapper();

		String messageTemplate = env.getProperty("securitycode.messagetouser");
		String message = String.format(messageTemplate, code, timeToLive);

		TropoMessage tropoMessage = new TropoMessage(env.getProperty("tropo.token"), numberToDial, message);
		StringEntity entity = new StringEntity(objectMapper.writeValueAsString(tropoMessage), ContentType.APPLICATION_JSON);
		postMethod.setEntity(entity);

		CloseableHttpResponse response = httpClient.execute(postMethod);

		int statusCode = response.getStatusLine().getStatusCode();
		String respBody = EntityUtils.toString(response.getEntity());
		if (statusCode != HttpStatus.OK.value()) {
			try {
				ObjectMapper om = new ObjectMapper();
				TropoResponse tr = om.readValue(respBody, TropoResponse.class);
				String err = String.format(ERROR_TEMPLATE, ERROR_HTTP_STATUS_NOT_OK, statusCode, tr.getReason());
				throw new TropoException(err);
			} catch (JsonParseException e) {
				String err = String.format(ERROR_TEMPLATE, ERROR_HTTP_STATUS_NOT_OK, statusCode, e.getMessage());
				throw new TropoException(err, e);
			} catch (JsonMappingException e) {
				String err = String.format(ERROR_TEMPLATE, ERROR_HTTP_STATUS_NOT_OK, statusCode, e.getMessage());
				throw new TropoException(err, e);
			}
		}

		System.out.println("============================================");
		System.out.println("STATUS CODE: " + statusCode);
		System.out.println("BODY: " + respBody);
		System.out.println("============================================");
		response.close();
	}

}