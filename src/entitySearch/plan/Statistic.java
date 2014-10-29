package entitySearch.plan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

import topicSearch.index.Fields;
import entitySearch.Configure;
import entitySearch.index.KeywordDocumentIndex;

public class Statistic {
	private static Statistic stat;
	public HashMap<String, Integer> dic;
	public HashSet<String> stopwords;
	public static Statistic getStat() {
		if (stat == null) {
			stat = new Statistic(Fields.TEXT);
			return stat;
		} else {
			return stat;
		}
	}
	public void LoadStopwords(String file) {
		try  {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp;
		while ((temp = reader.readLine()) !=null) {
			stopwords.add(temp);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isEnglish(String temp) {
		for (int i = 0 ; i < temp.length(); i++) {
			if (!Character.isLetter(temp.charAt(i)))
					return false;
		}
		return true;
	}
	public boolean checkwords(String word) {
		if (stopwords.contains(word)) return false;
		if (word.length() >=20 ) return false;
		return isEnglish(word);
	}
	public Statistic (String field) {
		try {			
		IndexReader reader  = IndexReader.open(Configure.indexDir + KeywordDocumentIndex.index);		
		TermEnum tes;
		stopwords = new HashSet<String>();
		this.LoadStopwords("D:\\SigmodExp\\stopwords.txt");
			tes = reader.terms();
		dic = new HashMap<String,Integer>();
			while(tes.next()) {
				Term t  = tes.term();
				if (t.field().compareTo(field) == 0) {
					String term = t.text();
					int freq = tes.docFreq();
					if (checkwords(term) && freq >=10) {
					dic.put(term, freq);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String min (String[]keywords) {
		int min = Integer.MAX_VALUE;
		String keyword ="";
		for (String key : keywords) {
			if (dic.get(key) != null && dic.get(key) < min) {
				keyword = key;
				min = dic.get(key);
			}
		}
		return keyword;
	}
}
