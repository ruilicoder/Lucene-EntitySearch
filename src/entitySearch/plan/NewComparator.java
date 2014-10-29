package entitySearch.plan;

import java.io.IOException;
import java.util.Comparator;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

import topicSearch.index.Fields;

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
			return -1;
		}
		if (num1 > num2) {
			return 1;
		}
		}catch (IOException e) {
			
		}
		return 0;
	}
	
}