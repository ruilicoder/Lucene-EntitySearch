package db2.explainparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

import topicSearch.index.Fields;
import topicSearch.index.KeywordDocumentIndex;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.EntityTypeIndex;
import entitySearch.plan.Query;

public class Execution2 {
	KeywordDocumentIndex tdi;
	EntityTypeIndex eti;
	DocumentEntityIndex dei;
	public Execution2(KeywordDocumentIndex tdi, EntityTypeIndex eti, DocumentEntityIndex dei) {
		
		this.tdi = tdi;
		this.eti = eti;
		this.dei = dei;
	}
	HashMap<Integer, Integer> res;
	public void process(Query q, String sequence) { 
		if (q.keywords.length == 1) {
			ArrayList<Integer> list = tdi.getDocumentIDs(q.keywords[0]);
			HashMap<Integer,Integer> results = dei.getEntites(list);
			ArrayList<Integer> list2 = eti.typeIndex.get(q.type);
			res = new HashMap<Integer,Integer>();
			for (Integer entity : list2) {
				if (results.containsKey(entity)) {
					res.put(entity, results.get(entity));
				}
			}
		}
		if (q.keywords.length == 2) {
		}
		
		if (q.keywords.length > 2) {
			
		}
	}
	
	
}

class  NewComparator implements Comparator  {
	public IndexReader reader;
	public NewComparator(IndexReader reader) {
		this.reader = reader;
	}
	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		String t1 = (String) arg0;
		String t2 = (String) arg1;
		try {
		int num1 = reader.docFreq(new Term(Fields.TEXT,t1));
		int num2 = reader.docFreq(new Term(Fields.TEXT,t2));
		if (num1 < num2) {
			return 1;
		}
		if (num1 > num2) {
			return -1;
		}
		}catch (IOException e) {
			
		}
		return 0;
	}
	
}