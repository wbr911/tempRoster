package jp.co.worksap.sample.controler;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="errorView")
public class ErrorView {
private String role;
@PostConstruct
private void init(){
	role=(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("role");
}
public String getRole() {
	return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("role");
}

public void setRole(String role) {
	this.role = role;
}

}
