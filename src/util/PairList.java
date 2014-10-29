package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class PairList<T1,T2> extends ArrayList<Pair<T1,T2>>{
	public static final int FIRST_ASC=0;
	public static final int FIRST_DESC=1;
	public static final int SECOND_ASC=2;
	public static final int SECOND_DESC=3;
	public PairList(){	
	}
	public PairList(HashMap<T1,T2> map){
		for (T1 k:map.keySet()){
			add(k,map.get(k));			
		}
	}
	public void add(T1 t1, T2 t2){
		add(new Pair<T1,T2>(t1,t2));
	}
	public void sort(int option){
		if (size()==0) return;
		T1 test1=get(0).first;
		T2 test2=get(0).second;
		if ((option==FIRST_ASC || option==FIRST_DESC) && !(test1 instanceof Comparable)) return;
		if ((option==SECOND_ASC || option==SECOND_DESC) && !(test2 instanceof Comparable)) return;
		Collections.sort(this,new PairComparator(option));		
	}
	public T1 getFirst(int o){
		return get(o).first;
	}
	public T2 getSecond(int o){
		return get(o).second;
	}

	class PairComparator implements Comparator<Pair<T1,T2>>{
		int option;
		public PairComparator(int option){
			this.option=option;
		}
		public int compare(Pair<T1, T2> o1, Pair<T1, T2> o2) {
			switch (option){
			case FIRST_ASC:
				return ((Comparable)o1.first).compareTo((Comparable)o2.first);
			case FIRST_DESC:
				return -((Comparable)o1.first).compareTo((Comparable)o2.first);
			case SECOND_ASC:
				return ((Comparable)o1.second).compareTo((Comparable)o2.second);
			case SECOND_DESC:
				return -((Comparable)o1.second).compareTo((Comparable)o2.second);
			}
			return 0;
		}
	}
}
