package jp.co.worksap.sample.controler;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
@ApplicationScoped
@ManagedBean(name="applicationView")
public class Application implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String companyName="Work Tempers Agency";
public String getCompanyName() {
	return companyName;
}

public void setCompanyName(String companyName) {
	this.companyName = companyName;
}

}
