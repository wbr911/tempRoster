package jp.co.worksap.sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.PreRemove;

import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.spec.utils.Resource;

@ManagedBean(name="keyWordDao")
@ApplicationScoped
public class KeyWordDao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8804528418920211116L;
	private ArrayList<String>allTags;
	public ArrayList<String> skills;
	public ArrayList<String> certificates;
	public ArrayList<String> userDefines;
	String skillStr;
	String certificateStr;
	String userDefineStr;
    private transient Connection con;
    private int changeTime;
    private static final int CACHE_NUMBER= 10;
	@PostConstruct
	public void init(){
		con  = Resource.getConnection();
		skills = new ArrayList<String>();
		certificates =new ArrayList<String>();
		userDefines = new ArrayList<String>();
		allTags = new ArrayList<String>();
		try {
			Statement stm = con.createStatement();
			ResultSet rs =stm.executeQuery("select tags from keyword where id=1 and kind='skills'");
			rs.next();
			skillStr=rs.getString("tags");
		    skills=parseTag(skillStr); 
			allTags.addAll(skills);
		    rs =stm.executeQuery("select tags from keyword where id=1 and kind='certificates'");
			rs.next();
			certificateStr =rs.getString("tags");
		    certificates=parseTag(certificateStr);
			allTags.addAll(certificates);
		    rs =stm.executeQuery("select tags from keyword where id=1 and kind='userDefines'");
			rs.next();
			userDefineStr = rs.getString("tags");
		    userDefines=parseTag(userDefineStr);
		    allTags.addAll(userDefines);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@PreDestroy
	private void destroy(){
		if(changeTime!=0){
		updateTags(skillStr, "skills");
		updateTags(certificateStr, "certificates");
		updateTags(userDefineStr, "userDefines");
		}
	}
	public void updateNewTags(CandidateDto cand){
		updateTagList2TagStr("skills", cand.getSkills());
		updateTagList2TagStr("certificates", cand.getCertificates());
		updateTagList2TagStr("userDefines", cand.getKeywords());
	}
	private void updateTagList2TagStr(String type ,ArrayList<String> tags){
		String tagStr ="";
		if(type.equals("skills")){
			tagStr = skillStr;
		}else if(type.equals("certificates")){
			tagStr = certificateStr;
		}else{
			tagStr = userDefineStr;
		}
		String lowerCaseStr = tagStr.toLowerCase();
		
		for(String tag : tags){
			if(lowerCaseStr.contains(tag.toLowerCase()+",")){
				continue;
			}else{
				addNewTag(tag, type);
				lowerCaseStr+= tag+",";
				
			}
		}
		
	}
	public List<String> completeSkillsTag(String query){
		List<String> res = new ArrayList<String>();
		for(String str : skills){
			if(str.toLowerCase().startsWith(query.toLowerCase())){
				res.add(str);
			}
		}
		return res ;
		//return tags;
	}
	public List<String> completeCertificatesTag(String query){
		List<String> res = new ArrayList<String>();
		for(String str : certificates){
			if(str.toLowerCase().startsWith(query.toLowerCase())){
				res.add(str);
			}
		}
		return res ;
		//return tags;
	}
	public List<String> completeUserDefinesTag(String query){
		List<String> res = new ArrayList<String>();
		for(String str : userDefines){
			if(str.toLowerCase().startsWith(query.toLowerCase())){
				res.add(str);
			}
		}
		return res ;
		//return tags;
	}
	public void addNewTag(String tag,String kind){
		allTags.add(tag);
		String var="";
		if(kind.equals("skills")){
			skills.add(tag);
		
			skillStr = skillStr+tag+",";
			var = skillStr;
		}
		if(kind.equals("certificates")){
			certificates.add(tag);
			certificateStr = certificateStr+tag+",";
			var = certificateStr;
		}
		if(kind.equals("userDefines")){
			userDefines.add(tag);
			userDefineStr = userDefineStr+tag+",";
			var = userDefineStr;
		}
		changeTime++;
		if(changeTime>=CACHE_NUMBER){
			updateTags(var, kind);
			changeTime=0;
		}
	}
	private void updateTags(String tags,String kind){
		   con = Resource.getConnection();
		try {
			PreparedStatement stm = con.prepareStatement("update keyword set tags=? where id=1 and kind=?");
			stm.setString(1, tags);
			stm.setString(2, kind);
			stm.executeUpdate();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getAllTags(){
		return allTags;
	}
	private ArrayList<String>parseTag(String tagStr){
		String[] tags = tagStr.split(",");
		ArrayList<String>res = new ArrayList<String>();
		for(int i = 0 ; i< tags.length ;i++){
			res.add(tags[i]);
		}
		return res;
	}
	public void createMockData(){
		con  = Resource.getConnection();
		try {
			con.createStatement().executeUpdate("insert into keyword values(1,'skills'," +
					"'ARM,DSP,STM32,AVR,PCB design,Photoshop,Dreamweaver,flash,unity3d,ruby,matlab,go,agile,waterfall model," +
					"Scrum,Hadoop,SOA,large-scale distributed development,machine learning,massive server development," +
					"data mining,webservice,saas,Android,PhoneGap,Android Js,mobile test,js,css,xml,html5,json,node.js," +
					"ruby,ajax,apache,Objective-c,Mac OS X,Xcode,c++,Oracle,Mysql,SqlServer,MapReduce,data structure,java," +
					"j2ee,java8,spring,hibernate,struts,jsf,JVM,junit,SQL,Intellij IDEA,')");
			con.createStatement().executeUpdate("insert into keyword values(1,'certificates','CCNA,TOEFL,TOEIC,CET4,CET6,IELTS,PMP,ACCA,')");
			con.createStatement().executeQuery("insert into keyword values(1,'userDefines','willing to work overtime,userDefine1,userDefine2,')");
		    con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ArrayList<String> getSkills() {
		return skills;
	}
	public void setSkills(ArrayList<String> skills) {
		this.skills = skills;
	}
	public ArrayList<String> getCertificates() {
		return certificates;
	}
	public void setCertificates(ArrayList<String> certificates) {
		this.certificates = certificates;
	}
	public ArrayList<String> getUserDefines() {
		return userDefines;
	}
	public void setUserDefines(ArrayList<String> userDefines) {
		this.userDefines = userDefines;
	};
	
	public static void main(String args[]){
		ArrayList<String>list = new KeyWordDao().parseTag("a,b,");
		for(String str:list){
			System.out.println(str+"/");
		}
	}

}
