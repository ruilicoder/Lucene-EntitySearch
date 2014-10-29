package topicSearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TopicIDIndex {
	static HashMap<String, Integer> map;
	static HashMap<Integer,String> dic;
	public TopicIDIndex(String file) {
		if (map == null) {
			map = new HashMap<String,Integer>();
			dic = new HashMap<Integer,String>();
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp ;
			int i = 0;
			while((temp = reader.readLine()) != null) {
				map.put(temp, i);
				dic.put(i, temp);
				i++;
			} 
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Integer getEntityID(String t) {
		if (map.containsKey(t)) {
			return map.get(t);
		}
		return -1;
	}
	public String getTopic(Integer id) {
		return dic.get(id);
	}
	
}
