package com.tellnow.api.utils;

import java.nio.charset.Charset;
import java.util.Date;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class TellNowUtils {

	private static final String FILE_SEPARATOR = "/";

	public static String getProfileId(String useridentifier) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		String strEncodedPassword = md5.encodePassword(useridentifier, null);
		return strEncodedPassword;
	}

	public static String getQuestionId(String owner, String question, boolean addTimestamp) {
		StringBuilder sb = new StringBuilder(owner);
		sb.append(question);
		if (addTimestamp) {
			sb.append(new Date().toString());
		}
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		String strEncodedPassword = md5.encodePassword(sb.toString(), null);
		return strEncodedPassword;
	}

	public static String getAnswerPublicId(String questionId, String answerText) {
		StringBuilder sb = new StringBuilder(questionId);
		sb.append(answerText);
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		String strEncodedPassword = md5.encodePassword(sb.toString(), null);
		return strEncodedPassword;
	}

	public static String getImageFileDir(String imageDir, String profileId) {
		StringBuilder path = new StringBuilder(imageDir);
		path.append(FILE_SEPARATOR).append(profileId);
		return path.toString();
	}

	public static String getUrl(String urlPrefix, String profileId, String name) {
		StringBuilder url = new StringBuilder(urlPrefix);
		url.append(FILE_SEPARATOR).append(profileId);
		url.append(FILE_SEPARATOR).append(name);
		return url.toString();
	}

	public static String truncateNotification(String message, int allowedLength){
		String result = message;
		if(message!=null && message.getBytes(Charset.forName("UTF-8")).length > allowedLength){
			while(result.getBytes(Charset.forName("UTF-8")).length > allowedLength){
				int p = result.length(); 
				result = result.substring(0, p-1);
			}
			result = result + "...";
		} 
		return result;
	}

	public static String getPayloadMessage(String payload) {
		try {
			return new String(payload);
		} catch (Exception e) {
		}
		return null;
	}
}
