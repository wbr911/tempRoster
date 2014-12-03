package jp.co.worksap.sample.spec.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import jp.co.worksap.sample.dao.ClientDao;
import jp.co.worksap.sample.dao.JobDao;
import jp.co.worksap.sample.dao.KeyWordDao;
import jp.co.worksap.sample.dao.TitleDao;
import jp.co.worksap.sample.dto.CandidateDto;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
@ManagedBean(name="wordExtract")
@ApplicationScoped
public class WordExtract {
	@ManagedProperty("#{CVParser}")
	private CVParser cvparser;
	private String webPath="";
	@PostConstruct
	public void init(){
		webPath= FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	}
	public CandidateDto parseWord(String suffix,InputStream in,String picPath,CandidateDto cand){
		if(suffix.equals("doc")){
			return parse2003(in, picPath, cand);
		}else if(suffix.equals("docx")){
			return parse2007(in, picPath, cand);
		}else{
		return cand;
		}
	}
	public CandidateDto parse2007(InputStream in,String picPath,CandidateDto cand){
		try { 
		XWPFDocument doc;
		doc = new XWPFDocument(in);
         XWPFPictureData p= doc.getAllPictures().get(0);
         String candPic = picPath +"."+ p.getFileName().split("\\.")[1];
         File outFile = new File(webPath + candPic);
         if(!outFile.exists()){
         	outFile.createNewFile();
         }
         OutputStream os = new FileOutputStream(outFile.getAbsolutePath());
         os.write(p.getData());
         cand.setPic("/"+candPic.replace("\\", "/"));
         POIXMLTextExtractor extractor = new XWPFWordExtractor(doc);
         String content= extractor.getText();
         cand.setResume(content);
         return cvparser.parseCV(content, cand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cand;
	}
	private CandidateDto parse2003(InputStream in,String picPath,CandidateDto cand){
	  try{
	   HWPFDocument hDocument= new HWPFDocument(in);
       Picture p = hDocument.getPicturesTable().getAllPictures().get(0);
      
       Range rang= hDocument.getRange();
       String candPic =picPath + "." +p.suggestFileExtension();
       File outFile = new File(webPath +candPic);
       if(!outFile.exists()){
       	outFile.createNewFile();
       }
       OutputStream os = new FileOutputStream(outFile);
       os.write(p.getContent());
       cand.setPic("/"+candPic.replace("\\", "/"));
       return cvparser.parseCV(rang.text(), cand);
	  }catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}
       return cand;
	}
	
    public CVParser getCvparser() {
		return cvparser;
	}
	public void setCvparser(CVParser cvparser) {
		this.cvparser = cvparser;
	}
	public static void main(String[] args) {
       try {
    	   ClientDao cd = new ClientDao();
    	   cd.init();
    	   TitleDao td = new TitleDao();
    	   
    	   td.init();
    	   
    	   KeyWordDao kwd = new KeyWordDao();
    	   kwd.init();
    	   CVParser parser = new CVParser();
    	   parser.setKwDao(kwd);
    	   parser.setTitleDao(td);
		WordExtract w = new WordExtract();
		w.setCvparser(parser);
		CandidateDto cand  =new CandidateDto();
		//w.parse2003(new FileInputStream(new File("C:\\Users\\stms140802\\Desktop\\Test2.doc")), "E:\\outDoc", new CandidateDto());
		w.parse2007(new FileInputStream(new File("C:\\Users\\stms140802\\Desktop\\Test.docx")), "E:\\outDocx",cand );
		System.out.println(cand.getName());
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}