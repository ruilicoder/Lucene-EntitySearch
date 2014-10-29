package topicSearch.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TimeKeywordDocumentIndex;
import topicSearch.index.TimeKeywordTopicIndex;
import topicSearch.index.TopicIDIndex;
import util.ExcutionTimer;
import entitySearch.Configure;

public class Plan2 extends ExecutionPlan{
	public Plan2(TimeKeywordTopicIndex tktdi, TimeKeywordDocumentIndex tkdi, TopicIDIndex eti) {
		this.tkdei  = tktdi;
		this.tkdi = tkdi;
		this.tii = eti;
	}
	TimeKeywordTopicIndex tkdei;
	TimeKeywordDocumentIndex tkdi;
	TopicIDIndex tii;
	public HashMap<String, Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		
		
		ArrayList<Integer>[] arrayList = new ArrayList[keywords.length];
		int index = tkdi.getDocumentIDs(keywords,String.valueOf(q.time), arrayList);
		ArrayList<Integer> list = this.getIntersection(arrayList);
		//System.out.println(list.size());
		HashMap<Integer,HashSet<Integer>> map = tkdei.getDocumentEntityPairs(String.valueOf(q.time),keywords[index]);
		//System.out.println(map.size());
		HashMap<Integer,HashSet<Integer>> results = getIntersection(map, list);
		res = getEntities(results, tii);
	}
	
	public static void main(String[] args) {
		TimeKeywordTopicIndex tktdi = new TimeKeywordTopicIndex(Configure.indexDir);
		System.out.println(tktdi.getNumDocs());
		DocTopicIndex dti = new DocTopicIndex(Configure.indexDir);
		TimeDocumentIndex tdi = new TimeDocumentIndex();
		TopicIDIndex tii = new TopicIDIndex(Configure.topicList);
		TimeKeywordDocumentIndex tkdi = new TimeKeywordDocumentIndex();
				
		Plan2 p2 = new Plan2(tktdi, tkdi, tii );
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		ExcutionTimer timer = new ExcutionTimer();

		System.out.println("test");
		timer.setStart();			
		for (int i = 1;  i < 2; i++) {
			  p2.processQuery(queries.get(i));	
			  /*for (String str: p2.res.keySet()) {
					System.out.println(str + ":" +  p2.res.get(str));
				}*/
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
	
}
