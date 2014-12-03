package jp.co.worksap.sample.controler;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import jp.co.worksap.sample.dao.CandidateDao;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.JobDto;

@ManagedBean(name="candPortal")
@ViewScoped
public class CandidatePortal implements Serializable{
     /**
	 * 
	 */
	private static final long serialVersionUID = 8428363914290013068L;
	private JobDto job;
     private CandidateDto cand;
     @ManagedProperty("#{candidateDao}")
     private CandidateDao candDao;
     @ManagedProperty("#{jobDao}")
     private JobDao jobDao;
     private String editable="true";
     @PostConstruct
     public void init(){
    	 ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
    	 String id = (String)((HttpSession)ec.getSession(false)).getAttribute("id");
    	 if(id!=null){
			 cand = candDao.getById(id);
		     job = jobDao.getById(cand.getJobId());
		     
		     if(job==null){
				 try {
						ec.redirect("noJobErrorPage.jsf");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;		
			 }
    	 }
	 
     }
    
    public void accept(){
    	String res = jobDao.updateJobFromUserPortal(job.getId(), cand, "accepted");
    	if(res.equals("success")){
    		updateMsg();
    	}else{
    		message(FacesMessage.SEVERITY_ERROR, res);
    	}
    }
    public void refuse(){
    	String res = jobDao.updateJobFromUserPortal(job.getId(), cand, "refused");
    	if(res.equals("success")){
    		updateMsg();
    		
    	}else{
    		message(FacesMessage.SEVERITY_ERROR, res);
    	}
    	
    }
    private void message(FacesMessage.Severity type,String content){
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, "message",content));
    }
    public void updateMsg(){
    	if(cand.getJobStatus().equals("accepted")){
    		message(FacesMessage.SEVERITY_INFO, "you have accepted the job successfully");
    		editable="false";
    	}else if(cand.getJobStatus().equals("refused")){
    		editable="false";
    		message(FacesMessage.SEVERITY_INFO, "you have refused the job successfully");
    	}else{
    		message(FacesMessage.SEVERITY_INFO, "welcome,please make your choice at the bottom");
    	}
    }
	public JobDto getJob() {
		return job;
	}
	public void setJob(JobDto job) {
		this.job = job;
	}
	public CandidateDto getCand() {
		return cand;
	}
	public void setCand(CandidateDto cand) {
		this.cand = cand;
	}
	public CandidateDao getCandDao() {
		return candDao;
	}
	public void setCandDao(CandidateDao candDao) {
		this.candDao = candDao;
	}
	public JobDao getJobDao() {
		return jobDao;
	}
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	
     
     
}
