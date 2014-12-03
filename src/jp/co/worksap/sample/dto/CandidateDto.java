package jp.co.worksap.sample.dto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.DefaultStreamedContent;
public class CandidateDto implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 6703238075448691692L;
	public static final String IDprefix="csw";
	private String id="";
	private String pwd="";
	private String name="";
	private int age=18;
	private String gender="";
	private String phone="";
	private String email="";
    private String employer="";
    private String education=""; 
    private String title="";
    private ArrayList<String>skills;
    private ArrayList<String>certificates;
    private ArrayList<String>keywords;
    private ArrayList<Note>noteList= new ArrayList<Note>();;
    private String LastNote="No Note";
	private String pic="/img/defaultPic.png";
	private String tagsStr;
	private String jobStatus="";
	private static ArrayList<String> educationList;
	private String status="available";
	private String chosenBy="bolan wang";
	private String job = "";
    private Address address= new Address("", 0, 0);
	private String resume="";
	// temp
	private String matchedTagStr="";
	public static final String LAST_NOTE_DEFAULT = "No Note";
	
    public CandidateDto()  {
		super();
		skills = new ArrayList<String>();
		certificates = new ArrayList<String>();
		keywords = new ArrayList<String>();
	}
	public static ArrayList<String>  getEducationList(){
		if(educationList==null){
			educationList = new ArrayList<String>();
		educationList.add("Middle School");
		educationList.add("High School");
		educationList.add("Technical School");
		educationList.add("Bachelor");
		educationList.add("Master");
		educationList.add("Doctor");
		}
		return educationList;
	}
	
	public CandidateDto(String id, String name, int age, String gender,
			String phone, String status, String chosenBy, String job,
			String email, String employer, String education, Address address,
			String title, ArrayList<String> skills,
			ArrayList<String> certificates, ArrayList<String> keywords,
			ArrayList<Note> noteList, String pic) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
		this.status = status;
		this.chosenBy = chosenBy;
		this.job = job;
		this.email = email;
		this.employer = employer;
		this.education = education;
		this.address = address;
		this.title = title;
		this.skills = skills;
		this.certificates = certificates;
		this.keywords = keywords;
		this.noteList = noteList;
		this.pic = pic;
		
	}


	public ArrayList<Note> getNoteList() {
		return noteList;
	}
	public void setNoteList(ArrayList<Note> noteList) {
		this.noteList = noteList;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLastNote() {
		return LastNote;
	}
	public void setLastNote(String lastNote) {
		LastNote = lastNote;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
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
	public ArrayList<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	public String getEmployer() {
		return employer;
	}
	public void setEmployer(String employer) {
		this.employer = employer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}


	
	public String getChosenBy() {
		return chosenBy;
	}
	public void setChosenBy(String chosenBy) {
		this.chosenBy = chosenBy;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
     

   public String getTagsStr() {
		return tagsStr;
	}


	public void setTagsStr(String tagsStr) {
		this.tagsStr = tagsStr;
	}


	public String getJobStatus() {
		return jobStatus;
	}


	public String getPwd() {
	return pwd;
    }


    public void setPwd(String pwd) {
	this.pwd = pwd;
    }
    public String getJobId(){
    	return job.split("/")[0];
    }


	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String statusLabel(){
		if(status.equals("available")){
			return "<label class='label label-success my-status'>available</label>";
		}else if(status.equals("rest")){
			return "<label class='label label-default my-status'>rest</label>";
		}else if(status.equals("chosen")){
			return "<label class='label label-warning my-status'>chosen</label>";
		}else if(status.equals("placed")){
			return "<label class='label label-info my-status'>placed</label>";
		}
		return "";
		
	}
    public String jobStatusLabel(){
    	if(this.jobStatus.equals("accepted")){
    		return "<label class='label label-success my-status' >accepted</label>";
    	}
    	else if(this.jobStatus.equals("refused")){
    		return "<label class='label label-warning my-status' >refused</label>";
    	}
    	else if(this.jobStatus.equals("no response")){
    		return "<label class='label label-default my-status'>no response</label>";
    	}else{
    		return "<label class='label label-info my-status'>''</label>";
    	}
    }
    

public static void setEducationList(ArrayList<String> educationList) {
		CandidateDto.educationList = educationList;
	}
public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
public static void main (String args[]){
	File file = new File("C:/glassfish/glassfish/domains/domain1/eclipseApps/TempsRoster2/img/defaultPic.png");
	try {
		FileInputStream in= new FileInputStream(file);
	
		try {byte[] c = new byte[1024];
			while(in.read(c)!=-1){
			
			for(int i = 0 ; i < c.length ; i++){
			System.out.println(c[i]);
			}
			}		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public String getMatchedTagStr() {
	return matchedTagStr;
}
public void setMatchedTagStr(String matchedTagStr) {
	this.matchedTagStr = matchedTagStr;
}
}
