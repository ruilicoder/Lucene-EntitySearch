package entitySearch.index;

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

import entitySearch.Configure;
import entitySearch.index.analyzer.EntityAnalyzer;

public class EntityDocumentIndex {
	public static String index = "entityDocumentIndex";
	public EntityDocumentIndex(String indexDir) {
		try {
		reader = IndexReader.open(indexDir);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	IndexReader reader;
	
	public ArrayList<Integer> getDocumentIDs(String term) {
		Term t= new Term (Fields.ENTITY, term);	
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
	public static void buildIndex(String fromFile, String toDir) {

		try {
		BufferedReader reader = new BufferedReader(new FileReader(fromFile));
		int docNum =  0;
		String temp = "";
		IndexWriter writer= new IndexWriter(toDir + index, new EntityAnalyzer());
		
		writer.setRAMBufferSizeMB(500);
		writer.setMaxBufferedDocs(500000);
		writer.setMergeFactor(20000);
		while((temp= reader.readLine()) != null) {
			String content  = temp;
			Document doc = buildDocument(content);
			writer.addDocument(doc);
			if (docNum % 1000 == 0) System.out.println(docNum);
			if (docNum++ > Configure.DOCNUM) {
				break;
			}
		}
		
		
} catch (IOException e) {
	e.printStackTrace();
}
		
}

	public int getNumDocs() {
	  return reader.numDocs();
	}
	private static Document buildDocument(String content) {
		Document doc = new Document();
		doc.add(new Field(Fields.ENTITY, content, Field.Store.NO, Field.Index.TOKENIZED, Field.TermVector.NO));
		return doc;
	}
}
