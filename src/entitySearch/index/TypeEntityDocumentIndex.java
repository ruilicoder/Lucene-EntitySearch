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
import entitySearch.index.analyzer.EntityAnalyzer;

public class TypeEntityDocumentIndex {
	public static String index = "TypeEntityDocumentIndex";
	
	IndexReader reader;
	public TypeEntityDocumentIndex(String indexDir) {
		try {
		reader = IndexReader.open(indexDir + index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumDocs() {
	  return reader.numDocs();
	}
	/*
	 * Get Document Entity Pairs for an entity type 
	 * HashMap<DocID, HashSet<EntityIDS>>
	 */
	public HashMap<Integer,HashSet<Integer>> getDocumentEntityPairs(String term) {
		HashMap<Integer,HashSet<Integer>> map = new HashMap<Integer,HashSet<Integer>> ();
		TermPositions tps = getDocumentPositions(term);
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
	
	public TermPositions getDocumentPositions(String term) {
		//reader.termDocs(t)
		Term t = new Term (Fields.TYPE, term);
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
	
	public static void buildIndex(String fromFile, String toDir) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fromFile));
			int docNum =  0;
			String temp = "";
			
			EntityTypeIndex eti  = new EntityTypeIndex(Configure.entityList);
			IndexWriter writer= new IndexWriter(toDir + index, new EntityAnalyzer(eti));
			
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
			writer.close();
			
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	
	public static void insert(String fromDir, String toDir, EntityTypeIndex eti) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fromDir));
			int docNum =  0;
			String temp = "";
			
			IndexWriter writer= new IndexWriter(toDir + index, new EntityAnalyzer(eti));
			
			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			while((temp= reader.readLine()) != null) {
				String content  = temp;
				docNum++;
				Document doc = buildDocument(content);
				writer.addDocument(doc);
				if (docNum >= Configure.updateCounts) {
					break;
				}
			}
			writer.close();
			
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	public static Document buildDocument(String content) {
		Document doc = new Document();
		doc.add(new Field(Fields.TYPE, content, Field.Store.NO, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS));
		return doc;
	}
	
	public static void main(String[] args) {
//		TypeEntityDocumentIndex tedi = new TypeEntityDocumentIndex(Co);
		TypeEntityDocumentIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		TypeEntityDocumentIndex tedi = new TypeEntityDocumentIndex(Configure.indexDir);
		
		//tedi.get
	}
}
