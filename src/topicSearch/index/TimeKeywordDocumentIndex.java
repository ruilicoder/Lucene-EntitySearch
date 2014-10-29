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

public class TimeKeywordDocumentIndex  {
	public static String index = "timeKeywordIndex";
	static IndexReader reader;

	public TimeKeywordDocumentIndex() {
		if (reader == null)
			try {
				reader = IndexReader.open(Configure.indexDir + index);

			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public ArrayList<Integer> getDocumentIDs(String term, String time) {
		Term t = new Term(time, term);
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

	
	public int getDocumentIDs(String[] keywords, String time, ArrayList<Integer>[] arrayList) {
		int min = Integer.MAX_VALUE;
		int index   =  -1;
		for (int i =0; i < keywords.length; i++) {
			String keyword = keywords[i];
			ArrayList<Integer> list = this.getDocumentIDs(keyword,time);
			arrayList[i]= list;
			if (list.size() < min) {
				min = list.size();
				index = i;
			}
		}
		return index;
	}
	public ArrayList<Integer>[] getDocumentIDs(String[] keywords, String time) {
		ArrayList<Integer>[] arrayList = new ArrayList[keywords.length];
		for (int i = 0; i < keywords.length; i++) {
			
			String keyword = keywords[i];
			ArrayList<Integer> list = this.getDocumentIDs(keyword, time);
			arrayList[i] = list;
		
		}
		return arrayList;
	}

	public int getNumDocs() {
	  return reader.numDocs();
	}
	public static void insert(String fromDir, String toDir) {
		IndexWriter writer;

		try {

			writer = new IndexWriter(toDir + index, new TwitterAnalyzer());
			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			BufferedReader reader = new BufferedReader(new FileReader(fromDir));
			BufferedReader reader2 = new BufferedReader(new FileReader(Configure.twitterTime));
			int docNum = 0;
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				String time = reader2.readLine();
				Document doc = buildDocument(temp, time);

				writer.addDocument(doc);
				docNum++;
				if (docNum >= Configure.updateCounts) {
					break;
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void buildIndex(String toDir) {
		// File indexFile = new File(fromDir);
		IndexWriter writer;

		try {


			writer = new IndexWriter(toDir + index, new TwitterAnalyzer());
			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			BufferedReader reader = new BufferedReader(new FileReader(Configure.twitterDocs));
			BufferedReader reader2 = new BufferedReader(new FileReader(Configure.twitterTime));
			int docNum = 0;
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				String time = reader2.readLine();
				Document doc = buildDocument(temp, time);
				writer.addDocument(doc);
				if (docNum++ > Configure.DOCNUM) {
					break;
				}
				if (docNum %50000 == 0) System.out.println(docNum);
			}
			writer.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Document buildDocument(String content, String time) {		
		Document doc = new Document();
		doc.add(new Field(time, content, Field.Store.NO,
				Field.Index.TOKENIZED, Field.TermVector.NO));
		return doc;
	}
}
