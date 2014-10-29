package topicSearch.plan;

import java.util.ArrayList;
import java.util.HashMap;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.DocumentTimeIndex;
import topicSearch.index.KeywordDocumentIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TopicIDIndex;
import util.ExcutionTimer;
import entitySearch.Configure;

public class BasicPlan2 extends ExecutionPlan{
	KeywordDocumentIndex tdi;
	DocTopicIndex dti;
	DocumentTimeIndex dtii;
	TopicIDIndex tii;
	public BasicPlan2(KeywordDocumentIndex tdi, DocTopicIndex dti, DocumentTimeIndex tdii, TopicIDIndex tii) { 
		this.tdi = tdi;
		this.dti  = dti;
		this.dtii = tdii;
		this.tii = tii;
	}
	HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		
		ArrayList<Integer>[] arrayList = tdi.getDocumentIDs(keywords);
		//ArrayList<Integer> list = tdii.getDocumentIDs(String.valueOf(q.time));
		
		//ArrayList<Integer>[] resList = new ArrayList[arrayList.length + 1];
		ArrayList<Integer> list2 = this.getIntersection(arrayList);
		ArrayList<Integer> list3 = new ArrayList<Integer>();
		for (int docID : list2) {
			if (dtii.getTimeID(docID) == q.time) {
				list3.add(docID);
			}
		}
		
		HashMap<Integer,Integer> map = dti.getToics(list3);
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
		DocumentTimeIndex dtii = new DocumentTimeIndex();
		BasicPlan2 bp2 = new  BasicPlan2(kdi, dti, dtii, tii);
		
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		for (int i = 1; i < 2; i++) {
			bp2.processQuery(queries.get(i));	
			for (String str: bp2.res.keySet()) {
				System.out.println(str + ":" +  bp2.res.get(str));
			}
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}
