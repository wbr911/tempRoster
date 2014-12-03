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

import jp.co.worksap.sample.dto.Address;
import jp.co.worksap.sample.dto.ClientDto;
import jp.co.worksap.sample.spec.utils.Resource;

@SessionScoped
@ManagedBean(name="clientDao")
public class ClientDao implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = -9149282331842361387L;
private ArrayList<ClientDto> clientList;
 private transient Connection conn;
 @PostConstruct
 public void init()
 {
     conn = Resource.getConnection();
     String sql = "select * from client order by name";
     clientList = new ArrayList<ClientDto>();
     //createTestData();
     try {
		ResultSet rs =conn.createStatement().executeQuery(sql);
		while(rs.next()){
			ClientDto client = new ClientDto();
			client.setDes(rs.getString("des"));
			client.setContactName(rs.getString("contactName"));
			client.setName(rs.getString("name"));
			client.setEmail(rs.getString("email"));
			client.setPhone(rs.getString("phone"));
			client.setAddress(new Address(rs.getString("address"), 0, 0));
			clientList.add(client);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
	 
 }
 
 public ArrayList<ClientDto>getAllClients(){
	 return clientList;
 }
 public ClientDto getById(String id){
	 for(ClientDto dto : clientList){
		 if(dto.getName().equals(id))
			 return dto;
	 }
	 return null;
 }
 public ClientDto getByEmail(String email){
	 for(ClientDto dto : clientList){
		 if(dto.getEmail().equals(email))
			 return dto;
	 }
	 return null;
 }
 private ArrayList<ClientDto> createTestData(){
//	 ArrayList<ClientDto> testData = new ArrayList<ClientDto>();
//	 for(int i = 0 ; i < 10; i++){
//		ClientDto dto = new ClientDto();
//		dto.setName("company" + i);
//		dto.setDes("company" + i+" is a famous specialist in the xxxx field,xxxxxxxxxxxxxxxxxxxxxxx<br/>xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx<br/>xxxxxxxxxxxxxxxxxxxx");
//		dto.setContactName("contact"+i);
//		dto.setEmail("contact"+i+"@163.com");
//		dto.setPhone("1350543966"+i);
//		addClient(dto);
//		testData.add(dto);
//	 }
//	 return testData;
	 // copy from old data base
	 return null;
 }
 public void addClient(ClientDto dto){
	 conn = Resource.getConnection();
	 String sql = "insert into client values(?,?,?,?,?,?)";
	 PreparedStatement stm;
	try {
		stm = conn.prepareStatement(sql);
		stm.setString(1, dto.getName());
		stm.setString(2, dto.getDes());
		stm.setString(3, dto.getContactName());
		stm.setString(4, dto.getEmail());
		stm.setString(5, dto.getPhone());
		stm.setString(6, dto.getAddress().getDes());
		stm.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
 }
 public static void main(String args[]){
	 new ClientDao().init();
 }
 
}
