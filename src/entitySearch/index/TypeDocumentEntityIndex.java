package entitySearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

import entitySearch.Configure;
import entitySearch.index.analyzer.EntityAnalyzer;

public class TypeDocumentEntityIndex {
	public static String index  = "TypeDocumentEntityIndex";
	
	public TypeDocumentEntityIndex(String docDir)  {
		try {
		reader = IndexReader.open(docDir + index);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	IndexReader reader;
	public ArrayList<Integer> getEntites(int docID, String type) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
		Document doc = reader.document(docID);
		String content = doc.get(type);
		//System.out.println(docID + "\t" +  content);
		if (content!= null && content.compareTo("") != 0) {
			 String[] entities = content.split(" ");
			// System.out.println(docID +  ":" + content);
				
			 for (String e : entities) {
				 list.add(Integer.parseInt(e));
			 }
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public HashMap<Integer,Integer> getEntites (ArrayList<Integer> docs, String type) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (Integer docID : docs) {
			ArrayList<Integer> entites = getEntites(docID, type);
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
	
	public static void buildIndex(String fromFile, String toDir) {

		try {
		BufferedReader reader = new BufferedReader(new FileReader(fromFile));
		int docNum =  0;
		String temp = "";
		IndexWriter writer= new IndexWriter(toDir + index, new EntityAnalyzer());
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		writer.setRAMBufferSizeMB(500);
		writer.setMaxBufferedDocs(500000);
		writer.setMergeFactor(20000);
		while((temp= reader.readLine()) != null) {
			String content  = temp;
			content = content.trim();
			Document doc = buildDocument(content,eti);
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

	public int getNumDocs() {
	  return reader.numDocs();
	}
public static void insert(String fromFile, String toDir, EntityTypeIndex eti) {

			try {
			BufferedReader reader = new BufferedReader(new FileReader(fromFile));
			int docNum =  0;
			String temp = "";
			IndexWriter writer= new IndexWriter(toDir + index, new EntityAnalyzer());
			writer.setRAMBufferSizeMB(500);
			writer.setMaxBufferedDocs(500000);
			writer.setMergeFactor(20000);
			while((temp= reader.readLine()) != null) {
				docNum ++;
				String content  = temp;
				content = content.trim();
				Document doc = buildDocument(content,eti);
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

	private static Document buildDocument(String content, EntityTypeIndex eti) {
		HashMap<Integer,String> map = new HashMap<Integer,String>();
		if (content.compareTo("") == 0) return new Document();
		String[] args = content.split("\t");
		int i = 0;
		for (String entity : args) {
			Integer id = Integer.parseInt(entity);
			Integer type = eti.getTypeID(id);

			if (map.containsKey(type)) {
				String str = map.get(type);
				str = str + " " + id;
				map.put(type, str);
			} else {
				map.put(type, String.valueOf(id));
			}
		}
		Document doc = new Document();
		for (Integer type : map.keySet()) {
			doc.add(new Field(String.valueOf(type), map.get(type), Field.Store.YES, Field.Index.NO, Field.TermVector.NO));					
		}
		return doc;
	}
	
	public static void main (String[] args) {
		//DocumentEntityIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		/*TypeDocumentEntityIndex dei = new TypeDocumentEntityIndex(Configure.indexDir);
		try  {
			int numDoc = dei.reader.numDocs();
			for (int i = 0 ; i < numDoc; i++) {
				Document doc = dei.reader.document(i);
				dei.getEntites(i);
			//	System.out.println(doc.get(Fields.ENTITY));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		String content = "6542	20080	9161	9161	15961	15587	10225	6542	30988	15587	15587	30988	15587	30988	15587	15961	15961	18689	6542	20080	20080	8386	15587	21703	618	18689	8386	8386	8386	24226	15961	21703	15961	15961	29131	618	618	5533	618	30988	618	8386	31439	21703	618	24226	15961	5154	15961	10225	15961	10225	10225	6542	7581	8969	7581	8969";
		Document doc = TypeDocumentEntityIndex.buildDocument(content, eti);
		System.out.println(doc.get("34"));

	}
}
