package jp.co.worksap.sample.spec.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.pdfbox.util.PDFTextStripper;
@ManagedBean(name="PDFExtract")
@ApplicationScoped
public class PDFExtract {
@ManagedProperty("#{CVParser}")
private CVParser cvParser;
private String webPath="";
@PostConstruct
public void init(){
	  webPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
}
 public CandidateDto parsePDF(InputStream in,String picPath,CandidateDto cand){
  try{   
   PDDocument pdfDocument = PDDocument.load(in);
   // get picture
   List pages =pdfDocument.getDocumentCatalog().getAllPages();
   for (int i = 0; i < pages.size(); i++) {
	   PDPage page = (PDPage) pages.get(i);
	   Map imgs = page.findResources().getImages();
	   if (null != imgs) {
		   Set keySet = imgs.keySet();
		   Iterator it = keySet.iterator();
		   if (it.hasNext()) {
			   Object obj = it.next();
			   PDXObjectImage img = (PDXObjectImage) imgs.get(obj);		  
			   String candPic =picPath+"."+img.getSuffix();
			   File pFile = new File(webPath+candPic);
			   if(!pFile.exists()){
				   pFile.createNewFile();
			   }
			   img.write2file(webPath+picPath);
			   candPic ="/"+ candPic.replace("\\", "/");
			   cand.setPic(candPic);
			   break;
		   }
       }
   }
   // end of get pic
   
   // get content
   PDFTextStripper stripper = new PDFTextStripper();
   stripper.setSortByPosition(true);
   String docText = stripper.getText(pdfDocument);
   cand =cvParser.parseCV(docText, cand);
   pdfDocument.close();
   cand.setResume(docText);
   return cand;
  }catch(Exception e){
   System.out.println("bb="+e.getMessage());
  }
  return null;
 }
 
 public CVParser getCvParser() {
	return cvParser;
}

public void setCvParser(CVParser cvParser) {
	this.cvParser = cvParser;
}

public static void main(String args[]){
  CandidateDto cand = new CandidateDto();	
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
	PDFExtract pdfe = new PDFExtract();
	pdfe.setCvParser(parser);
	pdfe.parsePDF(new FileInputStream(new File("C:\\Users\\stms140802\\Desktop\\Test.pdf")),
			  "E:\\pdf_pic",cand);
	System.out.println(cand.getName());
	pdfe.parsePDF(new FileInputStream(new File("C:\\Users\\stms140802\\Desktop\\Test2.pdf")),
			  "E:\\pdf_pic",cand);
	System.out.println(cand.getName());
	pdfe.parsePDF(new FileInputStream(new File("C:\\Users\\stms140802\\Desktop\\Test3.pdf")),
			  "E:\\pdf_pic",cand);
	System.out.println(cand.getName());
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 }
}