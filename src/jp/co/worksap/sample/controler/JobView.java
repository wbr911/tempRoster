////////////////////////////////////////////////////////////////////
//							_ooOoo_								  //
//						   o8888888o							  //	
//						   88" . "88							  //	
//						   (| ^_^ |)							  //	
//						   O\  =  /O							  //
//						____/`---'\____							  //						
//					  .'  \\|     |//  `.						  //
//					 /  \\|||  :  |||//  \						  //	
//				    /  _||||| -:- |||||-  \						  //
//				    |   | \\\  -  /// |   |						  //
//					| \_|  ''\---/''  |   |						  //		
//					\  .-\__  `-`  ___/-. /						  //		
//				  ___`. .'  /--.--\  `. . ___					  //	
//				."" '<  `.___\_<|>_/___.'  >'"".				  //
//			  | | :  `- \`.;`\ _ /`;.`/ - ` : | |				  //	
//			  \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//		========`-.____`-.___\_____/___.-`____.-'========		  //	
//				             `=---='                              //
//		^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//                       Buddha bless me                          //
//                  No bugs and changes forever                   //
////////////////////////////////////////////////////////////////////
package jp.co.worksap.sample.controler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;

import jp.co.worksap.sample.dao.CandidateDao;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dao.KeyWordDao;
import jp.co.worksap.sample.dao.SearchRecordDao;
import jp.co.worksap.sample.dao.TitleDao;
import jp.co.worksap.sample.dto.Address;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.JobDto;
import jp.co.worksap.sample.spec.service.EmailService;

