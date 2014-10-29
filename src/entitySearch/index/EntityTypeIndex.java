package entitySearch.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import entitySearch.Configure;

public class EntityTypeIndex {
	public HashMap<String,String> entityTypeMap;
	public HashMap<String,Integer> entityIDMap;
	public HashMap<Integer,Integer> entityIDtypeIDMap;
	
	public ArrayList<String> entityList;
	public ArrayList<String> typeList;
	public HashMap<Integer, ArrayList<Integer>> typeIndex;

	public HashMap<String,Integer> typeIDMap;

	public EntityTypeIndex(String index)  {
		try  {
		BufferedReader reader = new BufferedReader (new FileReader(index));
		String temp = "";
		entityTypeMap = new HashMap<String,String>(); 
		while ((temp = reader.readLine()) !=  null) {
			String[] args = temp.split("\\|");
			String entityInstance =  args[0];
			String entityType = args[1];
			entityTypeMap.put(entityInstance, entityType);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		entityList = new ArrayList<String>();
		typeList = new ArrayList<String>();
		this.entityIDMap();
		this.typeIDMap();
		entityIDtypeIDMap = new HashMap<Integer,Integer>();
		typeIndex = new HashMap<Integer,ArrayList<Integer>>();
		for (int i = 0; i < entityList.size(); i++) {
			String str = entityList.get(i);
			String type = this.entityTypeMap.get(str);
			int typeID = this.typeIDMap.get(type);
			this.entityIDtypeIDMap.put(i, typeID);
			if (typeIndex.containsKey(typeID)) {
				ArrayList<Integer> list = typeIndex.get(typeID);
				list.add(i);
			}else {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(i);
				typeIndex.put(typeID, list);
			}
		}
		
	}
	public String getEntityName(Integer id) {
		return entityList.get(id);
	}
	public Integer getTypeID(Integer id) {
		return entityIDtypeIDMap.get(id);
	}
	private void  entityIDMap() {
		entityIDMap = new HashMap<String, Integer> ();
		int i = 0;
		for (String str : entityTypeMap.keySet()) {
			entityList.add(str);
			entityIDMap.put(str,entityList.size());
		}
	}
	
	public void typeIDMap() {
		typeIDMap = new HashMap<String, Integer>();
		int i = 0;
		for (String str : entityTypeMap.values()) {
			if (!typeIDMap.containsKey(str)) {
				typeList.add(str);			
				typeIDMap.put(str,typeList.size());
			}
		}
	}
	
	public void produceTable() {
		try {
		PrintWriter printer = new PrintWriter(new FileWriter("D:\\sigmodexp\\entitySearch\\entityentitytype.txt"));
		 
		for (String str: entityTypeMap.keySet()) {
			 int entityID = entityIDMap.get(str);
			 String type = entityTypeMap.get(str);
			 int typeID = typeIDMap.get(type);
			 printer.println(entityID + "," +  typeID);
		 }
		printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		EntityTypeIndex ei = new EntityTypeIndex(Configure.entityList);
		//ei.produceTable();
		ArrayList<String> list = ei.typeList;
		for (int i = 0; i< ei.typeList.size(); i++) {
			System.out.println(i + "\t" + ei.typeList.get(i));
		}
	}
	
}
