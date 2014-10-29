package topicSearch.plan;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import topicSearch.index.DocTopicIndex;
import topicSearch.index.DocumentTimeIndex;
import topicSearch.index.KeywordDocumentIndex;
import topicSearch.index.TimeDocumentIndex;
import topicSearch.index.TimeKeywordDocumentIndex;
import topicSearch.index.TimeKeywordTopicIndex;
import topicSearch.index.TopicIDIndex;
import util.ExcutionTimer;
import entitySearch.Configure;

public class Exp {
	KeywordDocumentIndex kdi;
	TopicIDIndex tii;
	DocTopicIndex dti;
	TimeDocumentIndex tdi;
	TimeKeywordTopicIndex tktdi;
	TimeKeywordDocumentIndex tkdi;
	ArrayList<Query> queries; 
	DocumentTimeIndex dtii;
	public Exp() {
		kdi = new KeywordDocumentIndex();
		dti = new DocTopicIndex(Configure.indexDir);
		tdi = new TimeDocumentIndex();
		tktdi = new TimeKeywordTopicIndex(Configure.indexDir);
		tii = new TopicIDIndex(Configure.topicList);
		tkdi = new TimeKeywordDocumentIndex();
		dtii = new DocumentTimeIndex();
		
		kdi = new KeywordDocumentIndex();
		queries = Query.loadQuery(Configure.indexDir +  "queries.txt" );
		System.out.println("Query Size:" + queries.size());
		
	}
	public void testNumDocs() {
		System.out.println(kdi.getNumDocs());

		System.out.println(dti.getNumDocs());

		System.out.println(tdi.getNumDocs());

		System.out.println(tktdi.getNumDocs());

		System.out.println(tkdi.getNumDocs());

		System.out.println(dtii.getNumDocs());

	}
	public void testQueryProcess(int runtimes) {
		
		
		System.out.println("begin basic search plan2...");
	
	/*	BasicPlan2 bp2 = new  BasicPlan2(kdi, dti,dtii,tii);
		this.testQueryProcess(bp2,runtimes,"bp2");
		
		System.out.println("begin basic search plan...");
		BasicPlan bp = new  BasicPlan(kdi, dti,tdi,tii);
		this.testQueryProcess(bp,runtimes,"bp1");*/
		
		System.out.println("begin basic search plan...");
		DBPlan bp = new  DBPlan(kdi, dti,tdi,tii);
		this.testQueryProcess(bp,runtimes,"db3");
		
		
		
		
	/*	System.out.println("begin search plan1...");
		Plan1 plan1 = new Plan1(tkdi, dti, tii );
		this.testQueryProcess(plan1, runtimes, "plan1");
		
		
		System.out.println("begin search plan2...");
		Plan2 p2 = new Plan2(tktdi, tkdi, tii );
		
		this.testQueryProcess(p2, runtimes, "plan2");
		

		System.out.println("begin search plan3...");
		Plan3 p3 = new Plan3(tktdi, tii );
		this.testQueryProcess(p3, runtimes, "plan3");	
		*/
		
//		BasicPlan bp3 = new  BasicPlan(kdi, dti,tdi,tii);
//		this.testQueryProcess(bp,runtimes,"bp3");
		
/*		System.out.println("begin search plan4...");
		Plan4 p4 = new Plan4(tdei,kdi, eti);
		this.testQueryProcess(p4, runtimes, "plan4");	
		System.out.println("begin search plan5...");
		Plan5 p5 = new Plan5(kdei,eti);
		this.testQueryProcess(p5, runtimes, "plan5");	*/
	}
	
	public void testQueryProcess(ExecutionPlan ep, int runtimes, String name) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		warmup(ep,result);
		
		for (int i = 0; i < runtimes; i ++) {
			 run(ep, result) ;		
		}
		writeFile(name, result);
	}
	
	public void writeFile(String file, ArrayList<Integer> list) {
		try {
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.twitterResults + file));
		for (Integer time : list) {
			printer.println(time);
		}
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void warmup(ExecutionPlan ep, ArrayList<Integer> list) {
		int total = 0;;
		//PrintWriter printer = new PrintWriter(new FileWriter(file));
		ExcutionTimer timer = new ExcutionTimer();
		for (int i = 0; i <queries.size(); i++) {
			timer.setStart();					
			ep.processQuery(queries.get(i));	
			timer.setEnd();
			list.add(0);
		}
//		printer.close();
		
	}
	private int run(ExecutionPlan ep,  ArrayList<Integer> list) {
		int total = 0;;
		//PrintWriter printer = new PrintWriter(new FileWriter(file));
		ExcutionTimer timer = new ExcutionTimer();
		for (int i = 0; i <queries.size(); i++) {
			timer.setStart();					
			ep.processQuery(queries.get(i));	
			timer.setEnd();
			//printer.println(timer.getTime());
			int time = (int) (list.get(i) + timer.getTime());
			list.set(i,time);
			total  += timer.getTime();
		}
//		printer.close();
		
		return total;
	}
	
	public static void main(String[] args) {
		Exp e = new Exp();
		e.testQueryProcess(10);
		//e.testNumDocs();
	}
}