@ManagedBean(name="jobView")
@ViewScoped
public class JobView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3784945978639493780L;
	/**
	 * 
	 */
	@ManagedProperty("#{candidateView}")
	private CandidateView candView;
	@ManagedProperty("#{titleDao}")
	private TitleDao titleDao;
	@ManagedProperty("#{candidateDao}")
	private CandidateDao candDao;
    @ManagedProperty("#{emailService}")
    private EmailService emailService;
	@ManagedProperty("#{jobDao}")
    private JobDao jobDao;
	private ArrayList<JobDto> jobList;
	private ArrayList<JobDto> filteredJobList;
	//private String testDes;
	private JobDto newJob;
	private ArrayList<JobDto> selectedJobList;
	@ManagedProperty("#{keyWordDao}")
	private KeyWordDao keyWordDao;
	private String searchTags="";
	private String isDesChanged;
	//-------------------------------------------candidate view------------------------------
	private JobDto selectJob;
	private String submitMsg="";
	private static final String DEFAULT_CLIENT_EMAIL="yamadata2397@gmail.com"; 
	
	//---------------------------------------end of candidate view -------------------------
	@PostConstruct
	private void init(){
		newJob = new JobDto();
		jobList = jobDao.getAllJob();
		//testDes = jobList.get(0).getDes();
	}
	public void editJob(){
		titleDao.updateTitle(selectJob.getTitle());
		if(isDesChanged.equals("true")){
		selectJob.setTagRequirement(createTagRequirement(selectJob.getDes() , selectJob.getTagsStr()));
		isDesChanged = "false";
		}
		jobDao.editJob(selectJob);
	}
	public void editTagRequirement(){
		selectJob.setTagsStr(searchTags);
		jobDao.editTagRequirement(selectJob);
	}
	public void searchFromJob(){
		candView.setSelectJob(this.selectJob);
		if(selectJob.getTagsStr()!=null &&selectJob.getTagsStr().equals(searchTags)){
			return ;
		}else{
			selectJob.setTagsStr(searchTags);
			jobDao.editTagRequirement(selectJob);
		}
	}
	public void onTitleChanged(){
		
		if(newJob.getId()!=null){
		
		newJob.setQuotation(titleDao.getByTitle(newJob.getTitle()).quotation);
		newJob.setSalaries(titleDao.getByTitle(newJob.getTitle()).salaries);
		
		}else{
	    
		 selectJob.setQuotation(titleDao.getByTitle(selectJob.getTitle()).quotation);
		 selectJob.setSalaries(titleDao.getByTitle(selectJob.getTitle()).salaries);
		}
	}
	public void onClientChanged(){
		if(newJob.getId()!=null){
		newJob.setAddress(newJob.getClient().getAddress());
		}else{
		 selectJob.setAddress(selectJob.getClient().getAddress());
		
		}
	}
	public void submit(){
		if(submitMsg.contains("wrong")){
			return;
		}
		ArrayList<CandidateDto> deleteList = new ArrayList<CandidateDto>();
		for(JobDto job:selectedJobList){
			for(CandidateDto cand:job.getCandidates()){
				if(!cand.getJobStatus().equals("accepted")){
					cand.setStatus("available");
					candDao.update(cand);
					deleteList.add(cand);
				}
			}
			job.getCandidates().removeAll(deleteList);
			job.setStatus(JobDto.SUBMITTED);
			jobDao.editJob(job);
			emailService.sendEmail(DEFAULT_CLIENT_EMAIL, "sure to accept the employment plan",
					"hi," + job.getClient().getContactName()+"<br/>" +
				    "we have already found enough qualified candidates for you,you can see the details of the candidates and approve the employment plan by the link:" +
				    "<br/>" +
				    "http://localhost:9090"+FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/client/clientPortal?id="+job.getClient().getEmail()+"&pwd="+job.getClient().getPhone());
		}
		
		
	}
	public void submitCheck(){
		boolean flag=false;
		boolean zeroflag=false;
		if(selectedJobList.size()==0){
			submitMsg="<div class='alert alert-danger'><strong>wrong! </strong>no jobs are selected</div>";
			return;
		}
		for(JobDto job:selectedJobList){
			if(job.getStatus().equals(JobDto.PLACED)){
				submitMsg = "<div class='alert alert-danger'><strong>wrong! </strong>the job are already agreed</div>";
				return;
			}
			if(job.getAccepted()==0){
				zeroflag=true;
				break;
			}
			if(job.getStatus().equals(JobDto.PREPARE)){
				flag=true;
				break;
			}
		}
		if(zeroflag){
			submitMsg = "<div class='alert alert-danger'><strong>wrong! </strong>there are jobs that have 0 candidates</div>";
			return;
		}
		if(flag){
			submitMsg = "<div class='alert alert-warning'><strong>warning! </strong>there are jobs that don't have enough candidates</div>";
		}else{
			submitMsg="";
		}
	}
    public void onRowSelect(SelectEvent event){
		
		selectJob = ((JobDto) event.getObject());
	}
	public  void test(String content){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(content));
	}
	private void message(FacesMessage.Severity type,String content){
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, "message",content));
    }
	 public int sortId(Object var1, Object var2){
		 String s1 = ((String)var1).replaceAll("("+CandidateDto.IDprefix+"|"+JobDto.IDprefix+")", "");
		 String s2 = ((String)var2).replaceAll("("+CandidateDto.IDprefix+"|"+JobDto.IDprefix+")", "");
	    	return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
	 }
	 
	 public void clearNewTag(ToggleEvent event){
		 JobDto job = (JobDto) event.getData();
		 job.setNewAccept(0);
		 job.setNewRefuse(0);
	 }
	 public void initNewJob(){
		 newJob = new JobDto();
		 newJob.setId(jobDao.newId()+"");
	 }
	public void addJob(){
		 Calendar c = Calendar.getInstance();
		 newJob.setStartDate(c.getTime());
		 newJob.setTagRequirement(createTagRequirement(newJob.getDes(),null));
		 jobDao.addJob(newJob);
		 titleDao.updateTitle(newJob.getTitle());
		 jobList.add(newJob);
		 cancelAddJob();
	}
	public void deleteJob(){
		if(selectedJobList.size()==0){
			message(FacesMessage.SEVERITY_WARN,"no jobs are selected");
			return;
		}
		for(JobDto job:selectedJobList){
			for(CandidateDto cand:job.getCandidates()){
				cand.setStatus("available");
				cand.setJobStatus("");
				candDao.update(cand);
				
			}
		}
		jobDao.deleteJobs(selectedJobList);
		jobList.removeAll(selectedJobList);
		message(FacesMessage.SEVERITY_INFO,"jobs are selected sucessfully");
		
	}
	public void cancelAddJob(){
		newJob =new JobDto();
	}
	public ArrayList<String>getJobStatusList(){
		ArrayList<String>list= new ArrayList<String>();
		list.add(JobDto.PREPARE);
		list.add(JobDto.FINISHED);
		list.add(JobDto.SUBMITTED);
		list.add(JobDto.PLACED);
		list.add(JobDto.REFUSED);
		return list;
	}
	private String regxEscape(String regx){
		regx=regx.replaceAll("\\+", "\\\\+"); 
		regx=regx.replaceAll("\\.", "\\\\.");
		return regx;
	}
	private String createTagRequirement(String req,String referTags){
		String[] tagReferList=null;
		if(referTags!=null){
			 tagReferList= referTags.toLowerCase().split("}");
		}
		String defaultTagType="primary";
		String lowerCaseReq = req.toLowerCase();
		ArrayList<String> tags = keyWordDao.getAllTags();
		Pattern p=null;
		Matcher m = null;
		String tagLabel=null;
		for(String tag:tags){
			p = Pattern.compile("(?<=(^|\\W))"+regxEscape(tag.toLowerCase())+"(?=(\\W|$))");
			m =p.matcher(lowerCaseReq);
			if(m.find()){
			String target=req.substring(m.start(),m.end()); // avoid the upper case or lower case effect
			if(tagReferList!=null){
				String tagType=defaultTagType;                          //parse_requirement_tags()  tagStr +=newTag +","
				if(tagReferList[0].contains(tag.toLowerCase()+",")){
					tagType = "primary";
				}else if(tagReferList[1].contains(tag.toLowerCase()+",")){
					tagType="info";
				}else if(tagReferList[2].contains(tag.toLowerCase()+",")){
				    tagType="danger";
				}else if(tagReferList[3].contains(tag.toLowerCase()+",")){
				    tagType="default";
				}
				 tagLabel= createTag(target, tagType);
			}else{
			   tagLabel= createTag(target, defaultTagType);
			}
			req = req.replaceFirst("(?<=(^|\\W))"+regxEscape(target)+"(?=(\\W|$))",tagLabel );
			lowerCaseReq = lowerCaseReq.replaceFirst(p.pattern(), tagLabel);
			}
		}
		return req;	
	}
	private String createTag(String content, String type){
		String res="<label class='btn btn-"+type+" requirement-label'>" +content+
				//"<a class='glyphicon glyphicon-remove ' style='color: white;' onclick='removeTag(this)'></a>"+
			"</label>";
		return res;
	}
	public JobDao getJobDao() {
		return jobDao;
	}
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}
	
	public ArrayList<JobDto> getJobList() {
		return jobList;
	}

	public void setJobList(ArrayList<JobDto> jobList) {
		this.jobList = jobList;
	}

	public ArrayList<JobDto> getFilteredJobList() {
		return filteredJobList;
	}

	public void setFilteredJobList(ArrayList<JobDto> filteredJobList) {
		this.filteredJobList = filteredJobList;
	}

