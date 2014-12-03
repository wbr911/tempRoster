package jp.co.worksap.sample.dto;

import java.io.Serializable;

public class Address implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String des;
	private double x;
	private double y;
	public Address(){
		
	}
	
	public Address(String des, double x, double y) {
		super();
		this.des = des;
		this.x = x;
		this.y = y;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
