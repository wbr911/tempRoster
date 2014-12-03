package jp.co.worksap.sample.dto;

import java.io.Serializable;

public class User implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String id;
 private String name;
 private String role;
 private String phone;
 private String email;
 private String pwd;
public User(){
	
}
public User(String id, String name, String role,String phone, String email) {
	super();
	this.id = id;
	this.name = name;
	this.role = role;
	this.phone = phone;
	this.email =email;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
 
}
