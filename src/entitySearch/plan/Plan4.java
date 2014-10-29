package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.TypeDocumentEntityIndex;

public class Plan4 extends ExecutionPlan {
	TypeDocumentEntityIndex tdei; 
	KeywordDocumentIndex kdi;
	EntityTypeIndex eti; 
	public Plan4(TypeDocumentEntityIndex tdei, KeywordDocumentIndex kdi, EntityTypeIndex eti) {
		this.tdei = tdei;
		this.kdi = kdi;
	
		this.eti = eti;
	}
	public HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		ArrayList<Integer>[] arrayList = kdi.getDocumentIDs(keywords);
		ArrayList<Integer> list = this.getIntersection(arrayList);
		HashMap<Integer,Integer> map = tdei.getEntites(list, String.valueOf(q.type));
		
		 res = new HashMap<String,Integer>();
		for (Integer str : map.keySet()) {
			res.put(eti.getEntityName(str), map.get(str));
		}
	}
	
	public static void main(String[] args ) { 
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		TypeDocumentEntityIndex tdei = new TypeDocumentEntityIndex(Configure.indexDir);
		
		Plan4 bp = new Plan4(tdei, kdi, eti);
		
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		for (int i = 1; i <2; i++) {
			bp.processQuery(queries.get(i));	
			System.out.println(queries.get(i).type);
			for (String str: bp.res.keySet()) {
				System.out.println(str + ":" +  bp.res.get(str));
			}
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}

