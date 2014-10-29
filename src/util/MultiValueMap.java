package util;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiValueMap <T1, T2> extends HashMap<T1, ArrayList<T2>>{
	public MultiValueMap(){
	}
	public void add(T1 key, T2 value){
		ArrayList<T2> valueList=get(key);
		if (valueList==null){
			valueList=new ArrayList<T2>();
			put(key,valueList);
		}
		valueList.add(value);		
	}
}
