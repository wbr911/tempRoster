package jp.co.worksap.sample.spec.service;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.dom4j.Document;   
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;   
import org.dom4j.Element;   
@ManagedBean(name="smsService")
@ApplicationScoped
public class SMS_Service implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	public static String id =  "cf_wbr911";
	public static String pwd = "5213344";
	
	
	public void send(String phone ,String content) {
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");		
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");	


		int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
		
	    content = new String("������֤���ǣ�" + content+ "���벻Ҫ����֤��й¶�������ˡ�"); 

		NameValuePair[] data = {//�ύ����
			    new NameValuePair("account", id), 
			    new NameValuePair("password", pwd), //�������ʹ�����������ʹ��32λMD5����
			    //new NameValuePair("password", util.StringUtil.MD5Encode("����")),
			    new NameValuePair("mobile", phone), 
			    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);		
		
		
		try {
			client.executeMethod(method);	
			
			String SubmitResult =method.getResponseBodyAsString();
					
			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String smsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
						
			if(code == "2"){
				System.out.println("�����ύ�ɹ�");
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public static void main(String agrs[]){
		new SMS_Service().send("18551831696", "there are new offers for you, you can check it in our system");
	}
}
