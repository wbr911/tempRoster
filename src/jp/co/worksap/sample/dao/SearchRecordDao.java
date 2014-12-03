package jp.co.worksap.sample.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import jp.co.worksap.sample.dto.SearchRecordDto;
import jp.co.worksap.sample.spec.utils.Resource;
@ManagedBean(name="searchRecordDao")
@ApplicationScoped
public class SearchRecordDao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8678573233851245926L;
	private transient Connection con;
	private int idCount;
	private int cacheNumber;
	private ArrayList<SearchRecordDto>cacheList;
	private ArrayList<SearchRecordDto>cacheFavouriteList;
	private ArrayList<SearchRecordDto> addCacheList = new ArrayList<SearchRecordDto>();
	private ArrayList<Integer> deleteCacheList = new ArrayList<Integer>();
	@PostConstruct
	private void init(){
		con = Resource.getConnection();
		idCount = createNewId();
	}
	@PreDestroy
	private void destroy(){
		_addSearchDtoInBatch(addCacheList);
		_deleteSearchDtoInBatch(deleteCacheList);
	}
	public int createNewId(){
		
		try {
			int id = 1;
			Statement stm = con.createStatement();
			ResultSet rs=stm.executeQuery("select max(id) from search_record");
			if(rs.next()){
			id = rs.getInt(1)+1;
			stm.close();
			}
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public ArrayList<SearchRecordDto> getRecentListByUserId(String userId){
		if(cacheList!=null){
			return cacheList;
		}
		con = Resource.getConnection();
		cacheList = new ArrayList<SearchRecordDto>();
		try {
			PreparedStatement stm = con.prepareStatement(
					"select * from search_record where user_Id=? and record_type=? and rownum<=10 order by to_date(name,'MM/dd/yyyy-hh24:mi') desc");
			stm.setString(1,userId);
			stm.setString(2, SearchRecordDto.RECENT);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				cacheList.add(fillSearchRecordDto(rs));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cacheList;
	}
	public ArrayList<SearchRecordDto> getFavouriteListByUserId(String userId){
		if(cacheFavouriteList!=null){
			return cacheFavouriteList;
		}
		con = Resource.getConnection();
		ArrayList<SearchRecordDto>cacheFavouriteList = new ArrayList<SearchRecordDto>();
		try {
			PreparedStatement stm = con.prepareStatement(
					"select * from search_record where user_id =? and record_type=?");
			stm.setString(1,userId);
			stm.setString(2, SearchRecordDto.FVOURITE);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				cacheFavouriteList.add(fillSearchRecordDto(rs));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cacheFavouriteList;
	}
	public void addSearchDto(SearchRecordDto search){
		addCacheList.add(search);
		cacheNumber++;
		if(cacheNumber>=10){
			_addSearchDtoInBatch(addCacheList);
			addCacheList = new ArrayList<SearchRecordDto>();
			cacheNumber=0;
		}
		
	}
	private void _addSearchDtoInBatch(ArrayList<SearchRecordDto>list){
		con = Resource.getConnection();
		try {
			PreparedStatement stm = con.prepareStatement("insert into search_record values(?,?,?,?,?,?)");
			for(SearchRecordDto dto:list){
			fillInsertPstm(stm, dto);
			stm.addBatch();
			}
			stm.executeBatch();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteSearchDto(int id){
		cacheNumber++;
		deleteCacheList.add(id);
		if(cacheNumber>=10){
			_deleteSearchDtoInBatch(deleteCacheList);
			cacheNumber=0;
		}
		
	}
	private void _deleteSearchDtoInBatch(ArrayList<Integer> list){
		con = Resource.getConnection();
		try {
			PreparedStatement stm = con.prepareStatement("delete from search_record where id =?");
			for(Integer id:list){
			stm.setInt(1, id);
			stm.addBatch();
			}
			stm.executeBatch();
			deleteCacheList = new ArrayList<Integer>();
			stm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void fillInsertPstm(PreparedStatement stm, SearchRecordDto search){
		
		try {
			stm.setInt(1, search.getId());
			stm.setString(2, search.getName());
			stm.setString(3,search.getType());
			stm.setString(4, search.getUserId());
			stm.setString(5, search.getFilters());
			stm.setString(6, search.getTags());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private SearchRecordDto fillSearchRecordDto(ResultSet rs){ 
		SearchRecordDto search = new SearchRecordDto();
		try {
			search.setId(rs.getInt(1));
			search.setName(rs.getString(2));
			search.setType(rs.getString(3));
			search.setUserId(rs.getString(4));
			search.setFilters(rs.getString(5));
			search.setTags(rs.getString(6));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return search;
	}

}
