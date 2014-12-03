package jp.co.worksap.sample.dao;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.CLOB;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;



import jp.co.worksap.sample.dto.Address;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.ClientDto;
import jp.co.worksap.sample.dto.JobDto;
import jp.co.worksap.sample.spec.utils.Resource;
@ManagedBean(name="jobDao")
@SessionScoped
public class JobDao implements Serializable{

	private static final long serialVersionUID = -4387819046346336870L;
	/**
	 * 
	 */
	@ManagedProperty("#{candidateDao}")
	private CandidateDao candDao;
	@ManagedProperty("#{clientDao}")
	private ClientDao clientDao;
    private transient Connection conn;
	private ArrayList<JobDto>jobList;
	private ArrayList<String>titleList;
	private int newRefuse;
	private int newAccept;
	private int cacheNumber=0;
	private ArrayList<JobDto>updateCacheList = new ArrayList<JobDto>();
	@PostConstruct
	public void init(){
		conn = Resource.getConnection();
		jobList = new ArrayList<JobDto>();
		titleList = new ArrayList<String>();
		//createMockData();
		String jobSql = "select * from job order by id";
		
		try {
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(jobSql);
			while(rs.next()){
			jobList.add(fillJob(rs));	
			}
			String titleSql = "select distinct title from title";
			rs = stm.executeQuery(titleSql);
			while(rs.next()){
				titleList.add(rs.getString(1));
			}
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@PreDestroy
	private void destroy(){
		updateJobInBatch(updateCacheList);
		cacheNumber=0;
		updateCacheList = new ArrayList<JobDto>();
	}
 	private JobDto fillJob(ResultSet rs){
		
		JobDto job = new JobDto();
		try {
			job.setId(JobDto.IDprefix+rs.getInt("id")+"");
			job.setTitle(rs.getString("title"));
			job.setClient(clientDao.getById(rs.getString("clientId")));
			job.setAmount(rs.getInt("amount"));
			Clob clob =rs.getClob("des");
			if(clob!=null){
				job.setDes(clob.getSubString((long)1, (int)clob.length()));
			}
			
			job.getAddress().setDes(rs.getString("address"));
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            job.setStartDate(sdf.parse(rs.getString("startDate")));
			job.setBeginDate(sdf.parse(rs.getString("beginDate")));
			job.setEndDate(sdf.parse(rs.getString("endDate")));
			job.setAccepted(rs.getInt("accepted"));
			job.setRefused(rs.getInt("refused"));
			job.setNoResponse(rs.getInt("noResponse"));
			job.setCandidates(candDao.getCandsByJobId(job.getId(), job.getNumber()));
			job.setSalaries(rs.getDouble("salaries"));
			job.setMaxHour(rs.getInt("maxHour"));
			job.setStatus(rs.getString("status"));
			job.setQuotation(rs.getDouble("quotation"));
			job.setTagsStr(rs.getString("tagsStr"));
			Clob clob2 =rs.getClob("tagRequirement");
			if(clob2!=null){
				job.setTagRequirement(clob2.getSubString((long)1, (int)clob2.length()));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return job;
	}
	
	private void fillJobPstm(JobDto job,PreparedStatement stm){
		conn = Resource.getConnection();
		try {
			stm.setInt(1, Integer.valueOf(job.getId().replaceAll(JobDto.IDprefix, "")));
			stm.setString(2, job.getTitle());
			stm.setString(3, job.getClient().getName());
			stm.setInt(4, job.getAmount());
			//stm.setString(5, job.getDes());
			CLOB clob = oracle.sql.CLOB.createTemporary((OracleConnection)conn,true,1);
			stm.setClob(5, clob);
			stm.setString(6,job.getAddress().getDes());
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			stm.setString(7, sdf.format(job.getStartDate()));
			stm.setString(8, sdf.format(job.getBeginDate()));
			stm.setString(9, sdf.format(job.getEndDate()));
			stm.setInt(10, job.getAccepted());
			stm.setInt(11, job.getRefused());
			stm.setInt(12, job.getNoResponse());
			stm.setDouble(13, job.getSalaries());
			stm.setInt(14, job.getMaxHour());
			stm.setString(15, job.getStatus());
			stm.setDouble(16, job.getQuotation());
			stm.setString(17, job.getTagsStr()); 
			 CLOB clob2 = oracle.sql.CLOB.createTemporary((OracleConnection)conn,true,1);
			stm.setClob(18, clob2);
		
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ArrayList<JobDto> getAllJob(){
		return jobList;
		
	}
	public void editJobsStatus(ArrayList<JobDto> datas){
		
		conn = Resource.getConnection();
		String sql = "update job set status = ? where id=?";
		try {
			PreparedStatement stm = conn.prepareStatement(sql);
			for(JobDto job:datas){
			    stm.setString(1, job.getStatus());
			    stm.setInt(2, Integer.valueOf(job.getId().replaceAll(JobDto.IDprefix, "")));
			    stm.addBatch();
			}
			stm.executeBatch();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addJob(JobDto job){
		conn = Resource.getConnection();
		String sql = "insert into job values("+createPlace(18)+")";
		try {
			OracleConnection OCon = (OracleConnection)conn;
			OraclePreparedStatement stm = (OraclePreparedStatement)OCon.prepareCall(sql);
			conn.setAutoCommit(false);
			fillJobPstm(job, stm);
			stm.executeUpdate();
			stm.close();
			conn.commit();
			updateClob(job.getId(),"des",job.getDes());
			updateClob(job.getId(),"tagRequirement",job.getTagRequirement());
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	private void updateClob(String jobId,String column,String content){
		conn = Resource.getConnection();
		try {
			PreparedStatement stm = conn.prepareStatement("select "+column+" from job where id=? for update");
			stm.setInt(1, Integer.valueOf(jobId.replaceAll(JobDto.IDprefix, "")));
			ResultSet rs =stm.executeQuery();
			rs.next();
			CLOB clob = (CLOB) rs.getClob(1);
			clob.putString(1, content);
			stm =conn.prepareStatement("update job set "+column+"=? where id=?");
			stm.setClob(1, clob);
			stm.setInt(2, Integer.valueOf(jobId.replaceAll(JobDto.IDprefix, "")));
			stm.executeUpdate();
			conn.commit();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void updateJobInBatch(ArrayList<JobDto>list){
		deleteJobs(list);
		for(JobDto job:list){
			addJob(job);
		}
	}
	public void deleteJobs(ArrayList<JobDto>list){
		conn = Resource.getConnection();
		String sql = "delete from job where id=?";
		try {
			PreparedStatement stm = conn.prepareStatement(sql);
			for(JobDto job : list){
				stm.setInt(1, Integer.valueOf(job.getId().replaceAll(JobDto.IDprefix, "")));
				stm.addBatch();
			}
			
			stm.executeBatch();
			stm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ArrayList<String>getAllTitle(){
		return titleList;
	}
	private void cacheUpdate(JobDto job){
		boolean exist = false;
		for(JobDto pre:updateCacheList){
			if(pre.getId().equals(job.getId())){
				exist = true;
				break;
			}
		}
		if(!exist){
			updateCacheList.add(job);
		}
		cacheNumber++;
		if(cacheNumber>=10){
			updateJobInBatch(updateCacheList);
			cacheNumber=0;
			updateCacheList = new ArrayList<JobDto>();
		}
	}
	public void editTagRequirement(JobDto job){
		cacheUpdate(job);
//		conn = Resource.getConnection();
//		String sql = "update job set tagRequirement = empty_clob() where id=?";
//		
//		try {
//			conn.setAutoCommit(false);
//			PreparedStatement pstm =conn.prepareStatement(sql);
//			pstm.setInt(1, Integer.valueOf(job.getId().replaceAll(JobDto.IDprefix, "")));
//			pstm.executeUpdate();
//			this.updateClob(job.getId(), "tagRequirement", job.getTagRequirement());
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		
	}
	public void editJob(JobDto job){
		cacheUpdate(job);
//		 long statTime =System.currentTimeMillis();
//	     ArrayList<JobDto>list= new ArrayList<JobDto>();
//	     list.add(job);
//	     deleteJobs(list);
//	     addJob(job);
//	     long endTime =System.currentTimeMillis();
//	     System.out.println("edit Job:" + (endTime-statTime)/1000);
	}
	public ArrayList<JobDto> getJobByClientId(String id){
		ArrayList<JobDto> res = new ArrayList<JobDto>();
		for(JobDto job:jobList){
			if(job.getClient().getName().equals(id)){
				res.add(job);
			}
		}
		return res;
	}
	private  ArrayList<JobDto> createMockData(){
		conn = Resource.getConnection();
		titleList = new ArrayList<String>();
		String titleSql = "select distinct title from title";
		try {
		ResultSet rs;
		  
			rs = conn.createStatement().executeQuery(titleSql);
		while(rs.next()){
			titleList.add(rs.getString(1));
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<JobDto> faked = new ArrayList<JobDto>();
		ArrayList<CandidateDto> cands = new CandidateDao().getAllCandidates();
		Calendar c =java.util.Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		for(int i =0,j=0;i<7;i++){
			JobDto dto = new JobDto();
			dto.setId(i+"");
			dto.setTitle(titleList.get((int)(Math.random()*titleList.size())));
			dto.setClient(clientDao.getAllClients().get(i));
			dto.setAmount(i*3+2);
			dto.setAddress(new Address("change feng center", 0, 0));
			dto.setStatus(JobDto.PREPARE);
		
			dto.setSalaries(100.0);
			c.set(2014, 8, 29);
			Date d = c.getTime();
			dto.setBeginDate(d);
			c.set(2014, 11, 29);
			d = c.getTime();
			dto.setEndDate(d);
			c.set(2014, 10,9);
			d = c.getTime();
			dto.setStartDate(d);
			dto.setQuotation(120);
			dto.setDes("Qualifications <br/>"+	
"4-year University degree or higher<br/>"+
"Necessary skills and qualities <br/>"+	
"Ability to communicate on an operational level in English<br/>"+
"At least two years' experience developing and programming with Java or C++<br/>"+
"Job Summary<br/>"+	
"(1) Conduct research regarding the latest technologies<br/>"+
"(2) Develop new products and functions for the company's ERP software package<br/>"+
"Job Description<br/>"+	
"(1) Be an explorer in cutting edge technologies<br/>"+	
"Be on the lookout for the latest technologies and conduct research on their practical applications in products. Specific duties may include:<br/>"+	
"Apply new technologies to resolve plans and functions that are impossible using existing technologies.<br/>"+	
"Implement new technologies to significantly improve existing products and functions.<br/>"+	
"(2) Software architect role<br/>"+	
"Oversee the entire planning, design and installation processes for new products and functions.<br/>"+	
"Create new products and functions while assuming full authority over assigned areas.<br/>"+	
"Engage proactively in the planning, design and installation processes, rather than entirely relying on instructions from the client.<br/>"+	
"Plan products from a product-oriented standpoint<br/>"+	
"Explore potential as well as apparent needs, and create products and functions based on a perceived vision of future trends.<br/>"+	
"Examine your own ideas from multiple angles and increase the accuracy of conceptualized systems, regardless of your assigned division.<br/>"+	
"Design and install advanced software to benefit world-class clients.<br/>"+	
"Design and install versatile software that offers benefits to all of our clients, who are in the hundreds at the moment.<br/>"+	
"Consider upgradability in the future.<br/>"+	
"Coordination efforts with related products and functions.<br/>"+	
"Our ERP system is a massive, and covers all operational processes relevant to client management. Successful candidates will therefore be required to understand the overall structure and functions of our system, and design and install software to enable effective and seamless coordination with other related products and functions.");
		faked.add(dto);
		addJob(dto);
		}
		return faked;
	}
    private int getAmount(List<CandidateDto> list , String status){
    	int count=0;
    	for(CandidateDto dto:list){
    		if(dto.getJobStatus().equals(status)){
    			count++;
    		}
    	}
    	return count;
    }
    public JobDto getById(String id){
    	for(JobDto job : jobList){
    		if(job.getId().equals(id)){
    			return job;
    		}
    	}
    	return null;
    }
    public String updateJob(String jobId , CandidateDto cand ,String status){
    	String res = "";
    	JobDto job =this.getById(jobId);
    	if(status.equals("accepted")){
    	    if(job.getAccepted()<job.getAmount()){  		
        		cand.setJob(job.getSummaryInfo());
        		job.setAccepted(job.getAccepted()+1);
        		cand.setJobStatus(status);
        		if(cand.getStatus().equals("available")){
        			cand.setStatus("chosen");
        			job.getCandidates().add(cand);      
        		}else{// no response to accept
        			job.setNoResponse(job.getNoResponse()-1);
        			
        		}
        	if(job.getAccepted()==job.getAmount()){	
        		job.setStatus(JobDto.FINISHED);
        	}
        	
    	    }else{
        		if(job.getStatus().equals(JobDto.PLACED)){
        			return "the job are already finished";
        		}
        		return "we have found enough candidates for the job(" + job.getAmount() +"/"+job.getAmount()+")";
        	}
    	}
    	else if(status.equals("refused")){
    		
    		cand.setJobStatus(status);
    		job.setRefused(job.getRefused()+1);
    		job.setNoResponse(job.getNoResponse()-1);
    		
    	}
    	else if(status.equals("no response")){
    		 if(job.getAccepted()>=job.getAmount()){ 
    			 if(job.getStatus().equals(JobDto.PLACED)){
         			return "the job are already finished";
         		}
    				return "we have found enough candidates for the job(" + job.getAmount() +"/"+job.getAmount()+")";
    		 } 
    		cand.setJob(job.getSummaryInfo());
    		cand.setStatus("chosen");
    		cand.setJobStatus(status);
    		job.getCandidates().add(cand);
    		job.setNoResponse(job.getNoResponse()+1);
    	}
    	this.editJob(job);
    	candDao.update(cand);
    	return "success";
    }
    public String updateJobFromUserPortal(String jobId , CandidateDto cand ,String status){
    	String res =updateJob(jobId ,cand ,status);
    		if(status.equals("accepted") && res.equals("success")){
    			this.newAccept++;
    			JobDto job = getById(jobId);
    			job.setNewAccept(job.getNewAccept()+1);
    		}else if(status.equals("refused") && res.equals("success")){
    			this.newRefuse++;
    			JobDto job = getById(jobId);
    			job.setNewRefuse(job.getNewRefuse()+1);
    	}
    	return res;
    }
    
	public int getNewRefuse() {
		return newRefuse;
	}
	public void setNewRefuse(int newRefuse) {
		this.newRefuse = newRefuse;
	}
	public int getNewAccept() {
		return newAccept;
	}
	public void setNewAccept(int newAccept) {
		this.newAccept = newAccept;
	}
	public String newId(){
		if(jobList==null || jobList.size()==0){
			return JobDto.IDprefix+0;
		}else{
			return JobDto.IDprefix+(Integer.valueOf(jobList.get(jobList.size()-1).getId().replaceAll(JobDto.IDprefix, ""))+1);
		}
	}
	public ClientDao getClientDao() {
		return clientDao;
	}
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	public ArrayList<JobDto> getJobList() {
		return jobList;
	}
	public void setJobList(ArrayList<JobDto> jobList) {
		this.jobList = jobList;
	}
	public ArrayList<String> getTitleList() {
		return titleList;
	}
	public void setTitleList(ArrayList<String> titleList) {
		this.titleList = titleList;
	}
	public CandidateDao getCandDao() {
		return candDao;
	}
	public void setCandDao(CandidateDao candDao) {
		this.candDao = candDao;
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
	
	public static void main(String args[]){
		JobDao dao = new JobDao();
		
		ClientDao clientDao = new ClientDao();
		clientDao.init();
//		CandidateDao candDao = new CandidateDao();
//		candDao.init();
		dao.setClientDao(clientDao);
		dao.createMockData();
//		dao.setCandDao(candDao);
//		dao.init();
	}
}
