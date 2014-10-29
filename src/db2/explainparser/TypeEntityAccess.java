package db2.explainparser;

import java.util.ArrayList;

import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;

public class TypeEntityAccess extends AccessNode{
	static EntityTypeIndex eti ;
	
	public TypeEntityAccess() {
		if (eti == null) {
			eti = new EntityTypeIndex(Configure.entityList);
		}
	}
	
	public ArrayList<Result> getList(int typeID) {
		ArrayList<Integer> list =  eti.typeIndex.get(typeID);
		ArrayList<Result> results = new ArrayList<Result>();
		for (int i = 0; i < list.size(); i++) {
			Result r = new Result(list.get(i));
			results.add(r);
		}
		return results;
	}
	
	
}
