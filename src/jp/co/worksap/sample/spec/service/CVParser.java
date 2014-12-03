package jp.co.worksap.sample.spec.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dao.KeyWordDao;
import jp.co.worksap.sample.dao.TitleDao;
import jp.co.worksap.sample.dto.CandidateDto;
@ManagedBean(name="CVParser")
@ApplicationScoped
public class CVParser {
 @ManagedProperty("#{keyWordDao}")
 private KeyWordDao kwDao;
@ManagedProperty("#{titleDao}")
private TitleDao titleDao;
 public static String test="name\tBoran Wang\nage\t22\nbirthday:\t06.25.1992\n\ntest\ntst wbr912@163.com\n\nphone number : 13505439665\ngender: female\neducation: nanjing university, bachelor\ni am good at android plateform.\nbirth day: 6.25.1992\nmail: wbr911@163.com\n\njob intention: front end engineer\n    \ncertificates\tcet-4\n\ntest\nwbr911@163.com\n\n\n";
 public static final String EMAIL_REGEX = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";
 public static final String PHONE_REGEX="1[3|4|5|8]\\d{9}";// start with 13 14 15 18 
 public static final String BIRTH_REGEX="\\d{1,2}(\\.|-|\\s|/)\\d{1,2}(\\.|-|\\s|/)\\d{4}";
 public static final String GENDER_REGEX="male|female";
 public static final String NAME_REGEX ="[A-Z][a-zA-Z]*[^\\w\\f\\r\\t\\v:]+[A-Z][a-zA-Z]*";
 public static final String NAME_REGEX_IN_TABLE ="(name|Name|NAME)(\\W+)"+NAME_REGEX;
 public CandidateDto parseCV(String content , CandidateDto cand){
	 Pattern pattern;
	 Matcher matcher;
	 String lowCaseContent = content.toLowerCase();
	 //Name
	 pattern = Pattern.compile(NAME_REGEX_IN_TABLE);
	 matcher = pattern.matcher(content);
	  if(matcher.find()){
		  String res = matcher.group();
		  cand.setName(res.replaceAll("(name|Name|NAME)(\\W+)", ""));
	  }else{
		  pattern = Pattern.compile(NAME_REGEX);
		  matcher = pattern.matcher(content);
		  while(matcher.find()){
			  if(!matcher.group().toLowerCase().contains("resume")){ // avoid the affect of title
			  cand.setName(matcher.group());
			  break;
			  }
		  }
	  }
	 // EMIAL
	  pattern = Pattern.compile(EMAIL_REGEX);
	  matcher = pattern.matcher(content);
	  if(matcher.find()){
		  cand.setEmail(matcher.group());
	  }
	  // phone
	  pattern = Pattern.compile(PHONE_REGEX);
	  matcher = pattern.matcher(content);
	  if(matcher.find()){
		  cand.setPhone(matcher.group());
	  }
	  // age
	  pattern = Pattern.compile(BIRTH_REGEX);
	  matcher = pattern.matcher(content);
	  if(matcher.find()){ 
		  Date now = new Date();
		  String birth=matcher.group();
		  birth =birth.replaceAll("\\.|-|\\s|/", "/");
		  Date pre;
		try {
			pre = new SimpleDateFormat("MM/dd/yyyy").parse(birth);
			int age =(int)((now.getTime()-pre.getTime())/(1000*60*60*24)/365);
			cand.setAge(age);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  // gender
	  pattern = Pattern.compile(GENDER_REGEX);
	  matcher = pattern.matcher(lowCaseContent);
	  if(matcher.find()){
		  cand.setGender(matcher.group());
	  }
	  
	  //title
	  ArrayList<String> titles = titleDao.getTitleList();
	  for(String title : titles){
		  if(lowCaseContent.contains(title.toLowerCase())){
			  cand.setTitle(title);
			  break;
		  }
	  }
	  //education level
	  ArrayList<String>edus = CandidateDto.getEducationList();
	  for(String edu:edus){
		  if(lowCaseContent.contains(edu.toLowerCase())){
			  cand.setEducation(edu);
			  break;
		  }
	  }
	  //tags
	  ArrayList<String>skills = kwDao.getSkills();
	  for(String skill:skills){
		  pattern = Pattern.compile("\\W"+regxEscape(skill.toLowerCase())+"\\W");
		  matcher = pattern.matcher(lowCaseContent);
		  if(matcher.find()){
			  cand.getSkills().add(skill);
			  
		  }
	  }
	  ArrayList<String>certificates = kwDao.getCertificates();
	  for(String certificate:certificates){
		  pattern = Pattern.compile("\\W"+regxEscape(certificate.toLowerCase())+"\\W");
		  matcher = pattern.matcher(lowCaseContent);
		  if(matcher.find()){
			  cand.getCertificates().add(certificate);
		  }
	  }
	  ArrayList<String>userDefines = kwDao.getUserDefines();
	  for(String userDefine:userDefines){
		  pattern = Pattern.compile("\\W"+regxEscape(userDefine.toLowerCase())+"\\W");
		  matcher = pattern.matcher(lowCaseContent);
		  if(matcher.find()){
			  cand.getKeywords().add(userDefine);
		  }
	  }
	  return cand;  
 }
	private String regxEscape(String regx){
		regx=regx.replaceAll("\\+", "\\\\+"); 
		regx=regx.replaceAll("\\.", "\\\\.");
		return regx;
	}
 public KeyWordDao getKwDao() {
	return kwDao;
}

public void setKwDao(KeyWordDao kwDao) {
	this.kwDao = kwDao;
}

public static String getTest() {
	return test;
}

public static void setTest(String test) {
	CVParser.test = test;
}



public TitleDao getTitleDao() {
	return titleDao;
}

public void setTitleDao(TitleDao titleDao) {
	this.titleDao = titleDao;
}

public static void main(String args[]){
//	Date now = new Date();
//	String birth = "6/25/1992";
//	Date pre;
//	try {
//		pre = new SimpleDateFormat("MM/dd/yyyy").parse(birth);
//	
//	int age =(int)((now.getTime()-pre.getTime())/(1000*60*60*24)/365);
//	System.out.print(age);
//	} catch (ParseException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
  Pattern p = Pattern.compile("Java");
	  Matcher m = p.matcher("require Java or C++");
	  if(m.find()){
	  System.out.println("require Java or C++".substring(m.start(), m.end()));
	  }
  
 }
}