//	public String getTestDes() {
//		return testDes;
//	}
//	public void setTestDes(String testDes) {
//		this.testDes = testDes;
//	}
//	
	public JobDto getSelectJob() {
		return selectJob;
	}
	public void setSelectJob(JobDto selectJob) {
		this.selectJob = selectJob;
	}

	public JobDto getNewJob() {
		return newJob;
	}

	public void setNewJob(JobDto newJob) {
		this.newJob = newJob;
	}
	public ArrayList<JobDto> getSelectedJobList() {
		return selectedJobList;
	}

	public void setSelectedJobList(ArrayList<JobDto> selectedJobList) {
		this.selectedJobList = selectedJobList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSubmitMsg() {
		return submitMsg;
	}
	public void setSubmitMsg(String submitMsg) {
		this.submitMsg = submitMsg;
	}
	public int selectedJobSize(){
		return selectedJobList.size();
	}
	public EmailService getEmailService() {
		return emailService;
	}
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	public CandidateDao getCandDao() {
		return candDao;
	}
	public void setCandDao(CandidateDao candDao) {
		this.candDao = candDao;
	}
	public TitleDao getTitleDao() {
		return titleDao;
	}
	public void setTitleDao(TitleDao titleDao) {
		this.titleDao = titleDao;
	}
	public KeyWordDao getKeyWordDao() {
		return keyWordDao;
	}
	public void setKeyWordDao(KeyWordDao keyWordDao) {
		this.keyWordDao = keyWordDao;
	}
	 public static void main(String args[]){
		
		JobView j = new JobView();	
         String tag="agile";
		 String req ="Good understanding of project management concepts, agile process background is preferred&nbsp;</font></div><div><";
		 Pattern p = Pattern.compile("\\W"+j.regxEscape(tag.toLowerCase())+"\\W");
		 Matcher m = p.matcher(req);
		 if(m.find()){
				String target=req.substring(m.start(),m.end()); // avoid the upper case or lower case effect
		 
		 req =req.replaceFirst("(^|\\W)"+j.regxEscape(tag.toLowerCase())+"(\\W|$)", "xxxxxxxxxx");
		 System.out.println(p.pattern());
		 System.out.println(req);
		 }	
	 }
	public String getSearchTags() {
		return searchTags;
	}
	public void setSearchTags(String searchTags) {
		this.searchTags = searchTags;
	}
	public CandidateView getCandView() {
		return candView;
	}
	public void setCandView(CandidateView candView) {
		this.candView = candView;
	}
	public String getIsDesChanged() {
		return isDesChanged;
	}
	public void setIsDesChanged(String isDesChanged) {
		this.isDesChanged = isDesChanged;
	}
	
}
