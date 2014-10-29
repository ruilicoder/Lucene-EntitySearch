package db2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import entitySearch.plan.Query;
import entitySearch.Configure;

public class PlanGenerator {
	public static String transferToExplain(String query, String location) {
		String  explain = "db2expln -database topic3 -statement \"" + query +  "\" -graph -output " 
				+ location;
		return explain;
	}
	
	
    public static void main(String[] args) {
    	ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		try {
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.indexDir + "plan6.txt"));
		
		/* for (int i = 0; i < queries.size(); i++) {
			//System.out.println(queries.get(i).toString());
			printer.println(transferToExplain(queries.get(i).transferToSQL(), Configure.indexDir + "explain"+ i));
		} */
		/* for (int i = 0; i < queries.size(); i++) {
			//System.out.println(queries.get(i).toString());
			printer.println(queries.get(i).tranferTempPlan2());
		} */
		
	/*	for (int i = 0; i < queries.size(); i++) {
			printer.println(queries.get(i).tranferTempPlan2());
		}*/
/*		for (int i = 0; i < queries.size(); i++) {
			printer.println(queries.get(i).tranferTempPlan3());
		}*/
		
	/*	for (int i = 0; i < queries.size(); i++) {
			printer.println(queries.get(i).tranferTempPlan4());
		} */
	/*	for (int i = 0; i < queries.size(); i++) {
			printer.println(queries.get(i).tranferTempPlan5());
		}*/
		for (int i = 0; i < queries.size(); i++) {
			printer.println(queries.get(i).tranferTempPlan6());
		}
		
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
/* 	select T1.entityID, T2.docID from EntityEntityType as T1, EntityDocument as T2  where T1.entityID = T2.entityID and T1.TypeID= 15 and T2.docID in (
			
 	    	select docID from V1K where keyword = 'the' and V1K.TypeID = 15  intersect select docID from V1K where keyword = 'comput' and V1K.TypeID = 15 );    */
    public static String tranferTempPlan1(String[] keywords, String typeID) {
    	
    	String subsql = "select docID from V1K where keyword = ";
		String subsql1 = "select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= "+ typeID + "and V2.keyword='" + "comput" + "' and V2.docID in";
    	String temp = "";
		boolean begin = true;
		for (String keyword :  keywords) {
			if (begin) {
			temp = subsql + "'" + keyword + "'  and V1K.TypeID =" +  typeID ; 
			begin = false;
			}else 
				temp = temp + "  intersect " + subsql + "'"+keyword + "and V1K.TypeID =" +  typeID ;
		}
    			
		String sql = subsql1 + " ( " + temp + " )";
		return sql;
    }
   /*
    select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= 15 and V2.keyword='comput' intersects  select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= 15 and V2.keyword='fun'  
    
   */
}
