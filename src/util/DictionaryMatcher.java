package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DictionaryMatcher extends HashMap<String, DictionaryMatcher.Status>{
	public DictionaryMatcher(Collection<String> list){
		for (String item:list){
			String[] strs=item.split(" ");
			String str="";
			for (int i=0;i<strs.length;i++){
				if (i>0) str+=" ";
				str+=strs[i];
				Status s=get(str);
				if (s==null) {
					s=new Status();
					put(str, s);
				}
				if (i==strs.length-1) s.match=true;
				else s.prefix=true;
			}
		}
	}
	public class Status{
		public boolean prefix=false;
		public boolean match=false;
	}
}

