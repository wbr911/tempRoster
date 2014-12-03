package jp.co.worksap.sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jp.co.worksap.sample.dto.TitleDto;
import jp.co.worksap.sample.spec.utils.Resource;

@ManagedBean(name="titleDao")
@ApplicationScoped
public class TitleDao implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8583636244909707391L;
	/**
	 * 
	 */

	private transient Connection con;
    ArrayList<TitleDto>titles;
    ArrayList<String>titleStrs;
    @PostConstruct
    public void init(){
	 con = Resource.getConnection();
	 titles = new ArrayList<TitleDto>();
	 titleStrs = new ArrayList<String>();
	 try {
		ResultSet rs =con.createStatement().executeQuery("select * from title order by id");
		while(rs.next()){
			TitleDto t = new TitleDto();
			t.title = rs.getString("title");
			t.salaries = rs.getDouble("salaries");
			t.quotation = rs.getDouble("quotation");
			t.skills = rs.getString("skills");
			titles.add(t);
			titleStrs.add(t.title);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
    public ArrayList<String>getTitleList(){
    	return titleStrs;
    }
    public void addTitle(TitleDto title){
    	con = Resource.getConnection();
    	try {
			PreparedStatement stm = con.prepareStatement("insert into title values(?,?,?,?,?)");
			stm.setInt(1,titles.size()+1);
			stm.setString(2, title.title);
			stm.setDouble(3, title.salaries);
			stm.setDouble(4, title.quotation);
			stm.setString(5, title.skills);
			stm.executeUpdate();
			titleStrs.add(title.title);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void updateTitle(String title){
    	for(TitleDto dto: titles){
    		if(dto.title.equals(title)){
    			return ;
    		}
    	}
    	TitleDto t = new TitleDto();
    	t.title = title;
    	t.salaries=100;
    	t.quotation=120;
    	addTitle(t);
    }
    public TitleDto getByTitle(String t){
    	for(TitleDto dto: titles){
    		if(dto.title.equals(t)){
    			return dto;
    		}
    	}
    	return null;
    }
	public ArrayList<TitleDto> getTitles() {
		return titles;
	}
	public void setTitles(ArrayList<TitleDto> titles) {
		this.titles = titles;
	}
    
		
}

