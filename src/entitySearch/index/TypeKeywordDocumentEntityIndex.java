package entitySearch.index;

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

import util.ByteConvert;
import entitySearch.Configure;
import entitySearch.index.analyzer.TypeKeywordEntityAnalyzer;
import entitySearch.preprocess.TextReader;

public class TypeKeywordDocumentEntityIndex {
	public static String index = "typekeyworddocumentEntityIndex";
	IndexReader reader;
	public TypeKeywordDocumentEntityIndex(String fromDoc) {
		try {
		reader = IndexReader.open(fromDoc + index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public HashMap<Integer,HashSet<Integer>> getDocumentEntityPairs(String term, int type) {
		HashMap<Integer,HashSet<Integer>> map = new HashMap<Integer,HashSet<Integer>> ();
		TermPositions tps = getDocumentPositions(term,type);
		//if (tps == null) System.out.println("null");
		//System.out.println("");
		try {
			while (tps.next()) {
				int doc = tps.doc();
				int freq = tps.freq();
				HashSet<Integer> set;
				//System.out.println(doc);
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
							int[] values = ByteConvert.convertToIntArray(data);
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
	
	public TermPositions getDocumentPositions(String term, int type) {
		//reader.termDocs(t)
		Term t = new Term (String.valueOf(type), term);
		//reader.termDocs(t)
		try {
			TermPositions tps = reader.termPositions(t);
			return tps;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static void insert(String fromDir1, String fromDir2, String toDir, EntityTypeIndex eti) {
		IndexWriter writer;

		try {

			writer = new IndexWriter(toDir + index,
					new TypeKeywordEntityAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			BufferedReader reader1 = new BufferedReader(new FileReader(fromDir1));
			BufferedReader reader2 = new BufferedReader(new FileReader(fromDir2));
			
			int docNum = 0;
			for (docNum = 0; docNum < Configure.updateCounts; docNum++) {
				String content = reader1.readLine();
				String entities = reader2.readLine();
				Document doc = buildDocument(content, entities, eti);
				writer.addDocument(doc);
				
			}		
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void buildIndex(String fromDir1, String fromDir2, String toDir) {
		// File indexFile = new File(fromDir);
		IndexWriter writer;

		try {

			writer = new IndexWriter(toDir + index,
					new TypeKeywordEntityAnalyzer());

			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			TextReader reader = new TextReader(fromDir1);
			BufferedReader entityReader = new BufferedReader(new FileReader(
					fromDir2));
			EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
			int docNum = 0;
			while (reader.nextFile()) {
				while (reader.nextDoc()) {
					String content = reader.getDocContent();
					String entities = entityReader.readLine();
					// System.out.println(entities);
					Document doc = buildDocument(content, entities, eti);
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

	public int getNumDocs() {
	  return reader.numDocs();
	}
	private static Document buildDocument(String content, String entities,
			EntityTypeIndex eti) {
		if (entities.compareTo("") == 0)
			return new Document();
		if (content.compareTo("") == 0)
			return new Document();

		HashMap<String, String> map = new HashMap<String, String>();
		String[] args = entities.split("\t");

		for (String entity : args) {
			Integer id = Integer.parseInt(entity);
			Integer type = eti.getTypeID(id);
			if (map.containsKey(type)) {
				String str = map.get(type);
				str = str + " " + id;
				map.put(String.valueOf(type), str);
			} else {
				map.put(String.valueOf(type), String.valueOf(id));
			}
		}

		Document doc = new Document();
		for (String str : map.keySet()) {
			doc.add(new Field(str, content + " entityrli " + map.get(str),
					Field.Store.NO, Field.Index.TOKENIZED, Field.TermVector.NO));
		}

		return doc;

	}

}
