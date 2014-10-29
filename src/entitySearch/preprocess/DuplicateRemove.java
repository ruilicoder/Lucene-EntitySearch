package entitySearch.preprocess;

import java.util.HashSet;

public class DuplicateRemove {
	public static void  remove(String content) {
		String[] args = content.split("\t");
		HashSet<String> set  = new HashSet<String>();
		for (String str : args) {
			set.add(str);
		}
		StringBuilder sb = new StringBuilder();
		for (String str : set) {
			sb.append(str + "\t");
		}
	}
}
