package util;

import java.util.Comparator;
import java.util.HashMap;

public class HashTableComparator implements Comparator{
	HashMap dic;
	public HashTableComparator(HashMap dic) {
		this.dic = dic;
	}
	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		Integer i1 =  (Integer) dic.get(arg0);
		Integer i2 = (Integer) dic.get(arg1);
		if (i1 < i2) return 1;
		if (i1 > i2) return -1;
		return 0;
	}
	
}
