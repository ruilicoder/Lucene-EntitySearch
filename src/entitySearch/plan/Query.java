package entitySearch.plan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;

import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.Fields;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.KeywordsList;
import entitySearch.index.analyzer.EntityAnalyzer;

public class Query {
	public String[] keywords;
	public int type;
	
	public Query(String[] keywords, int  type) {
		this.keywords = keywords;
		this.type = type;
		//this.eti = eti;
		
	}
	public String toString() {
		String temp  = "";
		for (String key :keywords) {
			temp = temp + key + " ";
		}
		temp +=  type;
		return temp;
	}
	
	
	public String transferToSQL() {
		String subsql = "select docID from keywordDocument where keyword = ";
		String subsql1 = "select T1.entityID, T2.docID from EntityEntityType as T1, EntityDocument as T2  where T1.entityID = T2.entityID and T1.TypeID= " + type + 
				" and T2.docID in";
		
		
		String temp = "";
		boolean begin = true;
		for (String keyword :  keywords) {
			if (begin) {
			temp = subsql + "'" + keyword + "' "; 
			begin = false;
			}else 
				temp = temp + "  intersect " + subsql + "'"+keyword + "' ";
		}
		
		String sql = subsql1 + " ( " + temp + " )";
		return sql;
	}
	public String transferToKDSQL() {
		String subsql1 = "select docID from keywordDocument where keyword = ";
		String subsql2 = "select entityID from EntityEntityType where typeID = " + type; 
		
	//	String subsql = "select T3.entityID, T3.docID from EntityEntityType as T1, EntityDocument as T2  where T1.entityID = T2.entityID and T1.TypeID= " + type + 
	//			" and T2.docID in";
		
		
		String temp = "";
		boolean begin = true;
		for (String keyword :  keywords) {
			if (begin) {
			temp = subsql1 + "'" + keyword + "' "; 
			begin = false;
			}else 
				temp = temp + "  intersect " + subsql1 + "'"+keyword + "' ";
		}
		
		String sql = "select T3.entityID, T3.docID from " + "( " + temp + ") as T1, ( " + subsql2 + " ) as T2, " + "  EntityDocument as T3 where T1.docID = T3.docID and T3.entityID = T2.entityID" ;
		return sql;
	}

	
	
/* 	select T1.entityID, T2.docID from EntityEntityType as T1, EntityDocument as T2  where T1.entityID = T2.entityID and T1.TypeID= 15 and T2.docID in (
			
 	    	select docID from V1K where keyword = 'the' and V1K.TypeID = 15  intersect select docID from V1K where keyword = 'comput' and V1K.TypeID = 15 );    */
    public String tranferTempPlan1() {
    	String minkeyword = Statistic.getStat().min(keywords);
    	int typeID = type;
    	String subsql = "select docID from V1K where keyword = ";
		String subsql1 = "select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= "+ typeID + " and V2.keyword='" + minkeyword + "' and V2.docID in";
    	String temp = "";
		boolean begin = true;
		for (String keyword :  keywords) {
			if (begin) {
			temp = subsql + "'" + keyword + "'  and V1K.TypeID =" +  typeID ; 
			begin = false;
			}else 
				temp = temp + "  intersect " + subsql + "'"+keyword + "' and V1K.TypeID =" +  typeID ;
		}
    			
		String sql = subsql1 + " ( " + temp + " )";
		return sql;
    }
    
 /* select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= 15 and V2.keyword='comput' intersects  select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= 15 and V2.keyword='fun'  
  * */
    	    
    
    public String tranferTempPlan2() {
    	int typeID = type;
    	String subsql = "select V2.entityID, V2.docID from V2KT as V2 where V2.typeID= "+ typeID + " and V2.keyword=";  
    	String temp = "";
		boolean begin = true;
		for (String keyword :  keywords) {
			if (begin) {
			temp = subsql + "'" + keyword + "' "; 
			begin = false;
			}else 
				temp = temp + "  intersect " + subsql + "'"+keyword + "' ";
		 }
    			
		String sql = temp;
		return sql;
    }
    

    /* 
      select V3.entityID, V3.docID from V4TD as V3  where V3.typeID = 15 and V3.docID in (
			
 	    	select docID from V1K where keyword = 'the' and V1K.TypeID = 15  intersect select docID from V1K where keyword = 'comput' and V1K.TypeID = 15 ); */
  
     public String tranferTempPlan3() {
     	String minkeyword = Statistic.getStat().min(keywords);
     	int typeID = type;
     	String subsql = "select docID from V1K where keyword = ";
 		String subsql1 = "select V3.entityID, V3.docID from V4TD as V3  where V3.typeID = "+ typeID +  " and V3.docID in";
     	String temp = "";
 		boolean begin = true;
 		for (String keyword :  keywords) {
 			if (begin) {
 			temp = subsql + "'" + keyword + "'  and V1K.TypeID =" +  typeID ; 
 			begin = false;
 			}else 
 				temp = temp + "  intersect " + subsql + "'"+keyword + "' and V1K.TypeID =" +  typeID ;
 		}
     			
 		String sql = subsql1 + " ( " + temp + " )";
 		return sql;
     }
/*     select V5.entityID, V5.docID from V5TD as V5 where V5.TypeID= 15 and V5.docID in 
    	( select docID from keywordDocument where keyword = 'comput'   intersect select docID from keywordDocument where keyword = 'the'  ); */


