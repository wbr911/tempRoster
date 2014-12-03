package jp.co.worksap.sample.controler;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import jp.co.worksap.sample.dao.UserDao;
import jp.co.worksap.sample.dto.ClientDto;
import jp.co.worksap.sample.dto.User;
@ManagedBean(name="userLogin")
@ViewScoped
public class UserLogin implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 8046377850369354479L;
private String id;
private String pwd;
@ManagedProperty("#{userDao}")
private UserDao userDao; 
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
public void login(){
	User user =userDao.getById(id);
	if(user!=null && user.getPwd().equals(pwd)){
		 ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
    	 HttpSession session =(HttpSession) ec.getSession(true);
    	 session.setAttribute("user",user);
    	 session.setAttribute("role", user.getRole());
    	 try {
    		 //FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"waiting", "loading"));
			ec.redirect("main.jsf");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else{
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,"wrong", "id doesn't exist or password is wrong"));
	}
}
public UserDao getUserDao() {
	return userDao;
}
public void setUserDao(UserDao userDao) {
	this.userDao = userDao;
}

}
