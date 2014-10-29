package db2.explainparser;

import java.util.ArrayList;

import entitySearch.index.KeywordDocumentIndex;

public class KeywordIndexAccess extends AccessNode {
	KeywordDocumentIndex kdi;
	public KeywordIndexAccess() {
		kdi = new KeywordDocumentIndex();
	}
	
	public String key;
	public  ArrayList<Result> getDocuments(String key) {
		ArrayList<Integer> list =  kdi.getDocumentIDs(key);
		ArrayList<Result> results = new ArrayList<Result>();
		for (int i = 0; i < list.size(); i++) {
			Result r = new Result(list.get(i));
			results.add(r);
		}
		return results;
	}
}
