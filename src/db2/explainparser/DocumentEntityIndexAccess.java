package db2.explainparser;

import java.util.ArrayList;

import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;

public class DocumentEntityIndexAccess extends AccessNode {
	DocumentEntityIndex dei;
	public DocumentEntityIndexAccess() {
		dei = new DocumentEntityIndex(Configure.indexDir);
	}
	
	public int key ;
	public 	ArrayList<Result> getResults(int docID) {
		ArrayList<Integer> list =  dei.getEntites(docID);
		ArrayList<Result> results = new ArrayList<Result>();
		for (int i = 0; i < list.size(); i++) {
			Result r = new Result(list.get(i));
			results.add(r);
		}
		return results;
	}
	
}