     public String tranferTempPlan4() {
     	int typeID = type;
     	String subsql = "select docID from keywordDocument where keyword = ";
 		String subsql1 = "select V5.entityID, V5.docID from V5T as V5 where V5.TypeID="+ typeID ;
     	String temp = "";
 		boolean begin = true;
 		for (String keyword :  keywords) {
 			if (begin) {
 			temp = subsql + "'" + keyword + "'" ; 
 			begin = false;
 			}else 
 				temp = temp + "  intersect " + subsql + "'"+keyword + "'" ;
 		}
     			
 		String sql = "select T1.entityID, T1.docID from "  + "( " + subsql1 +" ) as T1, " +  " ( " + temp + " ) as T2 where T1.docID = T2.docID"  ;
 		return sql;
     }
     
     public String tranferTempPlan5() {
      	int typeID = type;
      	String subsql = "select docID from keywordDocument where keyword = ";
  		String subsql1 = "select V3.entityID, V3.docID from V4TD as V3  where V3.typeID = "+ typeID +  " and V3.docID in";
      	String temp = "";
  		boolean begin = true;
  		for (String keyword :  keywords) {
  			if (begin) {
  			temp = subsql + "'" + keyword + "'" ; 
  			begin = false;
  			}else 
  				temp = temp + "  intersect " + subsql + "'"+keyword + "'" ;
  		}
      			
  		String sql = subsql1 + " ( " + temp + " )";
  		return sql;
      }  
       
  /*   select docID,V8.entityID from , ( select docID, entityID from V6K where keyword ='compute' intersect select docID, entityID from V6K where keyword ='compute') as temp where temp.entityID  =V8.entityID and V8.typeID =1 */
     public String tranferTempPlan6() {
      	int typeID = type;
      	String subsql = "select docID, entityID from V6K where keyword = ";
  		String subsql1 = "( select entityID from entityentitytype where typeID =" + typeID + ") as V8";
      	String temp = "";
  		boolean begin = true;
  		for (String keyword :  keywords) {
  			if (begin) {
  			temp = subsql + "'" + keyword + "'" ; 
  			begin = false;
  			}else 
  				temp = temp + "  intersect " + subsql + "'"+keyword + "'" ;
  		}
      			
  		String sql = "select docID, V8.entityID from  " +  subsql1 + ", (" + temp + ")as temp where V8.entityID = temp.entityID";
  		return sql;
      }  
    
	public static ArrayList<Query> loadQuery (String dir) {
		ArrayList<Query> queries = new ArrayList<Query>();
		try {
		BufferedReader reader = new BufferedReader(new FileReader(dir ));
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		String temp = "";
		while ((temp = reader.readLine()) != null ) {
			//System.out.println(temp);
			String[] args = temp.split("\t");
			EntityAnalyzer analyzer = new EntityAnalyzer();
			TokenStream ts = analyzer.reusableTokenStream("text", new StringReader(args[0]));
			//String[] keywords = args[0].split(" ");
			ArrayList<String> list = new ArrayList<String>();
			Token t ;
			while((t=ts.next()) !=null) {
				list.add(t.termText());
			//	System.out.println(t.termText());
			}
			String[]keywords = new String[list.size()];
			keywords = list.toArray(keywords);
			int type = eti.typeIDMap.get(args[1]);
			Query query = new Query (keywords, type);
			queries.add(query);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queries;
	}
	public static void generateQueries(int number) {
		try {
		PrintWriter printer = new PrintWriter(new FileWriter("queries.txt"));
		IndexReader reader = IndexReader.open(Configure.indexDir + KeywordDocumentIndex.index);
		KeywordsList list = new KeywordsList(reader, Fields.TEXT) ;
		Random rand = new Random();
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		for (int i = 0; i < number; i++) {
			int n = rand.nextInt(10) + 1;
			if (n < 7) {
				n = rand.nextInt(3) + 1 ;
			} else {
				n = rand.nextInt(7) + 3;
			}
			for (int j = 0; j < n; j ++) {
				int  n1 = rand.nextInt(10) + 1;
				if (n1 < 8) {
					n1 = rand.nextInt(1000) + 1;
				} else {
					n1 = rand.nextInt(list.keywords.size() - 1000) + 1000;
				}
				String keyword = list.keywords.get(n1);
				printer.print(keyword + " ");
			}
			// number of entity type
			n = rand.nextInt(41);
			printer.println("\t" + eti.typeList.get(n));
		}
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//Query.generateQueries(200);
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		try {
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.indexDir  + "sql.txt"));
		
		for (int i = 0; i < queries.size(); i++) {
			//System.out.println(queries.get(i).toString());
			printer.println(queries.get(i).transferToSQL() + ";");
		}
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
