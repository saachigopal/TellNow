package com.tellnow.api.utils.knowledgetest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tellnow.api.domain.MediaFile;
import com.tellnow.api.repository.knowledgetest.KnowledgeAnswerRepository;
import com.tellnow.api.repository.knowledgetest.KnowledgeQuestionRepository;
import com.tellnow.api.utils.AutowireHelper;

@Component
@SuppressWarnings({"unused", "deprecation"})
public class KnowledgeTestExcelHandler {

	@Autowired
	KnowledgeQuestionRepository questionRepository;

	@Autowired
	KnowledgeAnswerRepository answerRepository;

	private static final String SHEET_QUESTIONS = "Questions";
	private static final String SHEET_ANSWERS = "Answers";
	private static final String SHEET_MEDIA = "Media";
	private static final String SHEET_TOPIC = "Topic";
	
	private static final String GENERIC_OWNER = "0000-0000-0000";
	
	private Map<String,KnowledgeQuestionUtil> questionUtilMap = new HashMap<String,KnowledgeQuestionUtil>();
	
	private XSSFWorkbook workbook;
		
	public KnowledgeTestExcelHandler() {
	}
	
	public KnowledgeTestExcelHandler(InputStream inputStream) throws IOException {
		AutowireHelper.autowire(this, this.questionRepository);
		this.workbook = new XSSFWorkbook(inputStream);
		questionRepository.emptyAnswersNatively();
		questionRepository.emptyQuestionsNatively();
	}
	
	public Map<String, LinkedHashMap<Integer, List<Integer>>> parseExcelFile() {
		
		Map<String, LinkedHashMap<Integer, List<Integer>>> errormap = new LinkedHashMap<String, LinkedHashMap<Integer, List<Integer>>>();
		
		errormap.put(SHEET_QUESTIONS, parseQuestionsSheet());
		errormap.put(SHEET_ANSWERS, parseAnswersSheet());
		
		long indexQuestion = 1;
		long indexAnswer = 1;
		for(String key : questionUtilMap.keySet()){
			KnowledgeQuestionUtil questionUtil = questionUtilMap.get(key);
			String d = String.format("%04d-%02d-%02d %02d:%02d:%02d", 
										questionUtil.getCreationDate().getYear() + 1900, 
										questionUtil.getCreationDate().getMonth() + 1, 
										questionUtil.getCreationDate().getDate(), 
										questionUtil.getCreationDate().getHours(), 
										questionUtil.getCreationDate().getMinutes(), 
										questionUtil.getCreationDate().getSeconds());
			questionRepository.insertQuestionNatively(indexQuestion, 
										d, 
										questionUtil.getQuestionPublicId(),	
										questionUtil.getQuestionText(), 
										questionUtil.getTopic());
			if(questionUtil.getAnswers()!=null && !questionUtil.getAnswers().isEmpty()){
				for(KnowledgeAnswerUtil answerUtil : questionUtil.getAnswers()){
					String dd = String.format("%04d-%02d-%02d %02d:%02d:%02d", 
											answerUtil.getCreationDate().getYear() + 1900, 
											answerUtil.getCreationDate().getMonth() + 1, 
											answerUtil.getCreationDate().getDate(), 
											answerUtil.getCreationDate().getHours(), 
											answerUtil.getCreationDate().getMinutes(), 
											answerUtil.getCreationDate().getSeconds());
					answerRepository.insertAnswerNatively(indexAnswer, 
											d, 
											answerUtil.isGood(), 
											answerUtil.getPublicId(), 
											answerUtil.getText(), 
											indexQuestion);
					indexAnswer++;
				}
			}
			indexQuestion++;
		}
		
		return errormap;
	}

