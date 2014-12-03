package jp.co.worksap.sample.spec.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import jp.co.worksap.sample.dao.ClientDao;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dto.ClientDto;
import jp.co.worksap.sample.dto.JobDto;

@FacesConverter("clientConverter")
public class ClientConverter implements Converter{
	@Override
	public Object getAsObject(FacesContext fc, UIComponent arg1, String arg2) {
		if(arg2.equals("Select One")){
			return null;
		}
		ClientDao clientDao = (ClientDao)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientDao");		
		for( ClientDto dto:clientDao.getAllClients()){
			if(dto.getName().equals(arg2)){
				return dto;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		String res = "";
		ClientDto dto = (ClientDto) arg2;
		if(dto==null){
			return "Select One";
		}
		res = dto.getName();
		return res;
	}

}
