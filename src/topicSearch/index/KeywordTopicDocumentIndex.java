package topicSearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermPositions;

import topicSearch.analyzer.KeywordTopicAnalyzer;
import twitter.TwitterByteConvertor;
import entitySearch.Configure;

public class KeywordTopicDocumentIndex {
	IndexReader reader;

	public KeywordTopicDocumentIndex(String indexDir) {
		try {
			reader = IndexReader.open(indexDir + index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, HashSet<Integer>> getDocumentEntityPairs(String term) {
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		TermPositions tps = getDocumentPositions(term);
		// if (tps == null) System.out.println("null");
		// System.out.println("");
		try {
			while (tps.next()) {
				int doc = tps.doc();
				int freq = tps.freq();
				HashSet<Integer> set;
				// System.out.println(doc);
				if (map.containsKey(doc)) {
					set = map.get(doc);
				} else {
					set = new HashSet<Integer>();
					map.put(doc, set);
				}
				for (int i = 0; i < freq; i++) {
					try {
						int pos = tps.nextPosition();
						if (tps.isPayloadAvailable()) {
							byte data[] = new byte[tps.getPayloadLength()];
							tps.getPayload(data, 0);
							int[] values = TwitterByteConvertor
									.convertToIntArray(data);
							for (int id : values)
								set.add(id);
							break;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public TermPositions getDocumentPositions(String term) {
		// reader.termDocs(t)
		Term t = new Term(Fields.TEXT, term);
		// reader.termDocs(t)
		try {
			TermPositions tps = reader.termPositions(t);
			return tps;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public int getNumDocs() {
		return reader.numDocs();
	}

	public static String index = "keywordDocumentEntityIndex";

	public static void buildIndex(String fromFile, String fromFile2,
			String toDir) {
		try {
			BufferedReader reader1 = new BufferedReader(
					new FileReader(fromFile));
			BufferedReader reader2 = new BufferedReader(new FileReader(
					fromFile2));
			String temp = "";

			IndexWriter writer = new IndexWriter(toDir + index,
					new KeywordTopicAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);

			for (int i = 0; i < Configure.DOCNUM; i++) {
				String content = reader1.readLine();
				String topics = reader2.readLine();
				Document doc = buildDocument(content, topics);
				if (i%50000 == 0) System.out.println(i);
				writer.addDocument(doc);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	}

	public static void insert(String fromFile, String fromFile2, String toDir) {
		try {
			BufferedReader reader1 = new BufferedReader(
					new FileReader(fromFile));
			BufferedReader reader2 = new BufferedReader(new FileReader(
					fromFile2));
			String temp = "";

			IndexWriter writer = new IndexWriter(toDir + index,
					new KeywordTopicAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);

			for (int i = 0; i < Configure.updateCounts; i++) {
				String content = reader1.readLine();
				String entity = reader2.readLine();
				Document doc = buildDocument(content, entity);
				writer.addDocument(doc);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Document buildDocument(String content, String entity) {
		if (entity.compareTo("") == 0)
			return new Document();
		if (content.compareTo("") == 0)
			return new Document();
		Document doc = new Document();
		doc.add(new Field(Fields.TEXT, content + " entityrli " + entity,
				Field.Store.NO, Field.Index.TOKENIZED, Field.TermVector.NO));
		return doc;
	}
}
