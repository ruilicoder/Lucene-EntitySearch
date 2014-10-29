	package topicSearch.index;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

import topicSearch.analyzer.TwitterAnalyzer;
import entitySearch.Configure;

public class DocTopicIndex {
public static String index = "DocumentTopicIndex";
	public DocTopicIndex(String indexDir) {
		if (reader == null)
		try {
		reader = IndexReader.open(indexDir + index);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public int getNumDocs() {
	  return reader.numDocs();
	}
	
	static IndexReader reader;
	
	public ArrayList<Integer> getTopics(int docID) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
		Document doc = reader.document(docID);
		String content = doc.get(Fields.TOPIC);
		//System.out.println(docID + "\t" +  content);
		if (content!= null && content.compareTo("") != 0) {
			 String[] entities = content.split("\t");
			 for (String e : entities) {
				 list.add(Integer.parseInt(e));
			 }
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public HashMap<Integer,Integer> getToics (ArrayList<Integer> docs) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (Integer docID : docs) {
			ArrayList<Integer> entites = getTopics(docID);
			for (Integer e : entites) {
				if (map.keySet().contains(e)) {
					int freq = map.get(e);
					map.put(e, freq + 1);
				} else {
					map.put(e, 1);
				}
			}
		}
		return map;
	}
	public static void insert(String fromFile, String toDir) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(fromFile));
		int docNum =  0;
		String temp = "";
		IndexWriter writer= new IndexWriter(toDir + index, new TwitterAnalyzer());
		
		writer.setRAMBufferSizeMB(500);
		writer.setMaxBufferedDocs(500000);
		writer.setMergeFactor(20000);
		while((temp= reader.readLine()) != null) {
			docNum++;
			Document doc = buildDocument(temp);
			writer.addDocument(doc);
			//if (docNum % 1000 == 0) System.out.println(docNum);
			if (docNum >=  Configure.updateCounts) {
				break;
			}
		}
		writer.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void buildIndex(String fromFile, String toDir) {

		try {
		BufferedReader reader = new BufferedReader(new FileReader(fromFile));
		int docNum =  0;
		String temp = "";
		IndexWriter writer= new IndexWriter(toDir + index, new TwitterAnalyzer());
		
		writer.setRAMBufferSizeMB(500);
		writer.setMaxBufferedDocs(500000);
		writer.setMergeFactor(20000);
		while((temp= reader.readLine()) != null) {
			String content  = temp;
			Document doc = buildDocument(content);
			writer.addDocument(doc);
			if (docNum++ > Configure.DOCNUM) {
				break;
			}
			if (docNum % 50000 == 0) System.out.println(docNum);
			
		}
		writer.close();	} catch (IOException e) {
			e.printStackTrace();
		}
}

	private static Document buildDocument(String content) {
		Document doc = new Document();
		doc.add(new Field(Fields.TOPIC, content, Field.Store.YES, Field.Index.NO, Field.TermVector.NO));		
		return doc;
	}
	public static void main (String[] args) {
		//DocumentEntityIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		DocTopicIndex dei = new DocTopicIndex(Configure.indexDir);
		try  {
			int numDoc = dei.reader.numDocs();
			System.out.println(numDoc);
			for (int i = 0 ; i < numDoc; i++) {
				Document doc = dei.reader.document(i);
				dei.getTopics(i);
		//	System.out.println(doc.get(Fields.TOPIC));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}




