package jp.co.worksap.sample.spec.utils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import jp.co.worksap.sample.controler.CandidateView;
import jp.co.worksap.sample.controler.JobView;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dto.ClientDto;
import jp.co.worksap.sample.dto.JobDto;


@FacesConverter("jobConverter")
public class JobConverter implements Converter{
	@Override
	public Object getAsObject(FacesContext fc, UIComponent arg1, String arg2) {
		if(arg2.equals("Id")){
			return null;
		}
		JobDao jobDao = (JobDao)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("jobDao");		
		for( JobDto job:jobDao.getAllJob()){
			if(job.getId().equals(arg2)){
				return job;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		String res = "";
		JobDto dto = (JobDto) arg2;
		res = dto.getId();
		return res;
	}
	



}
