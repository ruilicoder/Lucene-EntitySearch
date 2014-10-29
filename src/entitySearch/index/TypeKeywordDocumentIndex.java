package entitySearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;

import entitySearch.Configure;
import entitySearch.index.analyzer.EntityAnalyzer;
import entitySearch.index.analyzer.TypeTokenizer;
import entitySearch.preprocess.TextReader;

public class TypeKeywordDocumentIndex {
	public static String index = "typeKeywordDocumentIndex";
	public IndexReader reader;
	public TypeKeywordDocumentIndex(String indexDir) {
		try {
			reader = IndexReader.open(indexDir + index);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> getDocumentIDs(String term, int type) {
		Term t= new Term (String.valueOf(type), term);	
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			TermDocs td = reader.termDocs(t);
			while(td.next()) {
				int doc = td.doc();
				list.add(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	

	public ArrayList<Integer>[] getDocumentIDs(String[] keywords, int type) {
		ArrayList<Integer>[] arrayList = new ArrayList[keywords.length];
		for (int i =0; i < keywords.length; i++) {
			String keyword = keywords[i];
			ArrayList<Integer> list = this.getDocumentIDs(keyword,type);
			arrayList[i]= list;
				}
		return arrayList;
	}
	
	public int getDocumentIDs(String[] keywords, int type, ArrayList<Integer>[] arrayList) {
		int min = Integer.MAX_VALUE;
		int index   =  -1;
		for (int i =0; i < keywords.length; i++) {
			String keyword = keywords[i];
			ArrayList<Integer> list = this.getDocumentIDs(keyword,type);
			arrayList[i]= list;
			if (list.size() < min) {
				min = list.size();
				index = i;
			}
		}
		return index;
	}
	public static void buildIndex(String fromDir1, String fromDir2, String toDir) {
		//File indexFile = new File(fromDir);
		IndexWriter writer; 
		
		try {
		
		writer= new IndexWriter(toDir + index, new EntityAnalyzer());
		
		writer.setRAMBufferSizeMB(500);
		writer.setMaxBufferedDocs(500000);
		writer.setMergeFactor(20000);
		TextReader reader = new TextReader(fromDir1);
		BufferedReader entityReader = new BufferedReader(new FileReader(fromDir2));
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		int docNum =  0;
		while (reader.nextFile()) {
			while(reader.nextDoc()) {
				String content = reader.getDocContent();
				String entities = entityReader.readLine();
				//System.out.println(content);			
				Document doc = buildDocument(content, entities, eti);
				if (docNum % 1000 == 0) System.out.println(docNum);
				if (docNum++ > Configure.DOCNUM) {
					break;
				}
				writer.addDocument(doc);
			}
			if (docNum > Configure.DOCNUM) {
				break;
			}
		}
		writer.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void insert(String fromDir1, String fromDir2, String toDir, EntityTypeIndex eti) {
		//File indexFile = new File(fromDir);
		IndexWriter writer; 
		
		try {
		
		writer= new IndexWriter(toDir + index, new EntityAnalyzer());
		
		writer.setRAMBufferSizeMB(500);
		writer.setMaxBufferedDocs(500000);
		writer.setMergeFactor(20000);
		BufferedReader reader1 = new BufferedReader(new FileReader(fromDir1));
		BufferedReader reader2 = new BufferedReader(new FileReader(fromDir2));
		int docNum =  0;
		for (docNum = 0; docNum < Configure.updateCounts; docNum++) {
			String content = reader1.readLine();
			String entities = reader2.readLine();
			if (content == null || entities == null) {
				break;
			}
			Document doc = buildDocument(content, entities, eti);
			writer.addDocument(doc);			
		}
		writer.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumDocs() {
	  return reader.numDocs();
	}
	private static Document buildDocument(String content, String entities, EntityTypeIndex eti) {
		if (entities.compareTo("") == 0) return new Document();
		TypeTokenizer et = new TypeTokenizer(entities,eti);
		
		Document doc = new Document();
		while(true) {
			Token t;
			try {
				t = et.next();
				if (t == null) break;
				String type = t.termText();
				doc.add(new Field(type, content, Field.Store.NO, Field.Index.TOKENIZED, Field.TermVector.NO));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 }
		return doc;
		
	}
}
