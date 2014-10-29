package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.TypeKeywordDocumentEntityIndex;
import entitySearch.index.TypeKeywordDocumentIndex;

public class Plan3 extends ExecutionPlan {
	TypeKeywordDocumentEntityIndex tkdei;
	EntityTypeIndex eti;
	public Plan3(TypeKeywordDocumentEntityIndex tkdei, EntityTypeIndex eti) {
		this.tkdei  = tkdei;
		this.eti = eti;
	}
	public HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		HashMap<Integer,HashSet<Integer>>[] results = new HashMap[q.keywords.length];
		int i = 0;
		for (String keyword: q.keywords) {
			HashMap<Integer,HashSet<Integer>> map = tkdei.getDocumentEntityPairs(keyword,q.type);
			results[i] = map;
			i++;
		}
		
		
		HashMap<Integer,HashSet<Integer>> result = getIntersection(results);
		//ArrayList<Integer> list = engine.getDocs(timeID);
		//HashMap<Integer,HashSet<Integer>> res = engine.getIntersection(result,list);
		res  = getEntities(result, eti);
	}
	
	public static void main(String[] args) {
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		TypeKeywordDocumentIndex tkdi = new TypeKeywordDocumentIndex(Configure.indexDir);
		TypeKeywordDocumentEntityIndex tkdei = new TypeKeywordDocumentEntityIndex(Configure.indexDir);
				
		Plan3 p3 = new Plan3(tkdei, eti);
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		
		System.out.println("test");
		for (int i = 1;  i <queries.size(); i++) {
			  p3.processQuery(queries.get(i));	
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}
