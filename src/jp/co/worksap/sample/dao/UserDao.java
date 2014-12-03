package jp.co.worksap.sample.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import jp.co.worksap.sample.dto.User;

@ManagedBean(name="userDao")
@ApplicationScoped
public class UserDao implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -1692055690914632357L;
private ArrayList<User> userList;
@PostConstruct
public void init(){
	userList = makeTestData();
}
public User getById(String id){
	for(User u:userList){
		if(u.getId().equals(id)){
			return u;
		}
	}
	return null;
}
private ArrayList<User> makeTestData(){
	ArrayList<User> res =new ArrayList<User>();
	for(int i = 0;i<3;i++){
		User u = new User();
		u.setId("user"+i);
		u.setEmail("wbr911"+"@163.com");
		u.setName("user"+i);
		u.setPhone("1350543966"+i);
		u.setRole("consultant");
		u.setPwd("123456");
		res.add(u);
	}
	User u = new User();
	u.setId("manager"+1);
	u.setEmail("manager"+1+"@163.com");
	u.setName("manager"+1);
	u.setPhone("13505439665");
	u.setRole("manager");
	u.setPwd("123456");
	return res;
}
}
