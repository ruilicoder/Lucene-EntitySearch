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

public class Plan3 extends ExecutionPlan{
	public Plan3(TimeKeywordTopicIndex tktdi, TopicIDIndex eti) {
		this.tkdei  = tktdi;
		this.tii = eti;
	}
	TimeKeywordTopicIndex tkdei;
	TimeKeywordDocumentIndex tkdi;
	TopicIDIndex tii;
	public HashMap<String, Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		HashMap<Integer,HashSet<Integer>>[] results = new HashMap[q.keywords.length];
		int i = 0;
		for (String keyword: q.keywords) {
			HashMap<Integer,HashSet<Integer>> map = tkdei.getDocumentEntityPairs(String.valueOf(q.time), keyword);
			results[i] = map;
			i++;
		}
		
		
		HashMap<Integer,HashSet<Integer>> result = getIntersection(results);
		//ArrayList<Integer> list = engine.getDocs(timeID);
		//HashMap<Integer,HashSet<Integer>> res = engine.getIntersection(result,list);
		res  = getEntities(result, tii);
	}
	
	public static void main(String[] args) {
		TimeKeywordTopicIndex tktdi = new TimeKeywordTopicIndex(Configure.indexDir);
		System.out.println(tktdi.getNumDocs());
		DocTopicIndex dti = new DocTopicIndex(Configure.indexDir);
		TimeDocumentIndex tdi = new TimeDocumentIndex();
		TopicIDIndex tii = new TopicIDIndex(Configure.topicList);
		TimeKeywordDocumentIndex tkdi = new TimeKeywordDocumentIndex();
				
		Plan3 p3 = new Plan3(tktdi, tii );
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		ExcutionTimer timer = new ExcutionTimer();

		System.out.println("test");
		timer.setStart();			
		for (int i = 1;  i < 2; i++) {
			  p3.processQuery(queries.get(i));	
			  /*for (String str: p3.res.keySet()) {
					System.out.println(str + ":" +  p3.res.get(str));
			  }*/
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}
