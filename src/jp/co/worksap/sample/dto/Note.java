package jp.co.worksap.sample.dto;

import java.io.Serializable;

public class Note implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int count = 0;
	private String consultant;
	private String content;
	private String date;
	private String id;
	public Note(String consultant, String content, String date) {
		super();
		this.consultant = consultant;
		this.content = content;
		this.date = date;
		this.id=""+count++;
	}
	public Note(){
		this.id=""+count++;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConsultant() {
		return consultant;
	}
	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
