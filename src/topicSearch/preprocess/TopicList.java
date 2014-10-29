package topicSearch.preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import entitySearch.Configure;

public class TopicList {
	HashSet<String> topics;
	HashMap<String,Integer> ids;
	public TopicList(String file) {
		topics = new HashSet<String>();
		try {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = "";
		while((temp = reader.readLine()) != null) {
			String[] args = temp.split("\t");
			for (String str : args) {
				topics.add(str);
			}
		}
		ids = new HashMap<String,Integer>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void produceList(String file) {
		try {
			PrintWriter printer = new  PrintWriter(new FileWriter(file));
			ArrayList<String> list = new ArrayList<String>(topics);
			int i = 0;
			for (String str : list) {
				printer.println(str);
				ids.put(str, i++);
			}
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		TopicList tl = new TopicList(Configure.twitterTopics);
		tl.produceList(Configure.twitterTopics+ "id");
	}
	
}
