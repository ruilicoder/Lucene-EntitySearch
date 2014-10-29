package topicSearch.plan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.KeywordDocumentIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TopicIDIndex;
import util.ExcutionTimer;
import entitySearch.Configure;

public class DBPlan extends ExecutionPlan {
	KeywordDocumentIndex tdi;
	DocTopicIndex dti;
	TimeDocumentIndex tdii;
	TopicIDIndex tii;
	BufferedReader reader;
	public DBPlan(KeywordDocumentIndex tdi, DocTopicIndex dti, TimeDocumentIndex tdii, TopicIDIndex tii) { 
		this.tdi = tdi;
		this.dti  = dti;
		this.tdii = tdii;
		this.tii = tii;
		try {
		reader = new BufferedReader(new FileReader(Configure.twitterTopicsID));
		} catch (IOException e) {
			
		}
	}
	public HashMap<Integer, HashSet<Integer>> set;
	
	public void scan(HashSet<Integer> list) {
		set  = new HashMap<Integer, HashSet<Integer>>();
		
		String temp  = "";
		int i = 1; try {
		while ((temp = reader.readLine()) != null) {
			
			if (temp.compareTo("") != 0 && list.contains(i)) {
			String[] args = temp.split("\t");
			HashSet<Integer> s1 = new HashSet<Integer>();   
			for (String str : args) {
				s1.add(Integer.parseInt(str));
				set.put(i,s1);
			   }
			}
			
			i++;
		}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		DocTopicIndex dti = new DocTopicIndex(Configure.indexDir);
		TimeDocumentIndex tdi = new TimeDocumentIndex();
		TopicIDIndex tii = new TopicIDIndex(Configure.topicList);
		DBPlan bp = new  DBPlan(kdi, dti, tdi, tii);
		
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		for (int i = 0; i < 2; i++) {
			bp.processQuery(queries.get(i));	
			/*for (String str: bp.res.keySet()) {
				System.out.println(str + ":" +  bp.res.get(str));
			}*/
		}
		timer.setEnd();
		System.out.println(timer.getTime());
	}

	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		ArrayList<Integer>[] arrayList = tdi.getDocumentIDs(q.keywords);
		//ArrayList<Integer> list = tdii.getDocumentIDs(String.valueOf(q.time));
		
		//ArrayList<Integer>[] resList = new ArrayList[arrayList.length + 1];
		ArrayList<Integer> list2 = this.getIntersection(arrayList);
		HashSet<Integer> list = new HashSet<Integer>(list2);
		scan(list);
		ArrayList<Integer> list3 = tdii.getDocumentIDs(String.valueOf(q.time));
		HashMap<Integer,HashSet<Integer>> res = new HashMap<Integer, HashSet<Integer>>();
		for (int x : list3) {
			if (set.containsKey(x)) {
				res.put(x,set.get(x));
			}
		}
	
	}
}
