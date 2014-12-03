package jp.co.worksap.sample.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobDto implements Serializable{
	/**
	 * 
	 */
    public static String PREPARE="not enough";
    public static String FINISHED="enough";
    public static String PLACED="agreed";
    public static String SUBMITTED="submitted";
    public static String REFUSED="refused";
	private static final long serialVersionUID = 1L;
	public static final String IDprefix="JSW";
	private String id;
	private String title;
	private ClientDto client;
	private int amount;
	private String des;
	private Address address=new Address("", 0, 0);
    private Date startDate; //the date of task input
    private Date beginDate;
    private Date endDate;
    private int accepted;
    private int refused;
    private int noResponse;
    List<CandidateDto> candidates = new ArrayList<CandidateDto>();
    private double salaries;
    private int maxHour=10;
    private int newAccept=0;
    private int newRefuse=0;
    private String status=PREPARE;
    private double quotation;
    private String tagsStr;
    private String tagRequirement;
    private String ignoredTag="";
    public int getNumber(){
    	return noResponse+accepted+refused;
    }
    public int getNewAccept() {
		return newAccept;
	}
	public void setNewAccept(int newAccept) {
		this.newAccept = newAccept;
	}
	public int getNewRefuse() {
		return newRefuse;
	}
	public void setNewRefuse(int newRefuse) {
		this.newRefuse = newRefuse;
	}
	public JobDto() {
    	
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ClientDto getClient() {
		return client;
	}
	public void setClient(ClientDto client) {
		this.client = client;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public int getAccepted() {
		return accepted;
	}
	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}
	public int getRefused() {
		return refused;
	}
	public void setRefused(int refused) {
		this.refused = refused;
	}
	public int getNoResponse() {
		return noResponse;
	}
	public void setNoResponse(int noResponse) {
		this.noResponse = noResponse;
	}
	public List<CandidateDto> getCandidates() {
		return candidates;
	}
	public void setCandidates(List<CandidateDto> list) {
		this.candidates = (List<CandidateDto>) list;
	}
	public String getAcceptedLabel(){
		if(newAccept>0){
			return getAccepted() + "<label class='label label-info' style='float:right'> &nbsp;" + this.newAccept+"&nbsp; </label>";
		}
		return getAccepted()+"";
	}
	public String getRefusedLabel(){
		if(newRefuse>0){
			return getRefused()+  "<label class='label label-warning' style='float:right'>&nbsp;"+this.newRefuse+"&nbsp; </label>";
		}
		return getRefused()+"";
	}
	public String getSummaryInfo(){
		return this.getId()+'/'+this.getTitle()+'/' + this.getClient().getName();
	}
    public String getJobStatusForClientPortal(){
    	if(status.equals(PLACED)){
    		return "<div class='alert alert-success'>You have agreed with the job sucessfully</div>";
    	}else if(status.equals(REFUSED)){
    		return "<div class='alert alert-danger'>You have already refused the job </div>";
    	}else{
    		return "";
    	}
    }
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getSalaries() {
		return salaries;
	}
	public void setSalaries(double salaries) {
		this.salaries = salaries;
	}
	public int getMaxHour() {
		return maxHour;
	}
	public void setMaxHour(int maxHour) {
		this.maxHour = maxHour;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getProgress(){
		return accepted*100/amount;
	}
	public String getRefusedString(){
		return refused+"";
	}
    public String getNoResponseString(){
    	return noResponse+"";
    }
    public String getAmountString(){
    	return amount+"";
    }
    public String getProgressString(){
    	return "("+accepted+"/"+amount+")";
    }
	public String getProgressLabel(){
		if(accepted==-1){ // job dto header
			return "progress";
		}
//		String subProgress = "("+accepted+"/"+amount+")";
		String subProgress = "("+accepted+")";
		if(accepted == amount){
			subProgress="";
		}
		int width  = (getProgress()>0 && getProgress()<15)?15:getProgress();
		return "<div class='progress' style='min-width:150px;margin:0px'>"+
  "<div class='progress-bar' role='progressbar' aria-valuenow='"+getProgress()+"' aria-valuemin='0' aria-valuemax='100' style='width:"+width+"%'>"+
    getProgress()+"%"+ subProgress+
  "</div></div>";
	}
    public String getStatusLabel(){
    	if(status.equals(PREPARE)){
    		return "<span class='label label-warning my-status'>"+status+"</span>";
    	}
    	else if(status.equals(SUBMITTED)){
    		return "<span class='label label-info my-status' >"+status+"</span>";
    	}
    	else if(status.equals(PLACED)){
    		return "<span class='label label-success my-status' >"+status+"</span>";
    	}
    	else if(status.equals(REFUSED)){
    		return "<span class='label label-danger my-status' >"+status+"</span>";
    	}else {
    		return "<span class='label label-primary my-status' >"+status+"</span>";
    	}
    	
    	
    }
	public double getQuotation() {
		return quotation;
	}
	public void setQuotation(double quotation) {
		this.quotation = quotation;
	}
	public String getTagsStr() {
		return tagsStr;
	}
	public void setTagsStr(String tagsStr) {
		this.tagsStr = tagsStr;
	}
	public String getTagRequirement() {
		return tagRequirement;
	}
	public void setTagRequirement(String tagRequirement) {
		this.tagRequirement = tagRequirement;
	}
	public String getIgnoredTag() {
		return ignoredTag;
	}
	public void setIgnoredTag(String ignoredTag) {
		this.ignoredTag = ignoredTag;
	}
    

}
