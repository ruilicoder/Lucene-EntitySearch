package topicSearch.preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.lucene.index.IndexReader;

import topicSearch.index.KeywordDocumentIndex;
import entitySearch.Configure;
import entitySearch.index.Fields;
import entitySearch.index.KeywordsList;

public class TableBuilder {
	public TableBuilder() {
		
	}
	public void buildKeywordDocumentTable()  {
		try {
		IndexReader reader  = IndexReader.open(Configure.indexDir + KeywordDocumentIndex.index);
		KeywordsList keywordsList = new KeywordsList(reader, Fields.TEXT);
		System.out.println(keywordsList.keywords.size());
		keywordsList.buildKeywordDocumentTable(reader, keywordsList.keywords.size());
		} catch (IOException e ) {
			e.printStackTrace();
		}
	}
	
	public void buildDocTimeTable() {
		try {
			PrintWriter printer = new PrintWriter(new FileWriter(Configure.twitterTables + "timeDoc.txt"));
			BufferedReader reader = new BufferedReader(new FileReader(Configure.twitterTime));
			String temp = reader.readLine();
			int i = 1;
			while((temp = reader.readLine()) != null) {
				printer.println(i + ", " + temp);
				i++;
				if (i > Configure.DOCNUM) break;
			}
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void buildTopicDocumentTable() {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(Configure.twitterTopicsID));
		PrintWriter printer = new PrintWriter (new FileWriter(Configure.twitterTables + "docTopic"));
		String temp = "";
		int i = 0;
		while ((temp = reader.readLine()) != null) {
			
			if (temp.compareTo("")== 0) {
				i++;
				continue;
			}
			String[] args = temp.split("\t");
			for (int j = 0 ; j < args.length; j++) {
				if (args[j].trim().compareTo("")!=0) {
					printer.println(i + ", " + args[j]);
				}
			}
			i++;
			if (i > Configure.DOCNUM) break;			
		}
		printer.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String [] args ) {
		TableBuilder b = new TableBuilder();
		b.buildTopicDocumentTable();
		//b.buildKeywordDocumentTable();
		b.buildDocTimeTable();
	}
}
