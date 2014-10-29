package topicSearch.preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import topicSearch.index.TopicIDIndex;
import entitySearch.Configure;

public class TransferToIDList {
	public static void main(String[] args) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(Configure.twitterTopics));
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.twitterTopicsID));
		TopicIDIndex tii = new TopicIDIndex(Configure.topicList);
		String temp ;
		while((temp =reader.readLine())!= null) {
			String[] temps = temp.split("\t");
			for (String str: temps) {
				int id = tii.getEntityID(str);
				if (id!=-1) {
					printer.print(id + "\t");
				}
			}
			printer.println();
		}
		printer.close();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}