package entitySearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;

import entitySearch.Configure;

import util.HashTableComparator;

public class KeywordsList{
	public ArrayList<String> keywords;
	public ArrayList<Integer> frequency;
	public String field ;
	public HashSet<String> stopwords;
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
	public KeywordsList(IndexReader reader, String field) {
		this.field = field;
		
		frequency = new ArrayList<Integer>();
		TermEnum tes;
		stopwords = new HashSet<String>();
		this.LoadStopwords("D:\\SigmodExp\\stopwords.txt");
		try {
			tes = reader.terms();
			HashMap<String, Integer> dic = new HashMap<String,Integer>();
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
			keywords = new ArrayList<String>(dic.keySet());
			Collections.sort(keywords, new HashTableComparator(dic));
			for (String key : keywords) {
			//	System.out.println(key);
				frequency.add(dic.get(key));
				
			}
			System.out.println(keywords.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	public void  buildKeywordDocumentTable(IndexReader reader, int count) {
		try {
		PrintWriter printer = new PrintWriter (new FileWriter("D:\\sigmodexp\\entitySearch\\"+ "keyworddocumenttable2.txt"));
		int i = 0; 
		for (String key : keywords) {
			if (key.contains(",")) continue;
			TermDocs tds =  reader.termDocs(new Term (field,key));
			System.out.println(key);
			if (i++ > count) break;
			while (tds.next()) {
				printer.println(key+ "," +  tds.doc());
			}
		}
		printer.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
