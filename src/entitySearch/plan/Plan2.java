package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.TypeKeywordDocumentEntityIndex;
import entitySearch.index.TypeKeywordDocumentIndex;

public class Plan2 extends ExecutionPlan{
	public Plan2(TypeKeywordDocumentEntityIndex tkdei, TypeKeywordDocumentIndex tkdi, EntityTypeIndex eti) {
		this.tkdei  = tkdei;
		this.tkdi = tkdi;
		this.eti = eti;
	}
	TypeKeywordDocumentEntityIndex tkdei;
	TypeKeywordDocumentIndex tkdi;
	EntityTypeIndex eti;
	public HashMap<String, Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		
		
		ArrayList<Integer>[] arrayList = new ArrayList[keywords.length];
		
		int index = tkdi.getDocumentIDs(keywords,q.type, arrayList);
		ArrayList<Integer> list = this.getIntersection(arrayList);
		//System.out.println(list.size());
		HashMap<Integer,HashSet<Integer>> map = tkdei.getDocumentEntityPairs(keywords[index],q.type);
		//System.out.println(map.size());
		HashMap<Integer,HashSet<Integer>> results = getIntersection(map, list);
		res = getEntities(results, eti);
	}
	
	public static void main(String[] args) {
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		TypeKeywordDocumentIndex tkdi = new TypeKeywordDocumentIndex(Configure.indexDir);
		TypeKeywordDocumentEntityIndex tkdei = new TypeKeywordDocumentEntityIndex(Configure.indexDir);
				
		Plan2 p2 = new Plan2(tkdei, tkdi,eti);
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();		
		
		System.out.println("test");
		for (int i = 1;  i <queries.size(); i++) {
			  p2.processQuery(queries.get(i));	
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
	
}
