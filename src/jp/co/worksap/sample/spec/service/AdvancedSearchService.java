package jp.co.worksap.sample.spec.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import jp.co.worksap.sample.dto.CandidateDto;
import jp.co.worksap.sample.spec.utils.TagsParser;
@ManagedBean(name ="advancedSearch")
@ApplicationScoped
public class AdvancedSearchService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int REQUIRED = 0;
	private static final int OPTIONAL = 1;
	private static final int EXCLUDED = 2;
	public List<CandidateDto> search(List<CandidateDto>datas , String tagStr , String addressStr){
		ArrayList<String>[] tagList = TagsParser.parseTag(tagStr);
		// required, optional, exclude
		ArrayList<CandidateDto> res = new ArrayList<CandidateDto>();
		ArrayList<DtoWrapper> temp = new ArrayList<DtoWrapper>();
		for(CandidateDto dto: datas){
			String lowerCaseTagStr = dto.getTagsStr().toLowerCase();
			String regex = "";
			String matchedTagStr="";
			dto.setMatchedTagStr("");
			boolean hasAllRequiredTags = true;
			for(String tag : tagList[REQUIRED]){
				regex = "(^|.*\\W)" + regxEscape(tag.toLowerCase()) + "(\\W|$).*";
				if(!lowerCaseTagStr.matches(regex)){
					hasAllRequiredTags=false;
					break;
				}
				matchedTagStr+=tag+"/";
			}
			if(!hasAllRequiredTags){
				continue;
			}
			// has all the required tags ,then check the excluded tags
			boolean hasNoExcludedTags = true;
			for(String tag: tagList[EXCLUDED]){
				regex = "(^|.*\\W)" + regxEscape(tag.toLowerCase()) + "(\\W|$).*";
				if(lowerCaseTagStr.matches(regex)){
					hasNoExcludedTags = false;
				}
				
			}
			if(!hasNoExcludedTags){
				continue;
			}
			DtoWrapper wrapper = new DtoWrapper(dto, "");
			for(String tag: tagList[OPTIONAL]){  // the optional tags has a different priority based on the order in the list;
				regex = "(^|.*\\W)" + regxEscape(tag.toLowerCase()) + "(\\W|$).*";
				if(lowerCaseTagStr.matches(regex)){
					matchedTagStr+=tag+"/";
					wrapper.priority +="1";
				}else{
					wrapper.priority +="0";
				}
			}
			wrapper.dto.setMatchedTagStr(matchedTagStr);
			temp.add(wrapper);
		}
		 Collections.sort(temp);
		int size = temp.size();
		for(int i = size-1 ;i >=0;i--){ 
			  // descending order
			res.add(temp.get(i).dto);
		}
		
		return res;
	}
	private String regxEscape(String regx){
		regx=regx.replaceAll("\\+", "\\\\+"); 
		regx=regx.replaceAll("\\.", "\\\\."); 
		return regx;
	}
//	public static void main(String args[]){
//		String[] test =  new String[]{"a,b,c" ,"b,ab,c","b,a,c"};
//		for(int i = 0 ; i < test.length ; i++){
//			System.out.println(test[i].matches("\\ba\\b"));
//		}
//	}
	class DtoWrapper implements Comparable<DtoWrapper>{
		public CandidateDto dto;
		public String priority;
		public DtoWrapper(CandidateDto dto, String priority){
			this.dto  =dto;
			this.priority = priority;
		}
		@Override
		public int compareTo(DtoWrapper o) {
			int res = this.priority.compareTo(o.priority);
			if(res  == 0){
				// the more skills tags the candidates have, the higher priority the candidates have
				Integer tags = this.dto.getSkills().size()+this.dto.getCertificates().size();
				Integer oTags = o.dto.getSkills().size()+o.dto.getCertificates().size();
				res = tags.compareTo(oTags);
				if(res==0){
				return Integer.valueOf(this.dto.getId().replaceAll(CandidateDto.IDprefix, ""))
						.compareTo(
								Integer.valueOf(o.dto.getId().replaceAll(CandidateDto.IDprefix, "")));
				}
			}	
			return res;
		}
		
	}
	public static void main(String args[]){
		String tags  =",c++";
		System.out.println(tags.matches("(^|.*\\W)c\\+\\+\\W"));
		
	}

}