	private LinkedHashMap<Integer, List<Integer>> parseQuestionsSheet(){
		
		LinkedHashMap<Integer, List<Integer>> errormap = new LinkedHashMap<Integer, List<Integer>>(); 
		
		XSSFSheet sheet = workbook.getSheet(SHEET_QUESTIONS);
		XSSFRow row; 
		XSSFCell cell;

		Iterator<Row> rows = sheet.rowIterator();

		while (rows.hasNext()){
			row=(XSSFRow) rows.next();
			if(row.getRowNum()>0){
				List<Integer> errorlist = validateQuestionRow(row);
				boolean isBlankRow = isBlankQuestionRow(row);
				if(errorlist.isEmpty() && !isBlankRow){
					Long id = (long) row.getCell(15).getNumericCellValue();
					String questionPublicId = row.getCell(0).getStringCellValue();
					if(questionPublicId==null || questionPublicId.isEmpty()){
						questionPublicId = UUID.randomUUID().toString();
					}
					String questionText = row.getCell(1).getStringCellValue();
					Long topic = (long) row.getCell(3).getNumericCellValue();
					KnowledgeQuestionUtil knowledgeQuestionUtil = new KnowledgeQuestionUtil(questionPublicId, questionText, topic);
					questionUtilMap.put(questionText, knowledgeQuestionUtil);
				} else {
					if(!isBlankRow){
						errormap.put(row.getRowNum(), errorlist);
					}
				}
			}
		}
		
		return errormap;
	}

	private LinkedHashMap<Integer, List<Integer>> parseAnswersSheet(){
		
		LinkedHashMap<Integer, List<Integer>> errormap = new LinkedHashMap<Integer, List<Integer>>();
		
		XSSFSheet sheet = workbook.getSheet(SHEET_ANSWERS);
		XSSFRow row; 
		XSSFCell cell;

		Iterator<Row> rows = sheet.rowIterator();

		while (rows.hasNext()){
			row=(XSSFRow) rows.next();
			if(row.getRowNum()>0){
				List<Integer> errorlist = validateAnswerRow(row); 
				boolean isBlankRow = isBlankAnswerRow(row);
				if(errorlist.isEmpty() && !isBlankRow){
					Long id = (long) row.getCell(6).getNumericCellValue();
					String publicId = row.getCell(0).getStringCellValue();
					String questionText = row.getCell(1).getStringCellValue();
					boolean goodOrWrong = row.getCell(2).getBooleanCellValue();
					String text = row.getCell(3).getStringCellValue();
					String questionId = row.getCell(4).getStringCellValue();
					Long questionRow = (long) row.getCell(5).getNumericCellValue();
					Date date = new Date();
					if(questionUtilMap.containsKey(questionText)){
						KnowledgeQuestionUtil question = questionUtilMap.get(questionText);
						if(publicId==null || publicId.isEmpty()){
							int i = question.getNumberOfAnswers();
							publicId = String.format("%s-%07d", question.getQuestionPublicId(), i+1);
						}
						KnowledgeAnswerUtil answerUtil = new KnowledgeAnswerUtil(publicId, text, goodOrWrong, question);
						question.addAnswer(answerUtil);
					}
				} else {
					if(!isBlankRow){
						errormap.put(row.getRowNum(), errorlist);
					}
				}
			}
		}
		
		return errormap;
	}

	
	private MediaFile createMediaFileFromExcelRow(int rowNumber){
		
		XSSFSheet sheet = workbook.getSheet(SHEET_MEDIA);
		XSSFRow row = sheet.getRow(rowNumber);
		
		Iterator<Cell> cells = row.cellIterator();
		
		String name = cells.next().getStringCellValue();
		String md5Sum = cells.next().getStringCellValue();
		String path = cells.next().getStringCellValue();
		String contentType = cells.next().getStringCellValue();
		
		MediaFile mediaFile = new MediaFile(name, GENERIC_OWNER, md5Sum, path, contentType);
		
		return mediaFile;
	}
	 	
