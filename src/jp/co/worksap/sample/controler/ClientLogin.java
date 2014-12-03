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
import jp.co.worksap.sample.dao.ClientDao;
import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.dto.ClientDto;

@ViewScoped
@ManagedBean(name="clientLogin")
public class ClientLogin implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 6317526083265353594L;
	@ManagedProperty("#{clientDao}")
     private ClientDao clientDao;
	 private String id;
	 private String pwd;
	 HttpSession session;
	 
	 @PostConstruct
	 public void init(){
		 ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
    	 session =(HttpSession) ec.getSession(true);
    	 ClientDto client = null;
    	 String id = ec.getRequestParameterMap().get("id");
		 String pwd = ec.getRequestParameterMap().get("pwd");
		 if(id!=null && pwd !=null){
			 client = clientDao.getByEmail(id);
			 if(client!=null && client.getPhone().equals(pwd)){
				 session.setAttribute("id", client.getName());
				 session.setAttribute("role", "client");
				 try {
					ec.redirect("clientPortal.jsf");
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
		 ClientDto dto =clientDao.getByEmail(id);
		 if(dto!=null && dto.getPhone().equals(pwd)){
			  session.setAttribute("role", "client");
			  ClientDto client = clientDao.getByEmail(id);
			  session.setAttribute("id", client.getName());  
			  try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("clientPortal.jsf");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("wrong", "id doesn't exist or password is wrong"));
		 
	 }
	
	public ClientDao getClientDao() {
		return clientDao;
	}
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
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
