package entitySearch.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import entitySearch.index.EntityTypeIndex;

public abstract class ExecutionPlan {
	abstract public void processQuery(Query  q);
	
	public ArrayList<Integer> getIntersection(ArrayList<Integer>[] lists) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (ArrayList<Integer> list : lists) {
			for (int x : list) {
				if (map.containsKey(x)) {
					int freq = map.get(x);
					map.put(x, freq + 1);
				} else {
					map.put(x, 1);
				}
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (Integer x : map.keySet()) {
			int freq = map.get(x);
			if ( freq == lists.length) {
				result.add(x);
			}
		}
		return result;
	}
	
	public HashMap<Integer,HashSet<Integer>> getIntersection(HashMap<Integer,HashSet<Integer>> set, ArrayList<Integer> list) {
		HashMap<Integer,HashSet<Integer>> result = new HashMap<Integer,HashSet<Integer>>();
		for (int x:list) {
			if (set.containsKey(x)) {
				result.put(x, set.get(x));
			}
		}
		return result;
	}
	
	public HashMap<String,Integer> getEntities(HashMap<Integer,HashSet<Integer>> map , EntityTypeIndex eti) {
		HashMap<Integer,Integer> result = new HashMap<Integer,Integer>();
		
		for (int x : map.keySet()) {
			HashSet<Integer> set = map.get(x);
			for (int id : set) {
				if (result.containsKey(id)) {
					int freq = result.get(id) + 1;
					result.put(id, freq);					
				} else {
					result.put(id, 1);
				}
			}
		}
		HashMap<String, Integer> res = new HashMap<String,Integer>();
		for (Integer x:result.keySet()) {
			String entity = eti.getEntityName(x);
			res.put(entity,result.get(x));
		}
		return res;
	}
	public HashMap<String,Integer> getEntities(HashMap<Integer,HashSet<Integer>> map , EntityTypeIndex eti, int type) {
		HashMap<Integer,Integer> result = new HashMap<Integer,Integer>();
		
		for (int x : map.keySet()) {
			HashSet<Integer> set = map.get(x);
			for (int id : set) {
				if (eti.getTypeID(id) == type) {
				if (result.containsKey(id)) {
					int freq = result.get(id) + 1;
					result.put(id, freq);					
				} else {
					result.put(id, 1);
				}
				}
			}
		}
		HashMap<String, Integer> res = new HashMap<String,Integer>();
		for (Integer x:result.keySet()) {
			String entity = eti.getEntityName(x);
			res.put(entity,result.get(x));
		}
		return res;
	}	
	/*
	 * Get A set of <Document, HashSet<Topic>>
	 */
	public HashMap<Integer,HashSet<Integer>> getIntersection(HashMap<Integer,HashSet<Integer>>[] sets) {
		HashMap<Integer,Integer> set = new HashMap<Integer,Integer>();
		for (HashMap<Integer,HashSet<Integer>> m : sets) {
			for (int x : m.keySet()) {
				if (set.containsKey(x)) {
					int freq = set.get(x);
					set.put(x, freq+1);
				} else {
					set.put(x , 1);
				}
			}
 		}
		HashMap<Integer,HashSet<Integer>> map = new HashMap<Integer,HashSet<Integer>>();
		for (int x :set.keySet()) {
			int freq = set.get(x);
			if (freq == sets.length) {
				HashSet<Integer> s = sets[0].get(x);
				map.put(x, s);
			}
		}
		return map;
	}
}
