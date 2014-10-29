package topicSearch.plan;

import java.util.ArrayList;
import java.util.HashMap;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TimeKeywordDocumentIndex;
import topicSearch.index.TopicIDIndex;
import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.KeywordDocumentIndex;

public class Plan1 extends ExecutionPlan {
	public TimeKeywordDocumentIndex tkdi;
	public DocTopicIndex tdei;
	public TopicIDIndex tii;
	public Plan1(TimeKeywordDocumentIndex tkdi, DocTopicIndex tdei, TopicIDIndex tii)  {
			this.tkdi = tkdi;
			this.tdei = tdei;
			this.tii  = tii;
	}

	HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		
		//String[] keywords = q.keywords;
		
		ArrayList<Integer>[] arrayList = tkdi.getDocumentIDs( q.keywords,String.valueOf(q.time));
		ArrayList<Integer> list = this.getIntersection(arrayList);
		
		HashMap<Integer,Integer> map = tdei.getToics(list);
		res = new HashMap<String,Integer>();
		for (Integer str : map.keySet()) {
			res.put(tii.getTopic(str), map.get(str));
		}
		
	}
	public static void main(String[] args) {
		
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		System.out.println(kdi.getNumDocs());
		DocTopicIndex dti = new DocTopicIndex(Configure.indexDir);
		TimeDocumentIndex tdi = new TimeDocumentIndex();
		TopicIDIndex tii = new TopicIDIndex(Configure.topicList);
		TimeKeywordDocumentIndex tkdi = new TimeKeywordDocumentIndex();
				
		Plan1 p1 = new Plan1(tkdi, dti, tii );
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		ExcutionTimer timer = new ExcutionTimer();

		System.out.println("test");
		timer.setStart();			
		for (int i = 1;  i < 2; i++) {
			  p1.processQuery(queries.get(i));	
			  /*for (String str: p1.res.keySet()) {
					System.out.println(str + ":" +  p1.res.get(str));
			  }*/
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}
