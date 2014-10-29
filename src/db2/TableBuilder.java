package db2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.lucene.index.IndexReader;

import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.Fields;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.KeywordsList;

public class TableBuilder {
	public TableBuilder() {
		
	}
	public void buildKeywordDocumentTable()  {
		try {
		IndexReader reader  = IndexReader.open(Configure.indexDir + KeywordDocumentIndex.index);
		System.out.println(reader.numDocs());
		KeywordsList keywordsList = new KeywordsList(reader, Fields.TEXT);
		System.out.println(keywordsList.keywords.size());
		keywordsList.buildKeywordDocumentTable(reader, keywordsList.keywords.size());
		} catch (IOException e ) {
			e.printStackTrace();
		}
	}
	
	public void buildEntityTypeTable() {
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		eti.produceTable();
	}
	
	public void buildEntityDocumentTable(String fromDir, String toDir) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(fromDir + "id.txt"));
		PrintWriter printer = new PrintWriter (new FileWriter(toDir));
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
		}
		printer.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String [] args ) {
		TableBuilder b = new TableBuilder();
		b.buildEntityDocumentTable(Configure.entitydocumentDir, "D:\\sigmodexp\\entitySearch\\entityDocument.txt");
		b.buildKeywordDocumentTable();
		b.buildEntityTypeTable();
	}
}
