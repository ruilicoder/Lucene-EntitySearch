package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentEntityIndex;

public class Plan5 extends ExecutionPlan{
	KeywordDocumentEntityIndex kdei;
	EntityTypeIndex eti;
	public Plan5(KeywordDocumentEntityIndex kdei, EntityTypeIndex eti) {
		this.kdei = kdei;
		this.eti = eti;
	}
	public HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		HashMap<Integer,HashSet<Integer>>[] results = new HashMap[q.keywords.length];
		int i = 0;
		for (String keyword: q.keywords) {
			HashMap<Integer,HashSet<Integer>> map = kdei.getDocumentEntityPairs(keyword);
			results[i] = map;
			i++;
		}
		
		
	//	HashMap<Integer,HashSet<Integer>> result = getIntersection(results);
		//  = getEntities(result, eti,q.type);

	}
	public static void main(String[] args ) { 
		KeywordDocumentEntityIndex kdei = new KeywordDocumentEntityIndex(Configure.indexDir);
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
	//	TypeDocumentEntityIndex tdei = new TypeDocumentEntityIndex(Configure.indexDir);
		
		Plan5 bp = new Plan5(kdei, eti);
		
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		for (int i = 1; i <2; i++) {
			bp.processQuery(queries.get(i));	
		//	System.out.println(queries.get(i).type);
			for (String str: bp.res.keySet()) {
				System.out.println(str + ":" +  bp.res.get(str));
			}
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}

}
