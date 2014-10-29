package db2.explainparser;

public class AccessNode extends Node{
	public static String KeywordIndex = "IDX1109302046250";
	public static String EntityTypeIndex = "IDX1109302046370";
	public static String DocumentEntityIndex = "IDX1109302046300";
	
	public String Method;
	public String key ;
	public  AccessNode child;
	public void setKey(String key) {
		this.key  = key;
	}
}
