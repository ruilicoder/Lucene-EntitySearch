package topicSearch.plan;

import java.util.ArrayList;
import java.util.HashMap;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.KeywordDocumentIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TopicIDIndex;
import util.ExcutionTimer;
import entitySearch.Configure;


public class BasicPlan extends ExecutionPlan{
	KeywordDocumentIndex tdi;
	DocTopicIndex dti;
	TimeDocumentIndex tdii;
	TopicIDIndex tii;
	public BasicPlan(KeywordDocumentIndex tdi, DocTopicIndex dti, TimeDocumentIndex tdii, TopicIDIndex tii) { 
		this.tdi = tdi;
		this.dti  = dti;
		this.tdii = tdii;
		this.tii = tii;
	}
	HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		
		ArrayList<Integer>[] arrayList = tdi.getDocumentIDs(keywords);
		ArrayList<Integer> list = tdii.getDocumentIDs(String.valueOf(q.time));
		
		ArrayList<Integer>[] resList = new ArrayList[arrayList.length + 1];
		for (int i = 0; i < arrayList.length; i++) {
			resList[i] = arrayList[i];			
		}
		resList[arrayList.length] = list;
		ArrayList<Integer> list2 = this.getIntersection(resList);
		HashMap<Integer,Integer> map = dti.getToics(list2);
		//HashMap<Integer,Integer> results = new HashMap<Integer,Integer>();
		
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
		BasicPlan bp = new  BasicPlan(kdi, dti, tdi, tii);
		
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
	
	
	
	
}
