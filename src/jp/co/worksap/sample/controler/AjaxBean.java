package jp.co.worksap.sample.controler;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="ajaxBean")
@ViewScoped
public class AjaxBean {
private String ajaxStatus;
@PostConstruct
private void init(){
	ajaxStatus = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ajaxStatus");
}
public String getAjaxStatus() {
	init();
	return ajaxStatus;
}

public void setAjaxStatus(String ajaxStatus) {
	this.ajaxStatus = ajaxStatus;
}
}
