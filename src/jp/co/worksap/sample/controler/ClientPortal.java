package jp.co.worksap.sample.controler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;

import jp.co.worksap.sample.dao.CandidateDao;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.ClientDto;
import jp.co.worksap.sample.dto.JobDto;
@ManagedBean(name="clientPortal")
@ViewScoped
public class ClientPortal  implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -3618040266404160556L;
@ManagedProperty("#{jobDao}")
private JobDao jobDao;
@ManagedProperty("#{candidateDao}")
private CandidateDao candDao;
private String editable;
private ArrayList<JobDto>jobs;
private CandidateDto selectedCand;
private String msg;
private ClientDto client;
private String firstId;
private JobDto selectedJob;
@PostConstruct
public void init(){
	 ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
	 String id = (String)((HttpSession)ec.getSession(false)).getAttribute("id");
	 jobs = new ArrayList<JobDto>();
	 if(id!=null){
	 ArrayList<JobDto>temps = jobDao.getJobByClientId(id);
	 for(JobDto job:temps){
		 if(job.getStatus().equals(JobDto.SUBMITTED)|| job.getStatus().equals(JobDto.REFUSED)||job.getStatus().equals(JobDto.PLACED)){
			 jobs.add(job);
		 }
	 }
     if(jobs.size()==0){
			try {
			ec.redirect("noJobErrorPage.jsf");
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//		 if(jobs.get(0).getStatus().equals(JobDto.PLACED)){
//			 msg="<div id='msg' class='alert alert-success' style='display:none'>you have already agreed with the employment</div>";
//			 editable="false";
//		 }else if(jobs.get(0).getStatus().equals(JobDto.REFUSED)){
//			 msg="<div id='msg' class='alert alert-danger' style='display:none'>you have already refused the employment</div>";
//			 editable="true";
//		 }
//		 else{
//			 msg="";
//			 editable="true";
//		 }
	client = jobs.get(0).getClient();
	setFirstId(jobs.get(0).getId());
	 
	}
	 
}

public void onRowToggle(ToggleEvent event){
	selectedJob = ((JobDto) event.getData());
}
public void updateMsg(){
     	
}
public void agree(){
	this.editable="false";
	
		selectedJob.setStatus(JobDto.PLACED);
		for(CandidateDto cand : selectedJob.getCandidates()){
//			if(cand.getJobStatus().equals("accepted")){
//				cand.setEmployer(selectedJob.getClient().getName());
//				cand.setStatus("placed");
//			}else{
//				cand.setStatus("available");
//			}
			cand.setStatus("placed");
			candDao.update(cand);
		}
	
	jobDao.editJob(selectedJob);
	message(FacesMessage.SEVERITY_INFO, "You have agreed with the job plan for "+selectedJob.getTitle()+" sucessfully");
}
public void refuse(){

	selectedJob.setStatus(JobDto.REFUSED);
	jobDao.editJob(selectedJob);
	message(FacesMessage.SEVERITY_WARN, "You have already refused the job plan for "+selectedJob.getTitle());
}

private void message(FacesMessage.Severity type,String content){
	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, "message",content));
}
public String getEditable() {
	return editable;
}
public void setEditable(String editable) {
	this.editable = editable;
}
public ArrayList<JobDto> getJobs() {
	return jobs;
}
public void setJobs(ArrayList<JobDto> jobs) {
	this.jobs = jobs;
}
public JobDao getJobDao() {
	return jobDao;
}
public void setJobDao(JobDao jobDao) {
	this.jobDao = jobDao;
}
public CandidateDto getSelectedCand() {
	return selectedCand;
}
public void setSelectedCand(CandidateDto selectedCand) {
	this.selectedCand = selectedCand;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public CandidateDao getCandDao() {
	return candDao;
}
public void setCandDao(CandidateDao candDao) {
	this.candDao = candDao;
}
public ClientDto getClient() {
	return client;
}
public void setClient(ClientDto client) {
	this.client = client;
}
public String getFirstId() {
	return firstId;
}
public void setFirstId(String firstId) {
	this.firstId = firstId;
}
public JobDto getSelectedJob() {
	return selectedJob;
}
public void setSelectedJob(JobDto selectedJob) {
	this.selectedJob = selectedJob;
}


}
