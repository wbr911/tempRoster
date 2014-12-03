package jp.co.worksap.sample.spec.utils;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.worksap.sample.dto.CandidateDto;

public class TagsParser {
	public static  ArrayList<String>[] parseTag(String tagStr){
		
		
		ArrayList<String>[] tagsArray = new ArrayList[3];
		for(int i =0 ;i< 3;i++){
			tagsArray[i] = new ArrayList<String>();
		}
		if(tagStr==null|| tagStr.equals("")){
			return tagsArray;
		}
		tagStr = tagStr.replace("{", "");
		String[] tagsList = tagStr.split("}");
		for(int i=0;i< tagsList.length;i++){
			if(tagsList[i].length()!=0 && tagsList[i].charAt(tagsList[i].length()-1)==','){ // remove the extra , in the end
				tagsList[i]  =tagsList[i].substring(0, tagsList[i].length()-1);
			}
			if(tagsList[i].length()==0){
			   continue;
			}else{
			tagsArray[i] = new ArrayList<String>(Arrays.asList(tagsList[i].split(",")));
			}
		}
		return tagsArray;
	
	}
	public static String createTagsStr(CandidateDto dto){
		String res = "";
		res += createStr(dto.getSkills());
		res += createStr(dto.getCertificates());
		res += createStr(dto.getKeywords());
		return res;
		
	}
	private static String createStr(ArrayList<String> datas){
		String res = "{";
		for(int i = 0 ; i< datas.size();i++){
			if(i!= datas.size()){
			res += datas.get(i) +",";
			}else{
				res += datas.get(i);
			}
		}
		res+="}";
		return res;
	}
	/***
	 * 
	 * @param total with the format like "a,b,c"
	 * @return
	 */
	public static ArrayList<String> createRandomTags(String str,int min){
		String[] total = str.split(",");
		ArrayList<String> res = new ArrayList<String>();
		for(int i =0 ; i< total.length ;i++){
			
			if(Math.random()>0.3|| i<=(min-1)){
				res.add(total[i]);
			}
		}
		if(res.size()==0){
			res.add(total[((int)(Math.random()*total.length))]);
		}
		return res;
	}
	public static void main(String args[]){
		String var = "{}{}{}";
		ArrayList<String>[] list= TagsParser.parseTag(var);
		for(int i = 0 ; i < list.length ;i++){
			for(int j = 0 ;j < list[i].size();j++){
				System.out.print(list[i].get(j) +"/");

			}
			System.out.println();
		}
		
	}
}
