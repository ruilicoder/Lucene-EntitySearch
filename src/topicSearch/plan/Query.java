package topicSearch.plan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import org.apache.lucene.index.IndexReader;

import topicSearch.index.Fields;
import topicSearch.index.KeywordDocumentIndex;
import entitySearch.Configure;
import entitySearch.index.KeywordsList;

public class Query {
	public String[] keywords;
	public int  time;
	public Query(String[] keywords, int  time) {
		this.keywords = keywords;
		this.time = time;
		//this.eti = eti;
		
	}
	public String toString() {
		String temp  = "";
		for (String key :keywords) {
			temp = temp + key + " ";
		}
		temp +=  time;
		return temp;
	}
	
	
	public String transferToSQL() {
		String subsql = "select docID from keywordDocument where keyword = ";
		String subsql1 = "select T2.topic, T1.docID from TimeDocument as T1, TopicDocument as T2  where T1.docID = T2.docID and T1.time = " + time + 
				" and T2.docID in";
		
		
		String temp = "";
		boolean begin = true;
		for (String keyword :  keywords) {
			keyword = keyword.replaceAll("'", "''");
			if (begin) {
			temp = subsql + "'" + keyword + "' "; 
			begin = false;
		
			}else 
				temp = temp + "  intersect " + subsql + "'"+keyword + "' ";
		}
		
		String sql = subsql1 + " ( " + temp + " )";
		return sql;
	} 
	public static ArrayList<Query> loadQuery (String dir) {
		ArrayList<Query> queries = new ArrayList<Query>();
		try {
		BufferedReader reader = new BufferedReader(new FileReader(dir ));
		
		String temp = "";
		while ((temp = reader.readLine()) != null ) {
			String[] args = temp.split("\t");
			String[]keywords = args[0].split(" ");			
			int type = Integer.parseInt(args[1]);
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
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.indexDir + "queries.txt"));
		IndexReader reader = IndexReader.open(Configure.indexDir + KeywordDocumentIndex.index);
		KeywordsList list = new KeywordsList(reader, Fields.TEXT) ;
		Random rand = new Random();
//		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		for (int i = 0; i < number; i++) {
			int n = rand.nextInt(10) + 1;
			if (n < 8) {
				n = rand.nextInt(3) + 1 ;
			} else {
				n = rand.nextInt(7) + 3;
			}
			for (int j = 0; j < n; j ++) {
				int  n1 = rand.nextInt(10) + 1;
				if (n1 < 8) {
					n1 = rand.nextInt(4000) + 1;
				} else {
					n1 = rand.nextInt(list.keywords.size() - 4000) + 4000;
				}
				String keyword = list.keywords.get(n1);
				printer.print(keyword + " ");
			}
			// number of entity type
			n = rand.nextInt(31) + 20090900;
			printer.println("\t" + n );
		}
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
	//Query.generateQueries(1000);
	//ArrayList<Query> list =  Query.loadQuery(Configure.indexDir + "queries.txt");
			ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		try {
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.indexDir + "sql.txt"));
		
		for (int i = 0; i < queries.size(); i++) {
			System.out.println(queries.get(i).toString());
		//	printer.println(queries.get(i).transferToSQL() + ";");

			printer.println(queries.get(i).transferToSQL() + ";");
		}
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	} 
}
