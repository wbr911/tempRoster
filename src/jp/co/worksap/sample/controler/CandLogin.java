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
import jp.co.worksap.sample.dto.CandidateDto;

@ViewScoped
@ManagedBean(name="candLogin")
public class CandLogin  implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 6914226809318365534L;
	@ManagedProperty("#{candidateDao}")
     private CandidateDao candDao;
	 private String id;
	 private String pwd;
	 HttpSession session;
	 
	 @PostConstruct
	 public void init(){
		 ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
    	 session =(HttpSession) ec.getSession(true);
    	 CandidateDto cand = null;
    	 String id = ec.getRequestParameterMap().get("id");
		 String pwd = ec.getRequestParameterMap().get("pwd");
		 if(id!=null && pwd !=null){
			 cand = candDao.getById(id);
			 if(cand!=null && cand.getPhone().equals(pwd)){
				 session.setAttribute("id", id);
				 session.setAttribute("role", "cand");
				 try {
					ec.redirect("candPortal.jsf");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
			 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("wrong", "id doesn't exist or password is wrong"));
	      }
		 
    	 
	 }
	 public void login(){
		 CandidateDto dto =candDao.getById(id);
		 if(dto!=null && dto.getPwd().equals(pwd)){
			  session.setAttribute("role", "cand");
			  session.setAttribute("id", id);
			  try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("candPortal.jsf");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("wrong", "id doesn't exist or password is wrong"));
		 
		 
	 }
	public CandidateDao getCandDao() {
		return candDao;
	}
	public void setCandDao(CandidateDao candDao) {
		this.candDao = candDao;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	 
}
