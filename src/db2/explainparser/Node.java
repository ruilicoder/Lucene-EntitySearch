package db2.explainparser;

public class Node {
	public static String HASH_JOIN  = "Hash Join";
	public static String LOOP_JOIN = "Nested Loop Join";
	public static String INDEX_SCAN = "INDEX";
	public int[] columnID;
	public int numColumn;
	
	public static int DOCID = 0;
	public static int KEYWORD = 1;
	public static int ENTITY =2;
	public static int TYPE = 3;
	}
