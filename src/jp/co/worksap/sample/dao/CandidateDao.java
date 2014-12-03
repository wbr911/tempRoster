package jp.co.worksap.sample.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.sql.DataSource;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;

import com.sun.faces.taglib.TagParser;

import jp.co.worksap.sample.dto.Address;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.JobDto;
import jp.co.worksap.sample.dto.TitleDto;

import jp.co.worksap.sample.dto.Note;
import jp.co.worksap.sample.spec.utils.Resource;
import jp.co.worksap.sample.spec.utils.TagsParser;
@ManagedBean(name="candidateDao")
@SessionScoped
public class CandidateDao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6175186498447387693L;
	private transient Connection con;
	private static final String GET_ALL="select * from cand order by id";
	private static final String ADD="insert into employees values(?,?,?,?,?,?,?,?,?,?,?)";
	private ArrayList<CandidateDto> candList;
	private int idCount;
	private int noteIdCount;
	@PostConstruct
	public void init(){
		con= Resource.getConnection();
		candList = new ArrayList<CandidateDto>();
		refresh();
		try {
			Statement stm =con.createStatement();
			ResultSet res = stm.executeQuery("select max(id) from note");
			res.next();
			noteIdCount = res.getInt(1);
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//testData();
		if(candList.size()==0){
			idCount=1;
		}else{
		idCount =Integer.valueOf(candList.get(candList.size()-1).getId().replaceAll(CandidateDto.IDprefix, ""))+1;
		}	
	}
	
	private CandidateDto fillCandDto(ResultSet basicRes){
		CandidateDto cand = new CandidateDto();
		try {
		cand.setId(CandidateDto.IDprefix+basicRes.getInt(1)+"");
		cand.setPhone(basicRes.getString(2));
		cand.setName(basicRes.getString(3));
		cand.setAge(basicRes.getInt(4));
		cand.setGender(basicRes.getString(5));
		cand.setPhone(basicRes.getString(6));
		cand.setEmail(basicRes.getString(7));
		cand.setEmployer(basicRes.getString(8));
		cand.setEducation(basicRes.getString(9));
		cand.setTitle(basicRes.getString(10));
		cand.setLastNote(basicRes.getString(11));
		cand.setPic(basicRes.getString(12));
		cand.setTagsStr(basicRes.getString(13)==null?"":basicRes.getString(13));
		ArrayList<String>[]tags = TagsParser.parseTag(cand.getTagsStr());
		cand.setSkills(tags[0]);
		cand.setCertificates(tags[1]);
		cand.setKeywords(tags[2]);
		cand.setJobStatus(basicRes.getString(14));
		cand.setStatus(basicRes.getString(15));
		cand.setChosenBy(basicRes.getString(16));
		cand.setJob(basicRes.getString(17));
		cand.getAddress().setDes(basicRes.getString(18));
		CLOB clob = (CLOB) basicRes.getClob(19);
		if(clob!=null){
			cand.setResume(clob.getSubString((long)1, (int)clob.length()));
		}
		
		cand.setNoteList(getNoteListByCandId(cand.getId()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cand;
	}
	private ArrayList<Note> getNoteListByCandId(String id){
		   con = Resource.getConnection();
		id = id.replaceAll(CandidateDto.IDprefix, "");
		ArrayList<Note> noteList = new ArrayList<Note>();
		String sql = "select * from note where candId="+id;
		try {
			Statement stm = con.createStatement();
			ResultSet res = stm.executeQuery(sql);
			while(res.next()){
				Note n = new Note();
				n.setId(res.getInt(1)+"");
				n.setDate(res.getString(2));
				n.setContent(res.getString(3));
				n.setConsultant(res.getString(4));
				noteList.add(n);
			}
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return noteList;
	}
	public void updateStatus(CandidateDto cand){
		con = Resource.getConnection();
		String sql = "update cand set status = ? where id=?";
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setString(1, cand.getStatus());
			stm.setInt(2,Integer.valueOf(cand.getId().replaceAll(CandidateDto.IDprefix, "")) );
			stm.executeUpdate();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<CandidateDto> getAllCandidates(){
		return candList;
	}
    public ArrayList<CandidateDto> refresh(){
    	   con = Resource.getConnection();
    	Statement stm;
		try {
			stm = con.createStatement();
		ResultSet res = stm.executeQuery(GET_ALL);
		candList = new ArrayList<CandidateDto>();
		while(res.next()){
			CandidateDto cand = fillCandDto(res);
			candList.add(cand);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return candList;
    }
	public void addNote(String candId,Note note){
		   con = Resource.getConnection();
	    String sql= "insert into note values(?,?,?,?,?)";
		note.setId(++noteIdCount+"");
		try {
			PreparedStatement stm =con.prepareStatement(sql);
			stm.setInt(1, Integer.valueOf(note.getId()));
			stm.setString(2, note.getDate());
			stm.setString(3, note.getContent());
			stm.setString(4, note.getConsultant());
			stm.setInt(5,Integer.valueOf(candId.replaceAll(CandidateDto.IDprefix, "")));
			stm.executeUpdate();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    public void deleteNote(Note note){
    	   con = Resource.getConnection();
		String sql = "delete from note where id=" + note.getId();
		try {
			Statement stm = con.createStatement();
			stm.executeUpdate(sql);
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public ArrayList<CandidateDto>getCandsByJobId(String id,int number){
    	ArrayList<CandidateDto> res = new ArrayList<CandidateDto>();
    	for(CandidateDto cand:candList){
    		if(number==0){
    			break;
    		}
    		if(cand.getJob()==null||cand.getJob().equals("")){
    			continue;
    		}else if(cand.getJob().split("/")[0].equals(id)){
    			res.add(cand);
    			number--;
    		}
    	}
    	return res;
    }
    private void fillCandPstm(PreparedStatement stm,CandidateDto cand){
    	   con = Resource.getConnection();
    	try {
			stm.setInt(1,Integer.valueOf(cand.getId().replaceAll(CandidateDto.IDprefix, "")));
			stm.setString(2, cand.getPwd());
			stm.setString(3,cand.getName());
			stm.setInt(4,cand.getAge());
			stm.setString(5,cand.getGender());
			stm.setString(6,cand.getPhone());
			stm.setString(7,cand.getEmail());
			stm.setString(8,cand.getEmployer());
			stm.setString(9,cand.getEducation());
			stm.setString(10,cand.getTitle());
			stm.setString(11,cand.getLastNote());
			stm.setString(12,cand.getPic());
			stm.setString(13,cand.getTagsStr());
			stm.setString(14,cand.getJobStatus());
			stm.setString(15,cand.getStatus());
			stm.setString(16,cand.getChosenBy());
			stm.setString(17,cand.getJob());
			stm.setString(18,cand.getAddress().getDes());
			CLOB clob = new CLOB((OracleConnection)con);
			clob = oracle.sql.CLOB.createTemporary((OracleConnection)con,true,1);
			stm.setClob(19, clob);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    public void add(CandidateDto cand){
    	   con = Resource.getConnection();
    	String sql="insert into cand values("+ createPlace(19) +")";
    	try {
			PreparedStatement stm = con.prepareStatement(sql);
			con.setAutoCommit(false);
			fillCandPstm(stm, cand);
			stm.executeUpdate();
			con.commit();
			stm.close();
			updateClob(cand.getId(), cand.getResume());
			con.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	idCount++;
    }
    private void updateClob(String candId,String content){
    	   con = Resource.getConnection();
		try {
			PreparedStatement stm = con.prepareStatement("select resume from cand where id=? for update");
			stm.setInt(1, Integer.valueOf(candId.replaceAll(CandidateDto.IDprefix, "")));
			ResultSet rs =stm.executeQuery();
			rs.next();
			CLOB clob = (CLOB) rs.getClob(1);
			clob.putString(1, content);
			stm =con.prepareStatement("update cand set resume=? where id=?");
			stm.setClob(1, clob);
			stm.setInt(2, Integer.valueOf(candId.replaceAll(CandidateDto.IDprefix, "")));
			stm.executeUpdate();
			con.commit();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    public String newId(){
    	if(candList==null || candList.size()==0){
    		return CandidateDto.IDprefix+"1";
    	}
    	return CandidateDto.IDprefix+idCount;
    }
    public void delete(CandidateDto dto){
    	   con = Resource.getConnection();
    	String sql ="delete from cand where id="+ dto.getId().replaceAll(CandidateDto.IDprefix, "");
    	try {
    		Statement stm = con.createStatement();
			stm.executeUpdate(sql);
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(Integer.valueOf(dto.getId().replaceAll(CandidateDto.IDprefix, ""))==idCount-1){
    		idCount--;
    	}
    }
    public void updateTag(CandidateDto cand){
    	   con = Resource.getConnection();
    	String sql = "update cand set tagsStr = ? where id = ?";
    	PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.setString(1,cand.getTagsStr());
			stm.setInt(2,Integer.valueOf(cand.getId().replaceAll(CandidateDto.IDprefix, "")));
			stm.execute();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    }
    public void update(CandidateDto dto){
    	delete(dto);
    	add(dto);
    }
    public void deleteBatch(List<CandidateDto> list){
    	   con = Resource.getConnection();
    	String sql="delete from cand where id=?";
    	PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			for(CandidateDto dto:list){
				stm.setInt(1, Integer.valueOf(dto.getId().replaceAll(CandidateDto.IDprefix, "")));
				stm.addBatch();
			}
			stm.executeBatch();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public CandidateDto getById(String id){
    	for(CandidateDto cand : candList){
    		if(cand.getId().equals(id)){
    			return cand;
    		}
    	}
    	return null;
    }
    private String createPlace(int number){
    	String res="";
    	for(int i = 0 ;i < number ;i++){
    		if(i==number-1){
    			res+="?";
    		}else{
    		res+="?,";
    		}
    	}
    	return res;
    	
    }
    
	 void testData(TitleDao titleDao){
		 
		con= Resource.getConnection();
		candList = new ArrayList<CandidateDto>();
		ResultSet rs;
		noteIdCount =0;
		try {
			con.createStatement().executeUpdate("delete from cand");
			con.createStatement().executeUpdate("delete from note");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		String certificates = "CCNA,TOEFL,TOEIC,CET4,CET6,IELTS,PMP";
		String keys ="willing to work overtime,userDefine1,userDefine2";
		ArrayList<CandidateDto>res = new ArrayList<CandidateDto>();
	    ArrayList<Note> noteList = new ArrayList<Note>();
	    noteList.add(new Note("bolan wang", "said that he don't want to take hard work.", "9/18/2014"));
	    noteList.add(new Note("bolan wang", "said that he will take a rest until 1/1/2015", "9/18/2014"));
	    
	    String[]address={"Yanan West Road,Chang Feng Center","Zhongtan Road,Zhong Yuan City","Pudong,Yu Lan Xiang Yuan","Nanjing East Road 99 long 57"
	    		,"Yanan East Road,Quan Feng center","Huanghe Road 513,Guandao Garden"};
	    int addressSize = address.length;
		for(int i = 0 ; i < 100;i++){
	    	CandidateDto dto = new CandidateDto();
	    	dto.setId(i+1+"");
	    	dto.setPwd("18551831696");
	    	dto.setName("wangboran"+i);
	    	TitleDto title = titleDao.getTitles().get((int)(Math.random()*11));
	    	dto.setTitle(title.title);
	    	dto.setPhone("18551831696");
	    	dto.setEmail("wbr911@163.com");
	    	dto.setStatus(Math.random()>0.9? "rest":"available");
	    	dto.setLastNote("9/30/2014");
	    	dto.setSkills(TagsParser.createRandomTags(title.skills,3));
	    	dto.setCertificates(TagsParser.createRandomTags(certificates,2));
	    	dto.setKeywords(TagsParser.createRandomTags(keys,1));
	    	dto.setNoteList(noteList);
	    	dto.setAddress(new Address(address[(int)(Math.random()*addressSize)], 0, 0));
	    	dto.setEducation(CandidateDto.getEducationList().get(i%6));
	    	dto.setTagsStr(TagsParser.createTagsStr(dto));
	    	dto.setAge((int)(Math.random()*20+18));
	    	dto.setGender((Math.random()>0.5? "male":"female"));
	    	for(Note n: dto.getNoteList()){
	    		addNote(dto.getId(), n);
	    	}
	    	add(dto);
	    	
	    }
	    
	}
    public static void main(String args[]){
    	
    	
//    	CandidateDao cao =new CandidateDao();
//    	TitleDao td = new TitleDao();
//    	td.init();
//    	cao.testData(td);	
    	try {
			Resource.getConnection().createStatement().executeUpdate("update cand set pic='/img/defaultPic.png'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
