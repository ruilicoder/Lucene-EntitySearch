package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.TypeDocumentEntityIndex;
import entitySearch.index.TypeKeywordDocumentIndex;

public class Plan1 extends ExecutionPlan {
	public TypeKeywordDocumentIndex tkdi;
	public TypeDocumentEntityIndex tdei;
	public EntityTypeIndex eti;
	public Plan1(TypeKeywordDocumentIndex tkdi, TypeDocumentEntityIndex tdei, EntityTypeIndex eti)  {
			this.tkdi = tkdi;
			this.tdei = tdei;
			this.eti  = eti;
	}

	HashMap<String,Integer> res;
	@Override
	public void processQuery(Query q) {
		// TODO Auto-generated method stub
		
		String[] keywords = q.keywords;
		ArrayList<Integer>[] arrayList = tkdi.getDocumentIDs(keywords,q.type);
		
		ArrayList<Integer> list = this.getIntersection(arrayList);
		HashMap<Integer,Integer> map = tdei.getEntites(list,String.valueOf(q.type));
		 res = new HashMap<String,Integer>();
		for (Integer str : map.keySet()) {
			res.put(eti.getEntityName(str), map.get(str));
		}
		
	}
	public static void main(String[] args) {
		KeywordDocumentIndex kdi = new KeywordDocumentIndex();
		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		DocumentEntityIndex dei = new DocumentEntityIndex(Configure.indexDir);
		TypeDocumentEntityIndex tdei = new TypeDocumentEntityIndex(Configure.indexDir);
		TypeKeywordDocumentIndex tkdi = new TypeKeywordDocumentIndex(Configure.indexDir);
				
		Plan1 p1 = new Plan1(tkdi, tdei,eti);
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		ExcutionTimer timer = new ExcutionTimer();

		System.out.println("test");
		timer.setStart();			
		for (int i = 1;  i <queries.size(); i++) {
			  p1.processQuery(queries.get(i));	
			
		}
		timer.setEnd();
		System.out.println(timer.getTime());
		
	}
}
