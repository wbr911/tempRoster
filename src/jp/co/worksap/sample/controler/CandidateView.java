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
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import jp.co.worksap.sample.dao.CandidateDao;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dao.KeyWordDao;
import jp.co.worksap.sample.dao.SearchRecordDao;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.JobDto;
import jp.co.worksap.sample.dto.JobSelectHeader;
import jp.co.worksap.sample.dto.Note;
import jp.co.worksap.sample.dto.SearchRecordDto;
import jp.co.worksap.sample.dto.User;
import jp.co.worksap.sample.spec.service.AdvancedSearchService;
import jp.co.worksap.sample.spec.service.CandPictureService;
import jp.co.worksap.sample.spec.service.EmailService;
import jp.co.worksap.sample.spec.service.PDFExtract;
import jp.co.worksap.sample.spec.service.SMS_Service;
import jp.co.worksap.sample.spec.service.WordExtract;
import jp.co.worksap.sample.spec.utils.TagsParser;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@ManagedBean(name="candidateView")
@ViewScoped
public class CandidateView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8239000308183529621L;

	/**
	 * 
	 */
	@ManagedProperty("#{applicationView}")
	private Application app;

	private int testVar=1;
	private String autoCompleteVar;
	@ManagedProperty("#{candidateDao}")
	private CandidateDao candDao;
	@ManagedProperty("#{advancedSearch}")
	private AdvancedSearchService advancedSearch;
	@ManagedProperty("#{emailService}")
	private EmailService emailService;
	@ManagedProperty("#{smsService}")
	private SMS_Service smsService;
    @ManagedProperty("#{candPicService}")
    private CandPictureService candPicService;
	private User user;
	//---------------------------header bar------------------------
	private int newAccept;
	private int newRefuse;
	//-----------------------------end of header bar---------------
	//---------------------------creation dialog-------------------
	private String id;
	private CandidateDto newCand;
	//----------------------------end of creation dialog------------
	//-------------------search tool bar-------------------------
	private String shape;
	private String distance;
	private String address;
	private ArrayList<String>searchRecords;
	private String searchTags="";
	@ManagedProperty("#{keyWordDao}")
	private KeyWordDao keyWordDao;
	@ManagedProperty("#{searchRecordDao}")
	private SearchRecordDao searchDao;
	private ArrayList<SearchRecordDto>favouriteList;
	private ArrayList<SearchRecordDto>recentSeachList;
	private SearchRecordDto newFavourite;
	private SearchRecordDto selectedFavourite;
	private SearchRecordDto selectedRecent;
	private String idFilter;
	private String nameFilter;
	private String jobTitleFilter;
	private Integer ageFilter;
	private String genderFilter;
	private String educationFilter;
	private String addressFilter;
	private String phoneFilter;
	private String emailFilter;
	private String statusFilter;
	private String lastNoteFilter;
	private int searchRecordIdCount;
	private String searchLabel;
	//---------------------end of search tool bar-----------------
	
	//----------------------detail view---------------------------
	@ManagedProperty("#{jobDao}")
	private JobDao jobDao;
	@ManagedProperty("#{wordExtract}")
	private WordExtract word;
	@ManagedProperty("#{PDFExtract}")
	private PDFExtract pdf;
	private Note selectedNote;
	private Note newNote;
	private String candTags;
	private String[] selectMessageTypes;
	private JobSelectHeader jobHeader = new JobSelectHeader();
	private JobDto selectJob;
	private ArrayList<JobDto>jobList;
	private CandidateDto selectedRow;
	private UploadedFile pic;
	private CandidateDto parsedCand;
	private boolean use_parsed_title = true;
	private boolean use_parsed_age = true;
	private boolean use_parsed_email = true;
	private boolean use_parsed_phone = true;
	private boolean use_parsed_education = true;
	private boolean use_parsed_tags = true;
	private boolean use_parsed_name = true;
	private boolean use_parsed_gender = true;
	private boolean use_parsed_pic = true;
	private String parsedCandTags;
	
	//---------------------end of detail view---------------------
	private List<CandidateDto> candidates;
	private List<CandidateDto> selectedCandList;
	private List<CandidateDto> candidates_temp;
	private CandidateDto selectedCand;
	private List<CandidateDto> filteredCandidates;
	private List<String> tags=new ArrayList<String>(Arrays.asList(new String[]{"java" ,"js", "CCNA" ,"web service" ,"android","MFC","c++"}));
	private ArrayList<String> educationList;
	
	public List<String> completeAddress(String query){
		return tags;
	}
	@PostConstruct
	public void init(){
		candidates = candDao.getAllCandidates();
		candidates_temp = candidates;
		filteredCandidates = new ArrayList<CandidateDto>();
		searchRecords = new ArrayList<String>(Arrays.asList(new String[]{"searhc1 , seach2"}));
		newNote = new Note();
		user = (User) ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("user");
		if(user==null){
			return;
		}
		jobList = jobDao.getAllJob();
		selectMessageTypes = new String[]{"email"};
		educationList = CandidateDto.getEducationList();
	    parsedCand = new CandidateDto();
       // parsedCand.setPic("defaultPic.png");	 
        favouriteList = searchDao.getFavouriteListByUserId(user.getId());
        recentSeachList = searchDao.getRecentListByUserId(user.getId());
        searchRecordIdCount = searchDao.createNewId();
        newFavourite = initFavourite();
          
	}
	public ArrayList<JobDto>getAvailableJobList(){
		ArrayList<JobDto>res = new ArrayList<JobDto>();
		for(JobDto job:jobList){
			if(job.getAccepted()!=job.getAmount()){
				res.add(job);
			}
		}
		return res;
	}
	public ArrayList<JobDto>getDisabledJobList(){
		ArrayList<JobDto>res = new ArrayList<JobDto>();
		for(JobDto job:jobList){
			if(job.getAccepted()==job.getAmount()){
				res.add(job);
			}
		}
		return res;
	}
	public void useParsedCand(){
		if(use_parsed_name){
			selectedCand.setName(parsedCand.getName());
		}
		if(use_parsed_age){
			selectedCand.setAge(parsedCand.getAge());
		}
		if(use_parsed_education){
			selectedCand.setEducation(parsedCand.getEducation());
		}
		if(use_parsed_email){
			selectedCand.setEmail(parsedCand.getEmail());
		}
		if(use_parsed_phone){
			selectedCand.setPhone(parsedCand.getPhone());
		}
		if(use_parsed_title){
			selectedCand.setTitle(parsedCand.getTitle());
		}
		if(use_parsed_gender){
			selectedCand.setGender(parsedCand.getGender());
		}
		if(use_parsed_pic){
			selectedCand.setPic(parsedCand.getPic());
		}
		if(use_parsed_tags){
			_editTags(parsedCandTags,selectedCand);
		}
		selectedCand.setResume(parsedCand.getResume().replaceAll("\\n", "<br/>"));
		candDao.update(selectedCand);
		
	}
	public void uploadCV(FileUploadEvent e){
		String suffix =CandPictureService.getSuffix(e.getFile().getFileName());
		parsedCand = new CandidateDto();
		
		try {
			if(suffix.equals("pdf")){
				pdf.parsePDF(e.getFile().getInputstream(), candPicService.getImgPath(selectedCand.getId()), parsedCand);
			}else if("doc docx".contains(suffix)){
				word.parseWord(suffix, e.getFile().getInputstream(), candPicService.getImgPath(selectedCand.getId()),parsedCand);
			}
			else{
				message(FacesMessage.SEVERITY_ERROR, "wrong file format(only support .doc .docx and .pdf)");
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
	}
	public void uploadPic(FileUploadEvent e){
		selectedCand.setPic(candPicService.addPic(selectedCand.getId(),e.getFile()));
		candDao.update(selectedCand);
		test("pic");
	}
	public List<String> completeTag(String query){
		List<String> res = new ArrayList<String>();
		for(String str : keyWordDao.getAllTags()){
			if(str.toLowerCase().startsWith(query.toLowerCase())){
				res.add(str);
			}
		}
		return res ;
		//return tags;
	}
	
	public void onRowSelect(SelectEvent event){
		
		selectedCand = ((CandidateDto) event.getObject());
	}
	
	public void initNewCand(){
		selectedCand = new CandidateDto();
		selectedCand.setId(candDao.newId());
		candidates.add(selectedCand);
		candDao.add(selectedCand);
	}
	public void deleteNewCand(){
		candidates.remove(selectedCand);
		candDao.delete(selectedCand);	
	}
	public void deleteBatch(){
		candidates.removeAll(selectedCandList);
		candDao.deleteBatch(selectedCandList);	
	}
	public void edit(){
		selectedCand.setResume(selectedCand.getResume().replaceAll("\\n", "<br/>"));	
		candDao.update(selectedCand);
	}
	public String getAutoCompleteVar() {
		return autoCompleteVar;
	}
	public void setAutoCompleteVar(String autoCompleteVar) {
		this.autoCompleteVar = autoCompleteVar;
	}
	private void message(FacesMessage.Severity type,String content){
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, "message",content));
    }
	public void test(String content){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(content));
		autoCompleteVar ="test";
	}
	public void test(){
		test("test");
	}
	public void autoCompleteChange(SelectEvent e){
		AutoComplete auto = (AutoComplete) FacesContext.getCurrentInstance().getViewRoot().findComponent("requiredVar");
		auto.setVar("");
		
	}
	 public int sortId(Object var1, Object var2){
		 String s1 = ((String)var1).replaceAll("("+CandidateDto.IDprefix+"|"+JobDto.IDprefix+")", "");
		 String s2 = ((String)var2).replaceAll("("+CandidateDto.IDprefix+"|"+JobDto.IDprefix+")", "");
	    	return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
	 }
	 public boolean filterEducation(Object value, Object filter, Locale locale){
		 if(getEducationLevel((String)filter)<= getEducationLevel((String)value)){
			 return true;
		 }
		 return false;
	 }
	 public int sortEducation(Object var1, Object var2){
		 return getEducationLevel((String)var1)-getEducationLevel((String)var2);
	 }
	 private int getEducationLevel(String education){
		 for(int i = 0 ; i< educationList.size();i++){
			 if(educationList.get(i).equals(education)){
				 return i;
			 }
		 }
		 return -1;
	 }
	 
	 public void deleteNote(){
		 String id =(String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deleteId");
		 for(Note note:selectedCand.getNoteList()){
			 if(note.getId().equals(id)){
				 candDao.deleteNote(note);
				 selectedCand.getNoteList().remove(note);
				 break;
			 }
		 }
		
		 test(id);
	 }
	 public void addNote(){
		 String content = newNote.getContent();
		 content = content.replace("\r\n", "<br>");
		
		 content = content.replace(" ", "&nbsp");
		 newNote.setContent(content);
		 Date now = new Date(); 
		 SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");//
		 String date = dateFormat.format( now ); 
		 newNote.setDate(date);
		 newNote.setConsultant(user.getName());
		 candDao.addNote(selectedCand.getId(),newNote );
		 selectedCand.getNoteList().add(0,newNote);
		 selectedCand.setLastNote(date);
		 newNote = new Note();
		
	 }
	 public void editTags(){
		_editTags(candTags,selectedCand);	
		 keyWordDao.updateNewTags(selectedCand);
		candDao.updateTag(selectedCand);	
		}
	private void _editTags(String tagStr,CandidateDto dto){
		ArrayList<String>[] res = TagsParser.parseTag(tagStr);
		dto.setSkills(res[0]);
		dto.setCertificates(res[1]);
		dto.setKeywords((res[2]));
		dto.setTagsStr(TagsParser.createTagsStr(dto));
			
	}
	public void advancedSearch(){
		candidates = candDao.getAllCandidates();
		candidates= (ArrayList<CandidateDto>) advancedSearch.search(candidates, searchTags, address);
		searchLabel = SearchRecordDto.createDes(searchTags);
		SearchRecordDto recent = initNewRecentSearch();
		recentSeachList.add(0,recent);
		// store 10 recent search record at most
		if(recentSeachList.size()>=11){
			int last =recentSeachList.size()-1;
			int deleteId = recentSeachList.get(last).getId();
			searchDao.deleteSearchDto(deleteId);
			recentSeachList.remove(last);
		}
		recent.setId(this.searchRecordIdCount++);
		searchDao.addSearchDto(recent);
	}
	private SearchRecordDto initNewRecentSearch(){
		SearchRecordDto res = new SearchRecordDto();
		res.setType(SearchRecordDto.RECENT);
		res.setName(new SimpleDateFormat("MM/dd/yyyy-HH:mm").format(new Date()));
		res.setTags(searchTags);
		res.setUserId(user.getId());
		return res;
	}
	public void clear(){
		candidates = candDao.getAllCandidates();
		searchTags ="";
	}
	public void refresh(){
		candidates = candDao.refresh();
		if(!searchTags.equals("")){
			candidates = (ArrayList<CandidateDto>) advancedSearch.search(candDao.getAllCandidates(), searchTags, address);
		}
	}
	public String logout(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)externalContext.getSession(false);
		if(session != null){
			session.invalidate();
		}
		return "/user/userLogin.jsf?faces-redirect=true";
	}
	public void setNewId(){
		
	}
	public void addtoJob(String status){
		selectedCand.setChosenBy(user.getName());
		String res = jobDao.updateJob(selectJob.getId(), selectedCand,status);
	
		if(!res.equals("success")){
			message(FacesMessage.SEVERITY_ERROR, res);
			return;
		}
		if(status.equals("no response")){
			sendMsg();
		}
		
		
	}
	public void sendMsg(){
		for(int i = 0 ; i < selectMessageTypes.length;i++){
			if(selectMessageTypes[i].equals("sms")){
				smsService.send(selectedCand.getPhone(), createSMSStr());
			}else if(selectMessageTypes[i].equals("email")){
				emailService.sendEmail(selectedCand.getEmail(), "new job for your", createEmailStr());
			}
		}
	}
	
	public void updateHeader(){
		this.newAccept = jobDao.getNewAccept();
		this.newRefuse = jobDao.getNewRefuse();
	}
	public void clearHeader(){
		jobDao.setNewAccept(0);
		jobDao.setNewRefuse(0);
		this.newAccept = jobDao.getNewAccept();
		this.newRefuse = jobDao.getNewRefuse();
	}
	public void addFavourite(){
		String filterStr = "";
	    filterStr = addFilter2Str(idFilter, filterStr);
	    filterStr = addFilter2Str(nameFilter, filterStr);
	    filterStr = addFilter2Str(ageFilter, filterStr);
	    filterStr = addFilter2Str(genderFilter, filterStr);
	    filterStr = addFilter2Str(educationFilter, filterStr);
	    filterStr = addFilter2Str(addressFilter, filterStr);
	    filterStr = addFilter2Str(phoneFilter, filterStr);
	    filterStr = addFilter2Str(emailFilter, filterStr);
	    filterStr = addFilter2Str(statusFilter, filterStr);
	    filterStr = addFilter2Str(lastNoteFilter, filterStr);
	    newFavourite.setFilters(filterStr);
	    newFavourite.setTags(searchTags);
		searchDao.addSearchDto(newFavourite);
		favouriteList.add(newFavourite);
		newFavourite = initFavourite();
	}
	private void parseStr2Filter(String str){
		String[] list =(str+"end").split("/");
		int i = 0;
		this.idFilter = list[i++];
		this.nameFilter = list[i++];
		this.ageFilter =  list[i++].equals("")?null:Integer.valueOf(list[2]);
		this.genderFilter = list[i++];
		this.educationFilter = list[i++];
		this.addressFilter = list[i++];
		this.phoneFilter = list[i++];
		this.emailFilter  =list[i++];
		this.statusFilter = list[i++];
		this.lastNoteFilter = list[i++];	
	}
	public void chooseFavourite(){
		searchTags = selectedFavourite.getTags();
	    advancedSearch();
	    if(selectedFavourite.getFilters()!=null){
	    	String[]filters = selectedFavourite.getFilters().split("/");
	    	if(filters.length!=0){
	    		searchLabel+="<div class='left'>[Filters]:<span style='font-weight:normal'>";
	    		for(String str :filters){
	    			if(!str.equals("")){
	    			searchLabel += str+"/";
	    			}
	    		}
	    		searchLabel+="</span></div>";
	    	}
	    }
	    parseStr2Filter(selectedFavourite.getFilters());
	}
	public void chooseRecent(){
		searchTags = selectedRecent.getTags();
		advancedSearch();
	}

	private String addFilter2Str(Object o,String str){
		if(o!=null){
			str+=(String)o ;
		}
		return str+="/";
		
	}
	private SearchRecordDto initFavourite(){
		SearchRecordDto search = new SearchRecordDto();
		search.setId(searchRecordIdCount++);
		search.setType(SearchRecordDto.FVOURITE);
		search.setUserId(user.getId());
		return search;
	}
	public void deleteFavourite(){
		//selectedFavourite = getSearchById(Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deleteRecordId")));
		favouriteList.remove(selectedFavourite);
		searchDao.deleteSearchDto(selectedFavourite.getId());
	}
	public int sortDate(Object var1, Object var2){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date1=null;
		Date date2= null;
		try {
			Date noNote = sdf.parse("01/01/1900");
			if(((String)var1).equals(CandidateDto.LAST_NOTE_DEFAULT)){
				date1 = noNote;
			}else{
				date1 = sdf.parse((String)var1);
			}
			if(((String)var2).equals(CandidateDto.LAST_NOTE_DEFAULT)){
				date2 = noNote;
			}else{
				date2 = sdf.parse((String)var2);
			}
			return date1.compareTo(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	private String createSMSStr(){
		String res = "";
		res += "new job"
			   +"you can get more details and send feedback by the URL: "+ 
				createAccessURL()+
			   "from"+
			   app.getCompanyName();
		return res;
	}
	private String createAccessURL(){
		String context =FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		String res = "";
		res+= "http://localhost:9090"+context+"/cand/candPortal?id="+selectedCand.getId()+"&pwd="+selectedCand.getPhone();
		return res;
	}
	private String createEmailStr(){
		String res="";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		res +="<table cellspacing='0' cellpadding='0' border='0' align='center' style='margin: 0 auto;font: 1em Tahoma,Verdana,Arial; font-size: 1em;'>"
				+"<tbody>"
				  +"<tr>"
				    +"<td>"
				      +"<table width='700'  cellpadding='0' border='0' style='border: 0; padding: 0; border-spacing:0px' >"
				        +"<tbody>"
				          +"<tr >"
				            +"<td  style='background-color:#d9edf7;border-radius:  10px 10px 0px 0px; text-align:center;height:100px;font-size:1.5em'>"
				              +selectJob.getTitle()
				              +"<br/>"
				              +"<span style='margin:10px 0px;display:inline-block;padding:2px 4px;font-size:90%;color:#fff;background-color:#333;border-radius:3px;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.25);box-shadow:inset 0 -1px 0 rgba(0,0,0,.25)'>"
				              		+"offered by</span>"
				              +"<br/>"
				              +selectJob.getClient().getName()
				            +"</td>"
				          +"</tr>"
				        +"</tbody>"
				      +"</table>"
				    +"</td>"
				  +"</tr>"
				  +"<tr>"
				    +"<td>"
				      +"<table class='emailTable' width='700' cellspacing='10' cellpadding='0' border='0' style='border-left: solid 1px #dddddd; padding: 0;border-right: solid 1px #dddddd;'>"
				        +"<tbody>"
				          +"<tr>"
				            +"<td colspan='2' style='padding:10px;'>"
				              +"<span style='margin:10px 0px;display:inline-block;font-size:20px !important;padding:2px 4px;font-size:90%;color:#fff;background-color:#333;border-radius:3px;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.25);box-shadow:inset 0 -1px 0 rgba(0,0,0,.25)'>" +
				              "job requirement</span>"
				              +"<br/>"
				              +selectJob.getDes()
				            +"</td>"
				          +"</tr>"
				          +"<tr>"
				            +"<td colspan='2' style='padding:10px;'>"
				              +"<span style='margin:10px 0px;display:inline-block;font-size:20px !important;padding:2px 4px;font-size:90%;color:#fff;background-color:#333;border-radius:3px;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.25);box-shadow:inset 0 -1px 0 rgba(0,0,0,.25)'>" +
				              "Job conditions</span>"
				            +"</td>"
				          +"</tr>"
				          +"<tr>"
				            +"<td colspan='2' style='padding:10px;'>"
				              +"<table cellspacing='10' style='width: 100%;background-color: whitesmoke;'>"
				                +"<tbody>"
				                  +"<tr>"
				                    +"<td>salaries</td>"
				                    +"<td>"
				                      +selectJob.getSalaries()
				                    +"</td>"
				                  +"</tr>"
				                  +"<tr>"
				                    +"<td>max hour</td>"
				                    +"<td>"
				                      +selectJob.getMaxHour()
				                    +"</td>"
				                  +"</tr>"
				                  +"<tr>"
				                    +"<td>date</td>"
				                    +"<td>"
				                      +"<span style='color:#a94442'>"
				                        +sdf.format(selectJob.getBeginDate())
				                      +"</span>"     
				                      +"to<span style='color:#a94442'>"
				                        +sdf.format(selectJob.getEndDate())
				                      +"</span>"
				                    +"</td>"

				                  +"</tr>"
				                  +"<tr>"
				                    +"<td>location</td>"
				                    +"<td>"
				                      +"<a style='color:#428bca;text-decoration:underline' href='https://www.google.com.hk/maps/place/%E9%95%B7%E5%B3%B0%E4%B8%AD%E5%BF%83/@31.211043,121.43018,17z/data=!4m2!3m1!1s0x0:0x1fd038a28003e600?hl=en_uk'>Change Feng center</a>"
				                    +"</td>"
				                  +"</tr>"

				                +"</tbody>"
				              +"</table>"
				            +"</td>"
				          +"</tr>"
				          +"<tr>"
				            +"<td colspan='2' style='padding:10px;'>"
				              +"<span style='margin:10px 0px;display:inline-block;font-size:20px !important;padding:2px 4px;font-size:90%;color:#fff;background-color:#333;border-radius:3px;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.25);box-shadow:inset 0 -1px 0 rgba(0,0,0,.25)'>company introduction</span>"
				              +"<br/>"
				              +selectJob.getClient().getDes()
                               +"<br/>"
				              +"<span style='margin:10px 0px;display:inline-block;font-size:20px !important;padding:2px 4px;font-size:90%;color:#fff;background-color:#333;border-radius:3px;-webkit-box-shadow:inset 0 -1px 0 rgba(0,0,0,.25);box-shadow:inset 0 -1px 0 rgba(0,0,0,.25)'>your choices</span>"

				            +"</td>"
				          +"</tr>"
				          +"<tr>"
				            +"<td colspan='2' style='padding:10px;'>"      
				              +"click<a href='" +createAccessURL() +"' style='font-size:20px;background-color:#428bca;border-radius:5px;padding:5px 10px; width:100px;display:inline-block;text-align:center;color:whitesmoke'>accep</a>to accept this job</td>"
				          +"</tr>"
				          +"<tr>"
				            +"<td colspan='2' style='padding:10px;'>"
				              +"<br/>"    
				              +"click<a href='" +createAccessURL() +"' style='background-color:#f0ad4e;font-size:20px;border-radius:5px;padding:5px 10px;width:100px;display:inline-block;text-align:center; color:whitesmoke'>refus</a>to refuse this job</td>"
				          +"</tr>"
				        +"</tbody>"
				      +"</table>"
				    +"</td>"
				  +"</tr>"
				  +"<tr>"
				    +"<td>"
				      +"<table width='700' style='border-spacing:0px'>"
				        +"<tbody>"
				          +"<tr>"
				            +"<td colspan='2' style='background-color:#d9edf7;border-radius:0px 0px 15px 15px;text-align:center'>"
				              +"<p> please feel free to contact if you have any problem:"    
				                +"<span class='text-primary'>"
				                  +user.getPhone()
				                +"</span>" +
				                "&nbsp;&nbsp;"+user.getName()
				              +"</p>" 
				              +"<p> send by"        
				                +app.getCompanyName()
				              +"</p>"
				            +"</td>"
				          +"</tr>"
				        +"</tbody>"
				      +"</table>"
				    +"</td>"
				  +"</tr>"
				+"</tbody>"
				+"</table>"
				+"</table>";
		return res;

	}
	public int getTestVar() {
		return testVar;
	}
	public void setTestVar(int testVar) {
		this.testVar = testVar;
	}
	public CandidateDao getCandDao() {
		return candDao;
	}
	public void setCandDao(CandidateDao candDao) {
		this.candDao = candDao;
	}
	public AdvancedSearchService getAdvancedSearch() {
		return advancedSearch;
	}
	public void setAdvancedSearch(AdvancedSearchService advancedSearch) {
		this.advancedSearch = advancedSearch;
	}
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ArrayList<String> getSearchRecords() {
		return searchRecords;
	}
	public void setSearchRecords(ArrayList<String> searchRecords) {
		this.searchRecords = searchRecords;
	}
	public String getSearchTags() {
		return searchTags;
	}
	public void setSearchTags(String searchTags) {
		this.searchTags = searchTags;
	}
	public Note getSelectedNote() {
		return selectedNote;
	}
	public void setSelectedNote(Note selectedNote) {
		this.selectedNote = selectedNote;
	}
	public Note getNewNote() {
		return newNote;
	}
	public void setNewNote(Note newNote) {
		this.newNote = newNote;
	}
	public String getCandTags() {
		return candTags;
	}
	public void setCandTags(String candTags) {
		this.candTags = candTags;
	}
	public List<CandidateDto> getCandidates() {
		return candidates;
	}
	public void setCandidates(List<CandidateDto> candidates) {
		this.candidates = candidates;
	}
	public List<CandidateDto> getCandidates_temp() {
		return candidates_temp;
	}
	public void setCandidates_temp(List<CandidateDto> candidates_temp) {
		this.candidates_temp = candidates_temp;
	}
	public CandidateDto getSelectedCand() {
		return selectedCand;
	}
	public void setSelectedCand(CandidateDto selectedCand) {
		this.selectedCand = selectedCand;
	}
	public List<CandidateDto> getFilteredCandidates() {
		return filteredCandidates;
	}
	public void setFilteredCandidates(List<CandidateDto> filteredCandidates) {
		this.filteredCandidates = filteredCandidates;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String[] getSelectMessageTypes() {
		return selectMessageTypes;
	}
	public void setSelectMessageTypes(String[] selectMessageTypes) {
		this.selectMessageTypes = selectMessageTypes;
	}
	public JobDto getSelectJob() {
		return selectJob;
	}
	public void setSelectJob(JobDto selectJob) {
		this.selectJob = selectJob;
	}
	public ArrayList<JobDto> getJobList() {
		return jobList;
	}
	public void setJobList(ArrayList<JobDto> jobList) {
		this.jobList = jobList;
	}
	public JobDao getJobDao() {
		return jobDao;
	}
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}
	public Application getApp() {
		return app;
	}
	public void setApp(Application app) {
		this.app = app;
	}
	public EmailService getEmailService() {
		return emailService;
	}
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	public SMS_Service getSmsService() {
		return smsService;
	}
	public void setSmsService(SMS_Service smsService) {
		this.smsService = smsService;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CandidateDto getNewCand() {
		return newCand;
	}
	public void setNewCand(CandidateDto newCand) {
		this.newCand = newCand;
	}
	public List<CandidateDto> getSelectedCandList() {
		return selectedCandList;
	}
	public void setSelectedCandList(List<CandidateDto> selectedCandList) {
		this.selectedCandList = selectedCandList;
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
	public CandidateDto getSelectedRow() {
		return selectedRow;
	}
	public void setSelectedRow(CandidateDto selectedRow) {
		this.selectedRow = selectedRow;
	}
	public ArrayList<String> getEducationList() {
		return educationList;
	}
	public void setEducationList(ArrayList<String> educationList) {
		this.educationList = educationList;
	}
	public UploadedFile getPic() {
		return pic;
	}
	public void setPic(UploadedFile pic) {
		this.pic = pic;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public CandPictureService getCandPicService() {
		return candPicService;
	}
	public void setCandPicService(CandPictureService candPicService) {
		this.candPicService = candPicService;
	}
	public WordExtract getWord() {
		return word;
	}
	public void setWord(WordExtract word) {
		this.word = word;
	}
	public PDFExtract getPdf() {
		return pdf;
	}
	public void setPdf(PDFExtract pdf) {
		this.pdf = pdf;
	}
	public CandidateDto getParsedCand() {
		return parsedCand;
	}
	public void setParsedCand(CandidateDto parsedCand) {
		this.parsedCand = parsedCand;
	}
	public boolean isUse_parsed_title() {
		return use_parsed_title;
	}
	public void setUse_parsed_title(boolean use_parsed_title) {
		this.use_parsed_title = use_parsed_title;
	}
	public boolean isUse_parsed_age() {
		return use_parsed_age;
	}
	public void setUse_parsed_age(boolean use_parsed_age) {
		this.use_parsed_age = use_parsed_age;
	}
	public boolean isUse_parsed_email() {
		return use_parsed_email;
	}
	public void setUse_parsed_email(boolean use_parsed_email) {
		this.use_parsed_email = use_parsed_email;
	}
	public boolean isUse_parsed_phone() {
		return use_parsed_phone;
	}
	public void setUse_parsed_phone(boolean use_parsed_phone) {
		this.use_parsed_phone = use_parsed_phone;
	}
	public boolean isUse_parsed_education() {
		return use_parsed_education;
	}
	public void setUse_parsed_education(boolean use_parsed_education) {
		this.use_parsed_education = use_parsed_education;
	}
	public boolean isUse_parsed_tags() {
		return use_parsed_tags;
	}
	public void setUse_parsed_tags(boolean use_parsed_tags) {
		this.use_parsed_tags = use_parsed_tags;
	}
	public boolean isUse_parsed_name() {
		return use_parsed_name;
	}
	public void setUse_parsed_name(boolean use_parsed_name) {
		this.use_parsed_name = use_parsed_name;
	}
	public boolean isUse_parsed_gender() {
		return use_parsed_gender;
	}
	public void setUse_parsed_gender(boolean use_parsed_gender) {
		this.use_parsed_gender = use_parsed_gender;
	}
	public boolean isUse_parsed_pic() {
		return use_parsed_pic;
	}
	public void setUse_parsed_pic(boolean use_parsed_pic) {
		this.use_parsed_pic = use_parsed_pic;
	}
	public String getParsedCandTags() {
		return parsedCandTags;
	}
	public void setParsedCandTags(String parsedCandTags) {
		this.parsedCandTags = parsedCandTags;
	}
	
	public KeyWordDao getKeyWordDao() {
		return keyWordDao;
	}
	public void setKeyWordDao(KeyWordDao keyWordDao) {
		this.keyWordDao = keyWordDao;
	}
	public static void main(String args[]){
		
	}
	public ArrayList<SearchRecordDto> getFavouriteList() {
		return favouriteList;
	}
	public void setFavouriteList(ArrayList<SearchRecordDto> favouriteList) {
		this.favouriteList = favouriteList;
	}
	public ArrayList<SearchRecordDto> getRecentSeachList() {
		return recentSeachList;
	}
	public void setRecentSeachList(ArrayList<SearchRecordDto> recentSeachList) {
		this.recentSeachList = recentSeachList;
	}
	public SearchRecordDao getSearchDao() {
		return searchDao;
	}
	public void setSearchDao(SearchRecordDao searchDao) {
		this.searchDao = searchDao;
	}
	public SearchRecordDto getNewFavourite() {
		return newFavourite;
	}
	public void setNewFavourite(SearchRecordDto newFavourite) {
		this.newFavourite = newFavourite;
	}
	public SearchRecordDto getSelectedFavourite() {
		return selectedFavourite;
	}
	public void setSelectedFavourite(SearchRecordDto selectedFavourite) {
		this.selectedFavourite = selectedFavourite;
	}
	public String getIdFilter() {
		return idFilter;
	}
	public void setIdFilter(String idFilter) {
		this.idFilter = idFilter;
	}
	public String getNameFilter() {
		return nameFilter;
	}
	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}
	public String getJobTitleFilter() {
		return jobTitleFilter;
	}
	public void setJobTitleFilter(String jobTitleFilter) {
		this.jobTitleFilter = jobTitleFilter;
	}
	public Integer getAgeFilter() {
		return ageFilter;
	}
	public void setAgeFilter(Integer ageFilter) {
		this.ageFilter = ageFilter;
	}
	public int getSearchRecordIdCount() {
		return searchRecordIdCount;
	}
	public void setSearchRecordIdCount(int searchRecordIdCount) {
		this.searchRecordIdCount = searchRecordIdCount;
	}
	public String getGenderFilter() {
		return genderFilter;
	}
	public void setGenderFilter(String genderFilter) {
		this.genderFilter = genderFilter;
	}
	public String getEducationFilter() {
		return educationFilter;
	}
	public void setEducationFilter(String educationFilter) {
		this.educationFilter = educationFilter;
	}
	public String getAddressFilter() {
		return addressFilter;
	}
	public void setAddressFilter(String addressFilter) {
		this.addressFilter = addressFilter;
	}
	public String getPhoneFilter() {
		return phoneFilter;
	}
	public void setPhoneFilter(String phoneFilter) {
		this.phoneFilter = phoneFilter;
	}
	public String getEmailFilter() {
		return emailFilter;
	}
	public void setEmailFilter(String emailFilter) {
		this.emailFilter = emailFilter;
	}
	public String getStatusFilter() {
		return statusFilter;
	}
	public void setStatusFilter(String statusFilter) {
		this.statusFilter = statusFilter;
	}
	public String getLastNoteFilter() {
		return lastNoteFilter;
	}
	public void setLastNoteFilter(String lastNoteFilter) {
		this.lastNoteFilter = lastNoteFilter;
	}
	public String getSearchLabel() {
		return searchLabel;
	}
	public void setSearchLabel(String searchLabel) {
		this.searchLabel = searchLabel;
	}
	public SearchRecordDto getSelectedRecent() {
		return selectedRecent;
	}
	public void setSelectedRecent(SearchRecordDto selectedRecent) {
		this.selectedRecent = selectedRecent;
	}
	public JobSelectHeader getJobHeader() {
		return jobHeader;
	}
	public void setJobHeader(JobSelectHeader jobHeader) {
		this.jobHeader = jobHeader;
	}
	
}
