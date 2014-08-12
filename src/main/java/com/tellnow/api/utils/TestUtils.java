package com.tellnow.api.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.tellnow.api.domain.AgeRange;
import com.tellnow.api.domain.EducationDiscipline;
import com.tellnow.api.domain.EducationLevel;
import com.tellnow.api.domain.EducationSubDiscipline;
import com.tellnow.api.domain.Location;
import com.tellnow.api.domain.Religion;
import com.tellnow.api.domain.TellnowProfileStat;
import com.tellnow.api.domain.Topic;
import com.tellnow.api.service.ContentService;

public class TestUtils {

	@Autowired
	private static ContentService contentService;
	
	public static List<Topic> getAllTopics(){
		return contentService.getAllTopics();
	}
	
	public static Topic getRandomTopic(){
		List<Topic> topics = getAllTopics();
		if(topics!=null && !topics.isEmpty()){
			return topics.get(generateRandomInteger(topics.size()));
		} else {
			return null;
		}
	}
	
	public static List<Topic> getRandomTopics(int howMany){
		List<Topic> topics = getAllTopics();
		List<Topic> result = new ArrayList<Topic>();
		if(topics!=null && !topics.isEmpty()){
			if(howMany<=topics.size()){
				return topics;
			} else {
				Set<Integer> iSet = generateRandomIntegers(howMany, topics.size());
				for(int i : iSet){
					result.add(topics.get(i));
				}
				return result;
			}
		} else {
			return null;
		}		
	}

	public static List<AgeRange> getAllAgeRanges(){
		return contentService.getAllAgeRanges();
	}
	
	public static AgeRange getRandomAgeRange(){
		List<AgeRange> ageRanges = getAllAgeRanges();
		if(ageRanges!=null && !ageRanges.isEmpty()){
			return ageRanges.get(generateRandomInteger(ageRanges.size()));
		} else {
			return null;
		}
	}

	public static List<Religion> getAllReligions(){
		return contentService.getAllReligions();
	}

	public static Religion getRandomReligion(){
		List<Religion> religions = getAllReligions();
		if(religions!=null && !religions.isEmpty()){
			return religions.get(generateRandomInteger(religions.size()));
		} else {
			return null;
		}
	}

	public static List<EducationLevel> getAllEducationLevels(){
		return contentService.getAllEducationLevels();
	}
	
	public static EducationLevel getRandomEducationLevel(){
		List<EducationLevel> educationLevels = getAllEducationLevels();
		if(educationLevels!=null && !educationLevels.isEmpty()){
			return educationLevels.get(generateRandomInteger(educationLevels.size()));
		} else {
			return null;
		}
	}
	
	public static List<EducationDiscipline> getAllEducationDisciplines(){
		return contentService.getAllEducationDisciplines();
	}
	
	public static EducationDiscipline getEducationDiscipline(String disciplineName){
		return contentService.getEducationDiscipline(disciplineName);
	}
	
	public static List<EducationSubDiscipline> getAllEducationSubDisciplines(){
		return contentService.getAllEducationSubDisciplines();
	}
	
	public static List<EducationSubDiscipline> getEducationSubDisciplines(String disciplineName){
		return contentService.getEducationSubDisciplines(disciplineName);
	}

	public static EducationSubDiscipline getRandomEducationSubdiscipline(){
		List<EducationSubDiscipline> educationSubDisciplines = getAllEducationSubDisciplines();
		if(educationSubDisciplines!=null && !educationSubDisciplines.isEmpty()){
			return educationSubDisciplines.get(generateRandomInteger(educationSubDisciplines.size()));
		} else {
			return null;
		}
	}
	
	public static AgeRange getAgeRange(Date birthDate){
		List<AgeRange> ageRanges = getAllAgeRanges();
		long years = yearsFromDate(birthDate);
		AgeRange result = null;
		if(ageRanges!=null){
			for(AgeRange ageRange : ageRanges){
				if(ageRange.getStart()>=years && ageRange.getEnd()<=years){
					result = ageRange;
				}
			}
		} 
		return result;
	}
	
