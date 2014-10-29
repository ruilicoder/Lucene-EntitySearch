package entitySearch.plan;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentEntityIndex;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.TypeDocumentEntityIndex;
import entitySearch.index.TypeEntityDocumentIndex;
import entitySearch.index.TypeKeywordDocumentEntityIndex;
import entitySearch.index.TypeKeywordDocumentIndex;

public class Exp {
	KeywordDocumentIndex kdi;
	EntityTypeIndex eti;
	DocumentEntityIndex dei;
	ArrayList<Query> queries; 
	TypeEntityDocumentIndex tedi;
	TypeDocumentEntityIndex tdei;
	TypeKeywordDocumentIndex tkdi;
	TypeKeywordDocumentEntityIndex tkdei;
	KeywordDocumentEntityIndex kdei;
	NewComparator n;
	public Exp() {
		
		kdi = new KeywordDocumentIndex();
		eti = new EntityTypeIndex(Configure.entityList);
		dei = new DocumentEntityIndex(Configure.indexDir);
		tedi = new TypeEntityDocumentIndex(Configure.indexDir);
		
		tdei = new TypeDocumentEntityIndex(Configure.indexDir);
		tkdi = new TypeKeywordDocumentIndex(Configure.indexDir);
		tkdei = new TypeKeywordDocumentEntityIndex(Configure.indexDir);
		kdei   = new KeywordDocumentEntityIndex(Configure.indexDir);
		queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		System.out.println("Query Size:" + queries.size());
		n = new NewComparator(kdi.reader);
		for (Query q : queries) {
		Arrays.sort(q.keywords, n);
		}
	}
	public void testNumDocs() {
		System.out.println(kdi.getNumDocs());

		System.out.println(dei.getNumDocs());

		System.out.println(tedi.getNumDocs());

		System.out.println(tdei.getNumDocs());

		System.out.println(tkdi.getNumDocs());

		System.out.println(tkdei.getNumDocs());
		

		System.out.println(kdei.getNumDocs());
	}
	public void testQueryProcess(int runtimes) {
		
		System.out.println("begin basic search plan...");
		
		DBPlan db = new  DBPlan(kdi, eti, dei);
		this.testQueryProcess(db,runtimes,"db2");

		System.out.println("begin basic search plan...");
	
		BasicPlan bp = new  BasicPlan(kdi, eti, dei);
		this.testQueryProcess(bp,runtimes,"bp1");
		
		
		
		System.out.println("begin entity search plan...");
		EntitySearchPlan esp =  new  EntitySearchPlan(kdi, eti, tedi);
		this.testQueryProcess(esp, runtimes,"esp");
		
		
		System.out.println("begin search plan1...");
		Plan1 plan1 =   new Plan1(tkdi, tdei,eti);
		this.testQueryProcess(plan1, runtimes, "plan1");
		
		
		System.out.println("begin search plan2...");
		Plan2 p2 = new Plan2(tkdei, tkdi,eti);
		
		this.testQueryProcess(p2, runtimes, "plan2");
		

		System.out.println("begin search plan3...");
		Plan3 p3 = new Plan3(tkdei, eti);
		this.testQueryProcess(p3, runtimes, "plan3");	
		
		System.out.println("begin search plan4...");
		Plan4 p4 = new Plan4(tdei,kdi, eti);
		this.testQueryProcess(p4, runtimes, "plan4");	
	
		System.out.println("begin search plan5...");
		Plan5 p5 = new Plan5(kdei,eti);
		this.testQueryProcess(p5, runtimes, "plan5");
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
		PrintWriter printer = new PrintWriter(new FileWriter(Configure.indexDir + file));
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
