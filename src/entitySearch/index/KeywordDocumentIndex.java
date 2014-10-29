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
import entitySearch.preprocess.TextReader;

public class KeywordDocumentIndex {
	public static String index = "basicKeywordIndex";
	public static IndexReader reader;

	public KeywordDocumentIndex() {
		if (reader == null)
			try {
				reader = IndexReader.open(Configure.indexDir + index);

			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void insertDocuments(String toDir) {
		IndexWriter writer;

		try {

			writer = new IndexWriter(toDir + index, new EntityAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			TextReader reader = new TextReader(Configure.docDir);
			int docNum = 0;
			while (reader.nextFile()) {
				while (reader.nextDoc()) {
					String content = reader.getDocContent();
					// System.out.println(content);
					Document doc = buildDocument(content);
					if (docNum % 1000 == 0)
						System.out.println(docNum);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> getDocumentIDs(String term) {
		Term t = new Term(Fields.TEXT, term);
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

	public ArrayList<Integer>[] getDocumentIDs(String[] keywords) {
		ArrayList<Integer>[] arrayList = new ArrayList[keywords.length];
		for (int i = 0; i < keywords.length; i++) {
			String keyword = keywords[i];
			ArrayList<Integer> list = this.getDocumentIDs(keyword);
			arrayList[i] = list;
			// System.out.println(list.size());
		}
		return arrayList;
	}

	public int getNumDocs() {
	  return reader.numDocs();
	}
	public static void insert(String fromDir, String toDir) {
		IndexWriter writer;

		try {

			writer = new IndexWriter(toDir + index, new EntityAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			BufferedReader reader = new BufferedReader(new FileReader(fromDir));
			int docNum = 0;
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				if (docNum >= Configure.updateCounts) {
					break;
				}
				Document doc = buildDocument(temp);

				writer.addDocument(doc);
				docNum++;
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

			writer = new IndexWriter(toDir + index, new EntityAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			TextReader reader = new TextReader(Configure.docDir);
			int docNum = 0;
			while (reader.nextFile()) {
				while (reader.nextDoc()) {
					String content = reader.getDocContent();
					// System.out.println(content);
					Document doc = buildDocument(content);
					if (docNum % 1000 == 0)
						System.out.println(docNum);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Document buildDocument(String content) {
		
		
		Document doc = new Document();
		doc.add(new Field(Fields.TEXT, content, Field.Store.NO,
				Field.Index.TOKENIZED, Field.TermVector.YES));
		return doc;
	}

}
