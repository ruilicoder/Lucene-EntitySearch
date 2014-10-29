package topicSearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

import topicSearch.analyzer.TwitterAnalyzer;
import entitySearch.Configure;

public class DocumentTimeIndex {
	public static String index = "DocumentTimeIndex";
	public IndexReader reader;
	public DocumentTimeIndex() {
		if (reader == null)
			try {
				reader = IndexReader.open(Configure.indexDir + index);

			} catch (IOException e) {
				e.printStackTrace();
			}
	
	}
	
	

	public static void insertDocuments(String toDir) {
		IndexWriter writer;

		try {

				writer = new IndexWriter(toDir + index, new TwitterAnalyzer());
				writer.setRAMBufferSizeMB(500);
				writer.setMaxBufferedDocs(500000);
				writer.setMergeFactor(20000);
				BufferedReader reader = new BufferedReader(new FileReader(Configure.twitterTime));
				int docNum = 0;
				String temp = "";
				while ((temp = reader.readLine()) != null) {
					Document doc = buildDocument(temp);
					writer.addDocument(doc);
					docNum++;
					if (docNum > Configure.updateCounts) break;
				}
				writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Integer getTimeID(int docID) {
		try {
		Document doc = reader.document(docID);
		String id = doc.get(Fields.TIME);
		return Integer.parseInt(id);
		} catch (IOException e) {
			
		}
		return -1;
	}	
	public int getNumDocs() {
		  return reader.numDocs();
		}
	public static void buildIndex(String fromFile, String toDir) {
		IndexWriter writer;

		try {
			writer = new IndexWriter(toDir + index, new TwitterAnalyzer());
			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			BufferedReader reader = new BufferedReader(new FileReader(Configure.twitterTime));
			int docNum = 0;
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				Document doc = buildDocument(temp);
				writer.addDocument(doc);		
				if (docNum %50000 == 0) System.out.println(docNum);
				docNum++;
				if (docNum> Configure.DOCNUM) {
					break;
				}
			}
			writer.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static Document buildDocument(String content) {
		Document doc = new Document();
		doc.add(new Field(Fields.TIME, content, Field.Store.YES, Field.Index.NO, Field.TermVector.NO));		
		return doc;
	}
}
