package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.TypeEntityDocumentIndex;

public class EntitySearchPlan extends ExecutionPlan {

	KeywordDocumentIndex kdi;
	TypeEntityDocumentIndex tedi;
	EntityTypeIndex eti;
	public EntitySearchPlan(KeywordDocumentIndex kdi,  EntityTypeIndex eti, TypeEntityDocumentIndex tedi) { 
		this.kdi = kdi;
		this.tedi  = tedi;
		this.eti = eti;
		//this.dei = dei;
	}

	HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		String[] keywords = q.keywords;
		ArrayList<Integer>[] arrayList = kdi.getDocumentIDs(keywords);
		ArrayList<Integer> list = this.getIntersection(arrayList);
		//System.out.println(list.size());
		HashMap<Integer,HashSet<Integer>> map = tedi.getDocumentEntityPairs(Integer.toString(q.type));
		//System.out.println(map.size());
		HashMap<Integer,HashSet<Integer>> results = getIntersection(map, list);
		res = getEntities(results, eti);
	}
	
	public static void main(String[] args) {
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		TypeEntityDocumentIndex tedi = new TypeEntityDocumentIndex(Configure.indexDir);
		//tedi.buildIndex(fromFile, toDir)
		EntitySearchPlan bp = new  EntitySearchPlan(kdi, eti, tedi);
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();
		timer.setStart();	
		for (int i = 0;  i < queries.size(); i++) {
			bp.processQuery(queries.get(i));
		}
		timer.setEnd();
		System.out.println(timer.getTime());
	
	}

}
