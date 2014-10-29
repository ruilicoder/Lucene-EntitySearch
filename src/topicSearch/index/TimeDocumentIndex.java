package topicSearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;

import topicSearch.analyzer.TwitterAnalyzer;
import entitySearch.Configure;

public class TimeDocumentIndex {
	public static String index = "timeDocumentIndex";
	public IndexReader reader;
	public TimeDocumentIndex() {
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

	public ArrayList<Integer> getDocumentIDs(String term) {
		Term t = new Term(Fields.TIME, term);
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			TermDocs td = reader.termDocs(t);
			while (td.next()) {
				int doc = td.doc();
				list.add(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
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
		doc.add(new Field(Fields.TIME, content, Field.Store.NO, Field.Index.UN_TOKENIZED, Field.TermVector.NO));		
		return doc;
	}
}
