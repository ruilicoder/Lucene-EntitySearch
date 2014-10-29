package topicSearch.preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import util.DictionaryMatcher;
import util.DictionaryMatcher.Status;
import util.StopWord;
import entitySearch.Configure;

public class TopicMatcher {
	//HashSet<String>
	private DictionaryMatcher dm;
	public TopicMatcher() {
		HashSet<String> dic = new HashSet<String>();
		try {
			System.out.println("begin loading");
			String file = "D:\\data\\enwiki-title.txt";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String temp;
			while ((temp = reader.readLine()) != null) {
				if (temp.split(" +").length>=5) continue;
				if (StopWord.isStopWords(temp)) continue;
				if (isEnglishChar(temp))
						dic.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("finish loading:" +  dic.size());
		ArrayList<String> list = new ArrayList<String>(dic);
		
		dm = new DictionaryMatcher(list);		
	}
	
	public boolean isEnglishChar(String text) {
		boolean label = true;
		for (int k=0 ; k<text.length(); k++) {				
			if (text.charAt(k)<128) {
		      //System.out.print(content.charAt(k));
			} else {
				label = false;
			}
		}
		return label;	
	}
	public ArrayList<String> matcher(String content) {
		content = content.toLowerCase();
		ArrayList<String> matched = new ArrayList<String>();
		String[] words = content.split(" +");
		for (int i = 0; i < words.length; i++) {
			Status s = dm.get(words[i]);
			if (s == null)
				continue;
			if (s.match) {
				matched.add(words[i]);
			}
			if (s.prefix) {
				String match = words[i];
				for (int j = i + 1; j < words.length; j++) {
					match = match + " " + words[j];
					Status ss = dm.get(match);
					if (ss == null)
						break;
					if (ss.match) {
						matched.add(match);
					}
					if (ss.prefix == false) {
						break;
					}
				}
			}
		}

		return matched;
	}
	
	public static void processDocumentSet(String fromDir, String toDir) {
		
		int docNum = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fromDir));
			TopicMatcher em = new TopicMatcher();
			PrintWriter printer = new PrintWriter(new FileWriter(
					toDir));
//			PrintWriter printer3 = new PrintWriter(new FileWriter(toDir +"txt.txt"));
			String content = "";
			while ((content = reader.readLine())!=null) {
					ArrayList<String> list = em.matcher(content);
					for (String entity : list) {
						printer.print(entity + "\t");
					}
					printer.println();					
					if (docNum % 1000 == 0)
						System.out.println(docNum);
					docNum++;
					// writer.addDocument(doc);
			}
		
			printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	//	EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		TopicMatcher.processDocumentSet(Configure.twitterDocs, Configure.twitterTopics);
				
	}
}
