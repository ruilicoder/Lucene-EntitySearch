package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Counter extends HashMap<String,Integer>{
	private static final long serialVersionUID = 1L;
	int total;
	public Counter(){
		total=0;
	}
	public void addTerm(String word, int increment){
		Integer freq=get(word);
		if (freq==null){
			freq=0;
		}
		put(word, freq+increment);		
		total+=increment;
	}
	public void addTerm(String word){
		addTerm(word,1);
	}
	public void add(Object term){
		addTerm(term.toString(), 1);
	}
	public PairList<String,Integer> getSortedList(){
		PairList<String,Integer> list=new PairList<String,Integer>(this);
		list.sort(PairList.SECOND_DESC);
		return list;
	}
	public int getFrequence(String word){
		Integer freq=get(word);
		if (freq==null) freq=0;
		return freq;
	}
	public int total(){
		return total;		
	}
	
	public void truncate(int num){
		if (size()<num) return;
		PairList<String,Integer> result=getSortedList();
		total=0;
		clear();
		for (int i=0;i<result.size() && i<num;i++){
			total+=result.getSecond(i);
			put(result.getFirst(i),result.getSecond(i));
		}
	}

}