	private List<Integer> validateQuestionRow(XSSFRow row){
		
		List<Integer> errorlist = new ArrayList<Integer>();
		
		// questionPublicId
		//if(!validateCell(row.getCell(0))){
		//	result = false;
		//}

		if(!isBlankQuestionRow(row)){
			// questionText
			if(validateCell(row.getCell(1))){
				if(!validateCell(row.getCell(15))){
					errorlist.add(15);
				}
			} else {
				errorlist.add(1);
			}
			// topic
			if(validateCell(row.getCell(2))){
				if(!validateCell(row.getCell(3))){
					errorlist.add(3);
				}		
			} else {
				errorlist.add(2);
			}
		}		
		return errorlist;
	}
	
	private List<Integer> validateAnswerRow(XSSFRow row){
		
		List<Integer> errorlist = new ArrayList<Integer>();
		
		if(!isBlankAnswerRow(row)){
			// question text
			if(!validateCell(row.getCell(1))){
				errorlist.add(1);
			}
			// goodOrWrong
			if(!validateCell(row.getCell(2))){
				errorlist.add(2);
			}
			// text
			if(!validateCell(row.getCell(3))){
				errorlist.add(3);
			}
			// questionRow
			if(!validateCell(row.getCell(5))){
				errorlist.add(5);
			}
			// id
			if(!validateCell(row.getCell(6))){
				errorlist.add(16);
			}
		}
		return errorlist;
	}

	private boolean validateCell(XSSFCell cell){
		boolean result = true;
		if(cell==null){
			result = false;
		} else {
			switch(cell.getCellType()){
				case XSSFCell.CELL_TYPE_BLANK:
					result = false;
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					if(cell.getNumericCellValue()<1){
						result = false;
					}
					break;
				case XSSFCell.CELL_TYPE_STRING:
					if(cell.getStringCellValue().isEmpty()){
						result = false;
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					try{
						cell.getBooleanCellValue();
					} catch (IllegalStateException e) {
						result = false;
					}
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					switch(cell.getCachedFormulaResultType()){
						case XSSFCell.CELL_TYPE_NUMERIC:
							if(cell.getNumericCellValue()<1){
								result = false;
							}
							break;
						case XSSFCell.CELL_TYPE_STRING:
							if(cell.getStringCellValue().isEmpty()){
								result = false;
							}
							break;
						case XSSFCell.CELL_TYPE_ERROR:
							result = false;
					}
			}
		}
		return result;
	}
	
	private boolean isBlankQuestionRow(XSSFRow row){
		boolean result = true;
		for(int i=0; i<26; i++){
			XSSFCell cell = row.getCell(i);
			switch(cell.getCellType()){
				case XSSFCell.CELL_TYPE_ERROR:
					result = false;
					break;
				case XSSFCell.CELL_TYPE_STRING:
					if(!cell.getStringCellValue().isEmpty()){
						result = false;
					}
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					switch(cell.getCachedFormulaResultType()){
						case XSSFCell.CELL_TYPE_STRING:
							if(!cell.getStringCellValue().isEmpty()){
								result = false;
							}
							break;
						case XSSFCell.CELL_TYPE_ERROR:
							result = false;
					}
			}
		}
		return result;
	}
	
	private boolean isBlankAnswerRow(XSSFRow row){
		boolean result = true;
		for(int i=0; i<7; i++){
			XSSFCell cell = row.getCell(i);
			switch(cell.getCellType()){
				case XSSFCell.CELL_TYPE_ERROR:
					result = false;
					break;
				case XSSFCell.CELL_TYPE_STRING:
					if(!cell.getStringCellValue().isEmpty()){
						result = false;
					}
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					switch(cell.getCachedFormulaResultType()){
						case XSSFCell.CELL_TYPE_STRING:
							if(!cell.getStringCellValue().isEmpty()){
								result = false;
							}
							break;
						case XSSFCell.CELL_TYPE_ERROR:
							result = false;
					}
			}
		}
		return result;
	}

}
