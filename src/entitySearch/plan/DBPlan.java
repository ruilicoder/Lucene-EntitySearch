package entitySearch.plan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentIndex;

public class DBPlan extends ExecutionPlan{
	KeywordDocumentIndex tdi;
	EntityTypeIndex eti;
	DocumentEntityIndex dei;
	NewComparator n;
	public DBPlan(KeywordDocumentIndex tdi, EntityTypeIndex eti, DocumentEntityIndex dei) { 
		this.tdi = tdi;
		this.eti  = eti;
		this.dei = dei;
		 n = new NewComparator(tdi.reader);
	}
	HashMap<String,Integer> ress;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		
		if (q.keywords.length == 1) {
			ArrayList<Integer> list = tdi.getDocumentIDs(q.keywords[0]);
			HashMap<Integer,Integer> results = dei.getEntites(list);
			ArrayList<Integer> list2 = eti.typeIndex.get(q.type);
			HashMap<Integer, Integer> r = new HashMap<Integer,Integer>();
			for (Integer entity : list2) {
				if (results.containsKey(entity)) {
					r.put(entity, results.get(entity));
					
				}
			}
			ress = new HashMap<String,Integer>();
			for (Integer str : r.keySet()) {
				ress.put(eti.getEntityName(str), r.get(str));
			}
		}
		if (q.keywords.length == 2) {
			ArrayList<Integer>[] lists = tdi.getDocumentIDs(q.keywords);
		
	
			
			ArrayList<Integer> list = this.getIntersection(lists);
			
			
			HashMap<Integer,Integer> results = dei.getEntites(list);
			
	
			ArrayList<Integer> list2 = eti.typeIndex.get(q.type);
			
			
		
			HashMap<Integer, Integer> r = new HashMap<Integer,Integer>();
				for (int e2: list2)			
				{
					if (results.containsKey(e2)) {
						r.put(e2, results.get(e2));
	
					}
					
			}

			ress = new HashMap<String,Integer>();
			for (Integer str : r.keySet()) {
				ress.put(eti.getEntityName(str), r.get(str));
			}
		}
		
		if (q.keywords.length > 2) {
			
			ArrayList<Integer>[] lists = new ArrayList[2];
			lists[0] = tdi.getDocumentIDs(q.keywords[0]);
			lists[1] = tdi.getDocumentIDs(q.keywords[1]);
			ArrayList<Integer> list = this.getIntersection(lists);
			
			
			ArrayList<entitySearch.plan.Result> results = dei.getEntities(list);			
			ArrayList<Integer> list2 = eti.typeIndex.get(q.type);
			ArrayList<Result > res = new ArrayList<Result>();
			for (Result r : results) {
				for (Integer e : list2) {
					if (r.entityID == e) {
						res.add(r);
					}
				}
			}

			for (int i = 2 ; i < q.keywords.length; i++) {
				results  = res;
				res = new ArrayList<Result>();
				list2 = tdi.getDocumentIDs(q.keywords[i]);
				for (Result r : results) {
					for (Integer e : list2) {
						if (r.entityID == e) {
							res.add(r);
						}
					}
				}					
			}
			HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
			for (Result r: res) {
				if (map.containsKey(r.entityID)) {
					int freq = map.get(r.entityID);
					map.put(r.entityID, freq + 1);
				} else {
					map.put(r.entityID,  1);
						
				}
			}

			ress = new HashMap<String,Integer>();
			for (Integer str : map.keySet()) {
				ress.put(eti.getEntityName(str), map.get(str));
			}
			
		}
	}

	public static void main(String[] args) {
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		DocumentEntityIndex dei = new DocumentEntityIndex(Configure.indexDir);
		
		DBPlan bp = new  DBPlan(kdi, eti, dei);
		NewComparator n = new NewComparator(kdi.reader);
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		System.out.println("here");
		for (int i = 0; i< 10; i++) {
			System.out.println(queries.get(i).keywords[0]);
			Arrays.sort(queries.get(i).keywords, n);

			System.out.println(queries.get(i).keywords[0]);
		}
		System.out.println("here");
		
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		for (int i = 1; i < 10; i++) {
			bp.processQuery(queries.get(i));	
			
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}
