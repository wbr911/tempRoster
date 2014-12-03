package jp.co.worksap.sample.dto;

public class SearchRecordDto {
private int id;
private String name;
private String type;
private String userId;
private String filters;
private String tags;
private String des;

public static final String FVOURITE="favourite";
public static final String RECENT="recent";
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public String getFilters() {
	return filters;
}
public void setFilters(String filters) {
	this.filters = filters;
}
public String getTags() {
	return tags;
}
public void setTags(String tags) {
	this.tags = tags;
}

public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

public String getDes() {
	if(des == null){
		des = createDes(tags);
	}
	return des;
}
public static String createDes(String tagStr){
	
	String res=tagStr.replaceAll("\\{\\}", "{&nbsp;}");
	res =res.replaceFirst("\\{", "<div class='left'>[Required]:<span style='font-weight:normal'>");
	res = res.replaceFirst("\\{", "<div class='left'>[Optional]:<span style='font-weight:normal'>");
	res = res.replaceFirst("\\{", "<div class='left'>[Exclude]:<span style='font-weight:normal'>");
	res = res.replaceFirst("\\{.*\\}", "");
	res = res.replaceAll("\\}", "</span></div>");

	return res;
	
}
public static void main(String args[]){
	String test="{java,}{}{}";
	System.out.println(test.replaceFirst("\\{\\}", "{ }"));	
}

}
