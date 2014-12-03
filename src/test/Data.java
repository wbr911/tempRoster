package test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@SessionScoped

public class Data {
	private String id;
	private String input;

	public Data(String id) {
		super();
		this.id = id;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void choose(){
		id  += input;
		PrintWriter response;
		try {
			response = (PrintWriter) FacesContext.getCurrentInstance().getExternalContext().getResponseOutputWriter();
			response.write("ajaxResponse");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void main(String args[]){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			System.out.println(sdf.parse("06/25/1992").compareTo(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
