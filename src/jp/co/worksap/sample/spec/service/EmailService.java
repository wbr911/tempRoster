package jp.co.worksap.sample.spec.service;
import java.io.Serializable;
import java.util.Date;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.BeforeCompletion;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.*;
import javax.management.DescriptorKey;
import javax.persistence.PostRemove;
@ManagedBean(name="emailService")
@SessionScoped
public class EmailService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static int port = 25;

    static String server = "smtp.163.com";//邮件服务器（webnet）

    static String from = "wbr911@163.com";//发送者

    static String user = "wbr911@163.com";//发送者地址
    static String password = "5213344jyz";//密码
    private Transport transport;
    private Session session;
    
    public void init(){
    	Properties props = new Properties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(props, null);
        
        try {
        	transport = session.getTransport("smtp");
			transport.connect(server, user, password);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public  void sendEmail(String email, String subject, String body) {
    	if(transport==null){
    		init();
    	}
        try {
        	long startTime =System.currentTimeMillis();

            MimeMessage msg = new MimeMessage(session);
            msg.setSentDate(new Date());
            InternetAddress fromAddress = new InternetAddress(from);
            msg.setFrom(fromAddress);
            InternetAddress[] toAddress = new InternetAddress[1];
            toAddress[0] = new InternetAddress(email);
            msg.setRecipients(Message.RecipientType.TO, toAddress);
            msg.setSubject(subject, "UTF-8");    
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            html.setContent(body,"text/html;charset=utf-8");
            mainPart.addBodyPart(html);
            msg.setContent(mainPart);
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());
            long endTime = System.currentTimeMillis();
            System.out.println("emailSerive:"+(endTime-startTime)/1000);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
   
    public static void main(String args[]){
    	//EmailService.sendEmail("wbr911Private@gmail.com", "test", "this is test email from wbr911@163.com");
    }
}
