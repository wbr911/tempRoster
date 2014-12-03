package jp.co.worksap.sample.dto;

import java.io.Serializable;

public class ClientDto implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private String name;
private String des;
private String contactName;
private String email;
private String phone;
private Address address;
public ClientDto(){
	
}




public ClientDto(String name, String des) {
	super();
	this.name = name;
	this.des = des;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDes() {
	return des;
}
public void setDes(String des) {
	this.des = des;
}
public String getContactName() {
	return contactName;
}
public void setContactName(String contactName) {
	this.contactName = contactName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}




public Address getAddress() {
	return address;
}




public void setAddress(Address address) {
	this.address = address;
}
 
}
