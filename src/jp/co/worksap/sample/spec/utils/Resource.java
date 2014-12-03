package jp.co.worksap.sample.spec.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Resource {
	public static final String DRIVER="oracle.jdbc.driver.OracleDriver";  
    
   public static final String URL="jdbc:oracle:thin:@172.26.142.249:1521:nekyoiku";  
   public static final String PWD="stms140820";  	 
//	public static final String URL="jdbc:oracle:thin:@172.26.142.107:1521:nekyoiku";
//	public static final String PWD="STMS140820";
    public static final String USER="stms140820";  
    public static int connectFailureTime=0;
   
     
    public static Connection con=null;
    
public static Connection getConnection(){
	if(con==null){
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean status = false;
		while(!status){
		try {
			con = DriverManager.getConnection(URL,USER,PWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("connection error" + connectFailureTime++);
			continue;
		}
		status = true;
		}
	}
	return con;
}
}
