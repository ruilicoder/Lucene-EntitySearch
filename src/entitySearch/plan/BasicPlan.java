package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentIndex;

public class BasicPlan extends ExecutionPlan{
	KeywordDocumentIndex tdi;
	EntityTypeIndex eti;
	DocumentEntityIndex dei;
	public BasicPlan(KeywordDocumentIndex tdi, EntityTypeIndex eti, DocumentEntityIndex dei) { 
		this.tdi = tdi;
		this.eti  = eti;
		this.dei = dei;
	}
	HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		ArrayList<Integer>[] arrayList = tdi.getDocumentIDs(keywords);
		ArrayList<Integer> list = this.getIntersection(arrayList);
		
		HashMap<Integer,Integer> map = dei.getEntites(list);
		HashMap<Integer,Integer> results = new HashMap<Integer,Integer>();
		for (Integer eid : map.keySet()) {
			if (eti.entityIDtypeIDMap.get(eid) == q.type) {
				results.put(eid,map.get(eid));	
				//System.out.println("fuck");
			}
		}
		 res = new HashMap<String,Integer>();
		for (Integer str : results.keySet()) {
			res.put(eti.getEntityName(str), results.get(str));
		}
	}

	public static void main(String[] args) {
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		DocumentEntityIndex dei = new DocumentEntityIndex(Configure.indexDir);
		
		BasicPlan bp = new  BasicPlan(kdi, eti, dei);
		
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		for (int i = 1; i <10; i++) {
			bp.processQuery(queries.get(i));	
			for (String str: bp.res.keySet()) {
				System.out.println(str + ":" +  bp.res.get(str));
			}
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
	
	
	
	
}
