package topicSearch.plan;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.DocumentTimeIndex;
import topicSearch.index.KeywordDocumentIndex;
import topicSearch.index.KeywordTopicDocumentIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TimeKeywordDocumentIndex;
import topicSearch.index.TimeKeywordTopicIndex;
import util.ExcutionTimer;
import entitySearch.Configure;

public class InsertTime {
	ExcutionTimer te;
	public InsertTime() {
		 te = new ExcutionTimer();	
	}
	
	public void warmup() {
		te.setStart();
		KeywordDocumentIndex.insert(Configure.twitterDocs, Configure.indexDir);
		te.setEnd();
		long kds = te.getTime();
	//	System.out.println("kds \t" + kds);
		
		te.setStart(); 		
		DocTopicIndex.insert(Configure.twitterTopicsID, Configure.indexDir);
		te.setEnd();
		long des = te.getTime();
		
	//	System.out.println("des \t" + des);
		
		te.setStart(); 		
		TimeDocumentIndex.insertDocuments(Configure.indexDir);
		te.setEnd();		
		long tds  = te.getTime();
		//System.out.println("teds \t" + teds); 
		
		te.setStart();
		KeywordTopicDocumentIndex.insert(Configure.twitterDocs, Configure.twitterTopicsID, Configure.indexDir);
		te.setEnd();
		long ktds = te.getTime();
		
		//System.out.println("tkds\t"  + tkds);
	
		
		te.setStart();
		TimeKeywordTopicIndex.insert(Configure.twitterDocs ,  Configure.twitterTopics, Configure.twitterTime, Configure.indexDir);
		te.setEnd();
		
		long tkdes = te.getTime();		
		//System.out.println("tkdes\t" + tkdes); 
		te.setStart();
		TimeKeywordDocumentIndex.insert(Configure.twitterDocs, Configure.indexDir);
		te.setEnd();
		long tkds= te.getTime();
		
		
		
		
/*		
		TypeDocumentEntityIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		 *
		
		long tdes = te.getTime();		
		//System.out.println("tdes\t" + tdes);
		*/
		
		double b1 = kds + des  + tds;
		double p1 = tkds + des;
		double p2 = tkdes + tkds;
		double p3 = tkdes;
		double p4 = tkds + des;  
		//System.out.println(b1 + "\t" + b2 + "\t" + p1 + "\t" + p2 + "\t" + p3 + "\t" + p4);
	}
	public void testInsert() {
		
		te.setStart();
		KeywordDocumentIndex.insert(Configure.twitterDocs, Configure.indexDir);
		te.setEnd();
		long kds = te.getTime();
	//	System.out.println("kds \t" + kds);
		
		te.setStart(); 		
		DocTopicIndex.insert(Configure.twitterTopicsID, Configure.indexDir);
		te.setEnd();
		long des = te.getTime();
		
	//	System.out.println("des \t" + des);
		
		te.setStart(); 		
		TimeDocumentIndex.insertDocuments(Configure.indexDir);
		te.setEnd();		
		long tds  = te.getTime();
		//System.out.println("teds \t" + teds); 
		te.setStart(); 		
		DocumentTimeIndex.insertDocuments(Configure.indexDir);
		te.setEnd();		
		long dtts  = te.getTime();

		
		
		te.setStart();
		KeywordTopicDocumentIndex.insert(Configure.twitterDocs, Configure.twitterTopicsID, Configure.indexDir);
		te.setEnd();
		long ktds = te.getTime();
		
		//System.out.println("tkds\t"  + tkds);
	
		
		te.setStart();
		TimeKeywordTopicIndex.insert(Configure.twitterDocs ,  Configure.twitterTopicsID, Configure.twitterTime, Configure.indexDir);
		te.setEnd();
		
		long tkdes = te.getTime();		
		//System.out.println("tkdes\t" + tkdes); 
		te.setStart();
		TimeKeywordDocumentIndex.insert(Configure.twitterDocs, Configure.indexDir);
		te.setEnd();
		long tkds= te.getTime();
		
		
		
		
/*		
		TypeDocumentEntityIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		 *
		
		long tdes = te.getTime();		
		//System.out.println("tdes\t" + tdes);
		*/
		
		double b1 = kds + des  + tds;
		double b2 = kds + des  + dtts;
		
		double p1 = tkds + des;
		double p2 = tkdes + tkds;
		double p3 = tkdes;
		double p4 = tkds + des;  
		System.out.println(b1 + "\t" +  b2 + "\t" + p1 + "\t" + p2 + "\t" + p3 + "\t" + p4);		
	
	}
	
	public static void main(String[] args) {
		InsertTime it = new InsertTime();
		//it.warmup();
		for (int i = 0 ; i < 10; i++) {
			it.testInsert();
		}
		
	}
}