	public static TellnowProfileStat getRandomProfileStat(){
		final long MAX_QUERIES_POSTED = 1000;
		final long MAX_ANSWERS_POSTED = 2000;
		final long MAX_REWARDS_RECEIVED = 500;
		final Date MIN_LAST_QUERY_DATE = new Date();
		final Date MAX_LAST_QUERY_DATE = new Date();
		final Date MIN_LAST_ANSWER_DATE = new Date();
		final Date MAX_LAST_ANSWER_DATE = new Date();
		final Date MIN_LAST_REWARDED_DATE = new Date();
		final Date MAX_LAST_REWARDED_DATE = new Date();
		TellnowProfileStat profileStat = new TellnowProfileStat(
				generateRandomLong(MAX_QUERIES_POSTED), 
				generateRandomLong(MAX_ANSWERS_POSTED), 
				generateRandomDateBetween(MIN_LAST_QUERY_DATE, MAX_LAST_QUERY_DATE), 
				generateRandomDateBetween(MIN_LAST_ANSWER_DATE, MAX_LAST_ANSWER_DATE), 
				generateRandomLong(MAX_REWARDS_RECEIVED), 
				generateRandomDateBetween(MIN_LAST_REWARDED_DATE, MAX_LAST_REWARDED_DATE), 
				generateRandomLocation());
		return profileStat;
	}

	private static int generateRandomInteger(int max){
		Random generator = new Random();
		return generator.nextInt(max);
	}

	private static long generateRandomLong(long max){
		Random generator = new Random();
		return (long)(generator.nextDouble() * max);
	}

	@SuppressWarnings("unused")
	private static double generateRandomDoubleLessThan(double max){
		return generateRandomDoubleBetween(0, max);
	}

	private static double generateRandomDoubleBetween(double min, double max){
		Random generator = new Random();
		return min + (max - min) * generator.nextDouble();
	}

	private static Set<Integer> generateRandomIntegers(int howMany, int max){
		Set<Integer> result = new HashSet<Integer>(); 
		Random generator = new Random();
		while(result.size()<=howMany){
			int i = generator.nextInt(max);
			if(!result.contains(i)){
				result.add(i);
			}
		}
		return result;
	}
	
	private static long yearsFromDate(Date date){
		Date now = new Date();
		return (long) ((now.getTime() - date.getTime()) / (365.0 * 24.0 * 60.0 * 60.0 * 1000.0));
	}

	@SuppressWarnings("unused")
	private static long yearsBetween(Date beginDate, Date endDate){
		return (long) ((endDate.getTime() - beginDate.getTime()) / (365.0 * 24.0 * 60.0 * 60.0 * 1000.0));
	}

	public static Date generateRandomDateBetween(Date begin, Date end){
		int daysBetween = (int) ((end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000));
		int randomDay = generateRandomInteger(daysBetween);
		long millisNewDate = begin.getTime() + randomDay * (24 * 60 * 60 * 1000);
		return new Date(millisNewDate);
	}
	
	public static double generateRandomBearing(){
		final double MIN_DEGREE =   0.0;
		final double MAX_DEGREE = 360.0;
		Random r = new Random();
		return MIN_DEGREE + (MAX_DEGREE - MIN_DEGREE) * r.nextDouble();
	}

	public static double generateRandomNormalizedBearing(){
		return LatLngTool.normalizeBearing(generateRandomBearing());
	}

	public static double generateRandomLatitude(){
		final double MIN_DEGREE = -90.0;
		final double MAX_DEGREE =  90.0;
		return generateRandomDoubleBetween(MIN_DEGREE, MAX_DEGREE);
	}

	public static double generateRandomNormalizedLatitude(){
		return LatLngTool.normalizeLatitude(generateRandomLatitude());
	}

	public static double generateRandomLongitude(){
		final double MIN_DEGREE = -180.0;
		final double MAX_DEGREE =  180.0;
		return generateRandomDoubleBetween(MIN_DEGREE, MAX_DEGREE);
	}

	public static double generateRandomNormalizedLongitude(){
		return LatLngTool.normalizeLongitude(generateRandomLongitude());
	}
	
	public static Location generateRandomLocation(){
		double lat = generateRandomNormalizedLatitude();
		double lng = generateRandomNormalizedLongitude();
		return new Location(lat, lng);
	}
	
	public static Location generateRandomLocationWithinRadius(Location startPoint, double radius){
		double lat = LatLngTool.normalizeLatitude(startPoint.getLatitude());
		double lng = LatLngTool.normalizeLongitude(startPoint.getLongitude());
		double randomBearing = generateRandomNormalizedBearing();
		LatLng start = new LatLng(lat, lng);
		LatLng end = LatLngTool.travel(start, 
						randomBearing, 
						generateRandomDoubleBetween(0, radius), 
						LengthUnit.KILOMETER);
		return new Location(end.getLatitude(), end.getLongitude());
	}
}
