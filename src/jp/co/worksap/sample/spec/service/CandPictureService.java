package jp.co.worksap.sample.spec.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;


import org.primefaces.model.UploadedFile;

@ManagedBean(name="candPicService")
@ApplicationScoped
public class CandPictureService {
    private String context;
    @PostConstruct
    public void init(){
    	context =FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    }
    public String getImgPath(String id){
    	return "img\\pic"+id;
    }
	public String addPic(String id,UploadedFile pic){
		
		String suffix = getSuffix(pic.getFileName());
		
		String fileName ="img\\"+ "pic"+id +"."+ suffix;
		String filePath =context+ fileName;
    	 File file = new File(filePath);
    	 if(!file.exists()){
    		 try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 try {
    		 InputStream input = pic.getInputstream();
    		 OutputStream os =new FileOutputStream(file);
    		 int len =0;
    		 byte[] buffer = new byte[1024];
    		 String content;
    		 while((len=input.read(buffer,0,1024))!=-1){
    			 os.write(buffer, 0, len);
    			 
    		 }
    		 os.flush();
    		 os.close();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	 return "/"+fileName.replace("\\", "/");
     }
	public static String getSuffix(String name){
		Pattern p = Pattern.compile("\\w{3,}$");
		Matcher m =p.matcher(name);
		if(m.find()){
			return m.group();
			
		}else{
			return null;
		}
	
	}
	
}
