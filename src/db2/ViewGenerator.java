package db2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ViewGenerator {

	public static String dir = "D:\\sigmodexp\\entitySearch\\";
	public static String entityentitytype = "entityentitytype.txt";
	public static String entityDocument = "entityDocument.txt";
	public static String keywordDocument = "keyworddocumenttable.txt";
	public static int numberKeyword = 3612;
	public static HashMap<Integer, Integer> typeMap;
	public static HashMap<Integer, HashSet<Integer>> invertedIndex;
	static HashMap<Integer, HashMap<Integer, HashSet<Integer>>> typeInvertedIndex;

	public static void createV2KT() {
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		// HashMap<Integer,HashSet<Integer>>[] keywords = new
		// HashMap[numberKeyword];
		HashMap<String, Integer> keywordID = new HashMap<String, Integer>();
		typeMap = loadEntityEntityTypeTable();
		invertedIndex = LoadEntityDocument();

		typeInvertedIndex = createV4KT();
		System.out.println("finish loading");
		try {
			printer = new PrintWriter(new FileWriter(dir + "V2KT.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// int id = 1;
		String currentKeyword = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ keywordDocument));
			String temp = "";

			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				try {
					String keyword = args[0];
					int docID = Integer.parseInt(args[1]);

					if (keyword.compareTo(currentKeyword) == 0) {
						Set<Integer> entites = invertedIndex.get(docID);
						if (entites == null)
							continue;
						for (int entity : entites) {
							int typeID = typeMap.get(entity);
							if (map.containsKey(typeID)) {
								HashSet<Integer> docs = map.get(typeID);
								docs.add(docID);
							} else {
								HashSet<Integer> docs = new HashSet<Integer>();
								docs.add(docID);
								map.put(typeID, docs);
							}
						}

					} else {
						// id = id +1;
						printFile2(currentKeyword, map);
						currentKeyword = keyword;
						map.clear();
						Set<Integer> entites = invertedIndex.get(docID);
						if (entites == null)
							continue;
						for (int entity : entites) {
							int typeID = typeMap.get(entity);
							if (map.containsKey(typeID)) {
								HashSet<Integer> docs = map.get(typeID);
								docs.add(docID);
							} else {
								HashSet<Integer> docs = new HashSet<Integer>();
								docs.add(docID);
								map.put(typeID, docs);
							}
						}
					}
				} catch (Exception e) {
					System.err.println(temp);
					continue;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		printer.close();
	}
	
	public static void reWriteEntityDocument() {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ entityDocument));
			String temp = "";
			PrintWriter printer = new PrintWriter(new FileWriter(dir+ entityDocument+"s"));
			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				int docID = Integer.parseInt(args[0]);
				int entityID = Integer.parseInt(args[1].trim());
				// map.put(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
				if (docID > 30000) break;
				printer.println(docID + "," + entityID);
			}
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void reWriteKeywordTable() {
		try {
			printer = new PrintWriter(new FileWriter(dir + "keywordDocument.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// int id = 1;
		String currentKeyword = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ keywordDocument));
			String temp = "";
			HashSet<String> keywords = new HashSet<String>();
			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				try {
					String keyword = args[0];
					int docID = Integer.parseInt(args[1]);
					if (docID > 30000) continue;
					if (keyword.length() >=20) {
						System.err.println("50");
					} else {
						
						keywords.add(keyword);
						if (keywords.size() >=5000) break;
						
						printer.println(keyword + "," + docID);					
					}
					
				} catch (Exception e) {
					System.err.println(temp);
					continue;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		printer.close();
	}
	
	public static void createV6K() {
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		// HashMap<Integer,HashSet<Integer>>[] keywords = new
		// HashMap[numberKeyword];
		HashMap<String, Integer> keywordID = new HashMap<String, Integer>();
		typeMap = loadEntityEntityTypeTable();
		invertedIndex = LoadEntityDocument();

		System.out.println("finish loading");
		try {
			printer = new PrintWriter(new FileWriter(dir + "V6K.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// int id = 1;
		String currentKeyword = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ keywordDocument));
			String temp = "";

			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				try {
					String keyword = args[0];
					int docID = Integer.parseInt(args[1]);
					Set<Integer> entites = invertedIndex.get(docID);
					if (entites == null)
							continue;
					for (int entity : entites) {
							printer.println(keyword +  "," + docID + ","
									+ entity);
					}
				} catch (Exception e) {
					System.err.println(temp);
					continue;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		printer.close();
	}
	

	public static void createV1KT() {
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		// HashMap<Integer,HashSet<Integer>>[] keywords = new
		// HashMap[numberKeyword];
		HashMap<String, Integer> keywordID = new HashMap<String, Integer>();
		HashMap<Integer, Integer> typeMap = loadEntityEntityTypeTable();

		HashMap<Integer, HashSet<Integer>> invertedIndex = LoadEntityDocument();
		System.out.println("finish loading");
		try {
			printer = new PrintWriter(new FileWriter(dir + "V1K.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// int id = 1;
		String currentKeyword = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ keywordDocument));
			String temp = "";

			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				try {
					String keyword = args[0];
					int docID = Integer.parseInt(args[1]);

					if (keyword.compareTo(currentKeyword) == 0) {
						Set<Integer> entites = invertedIndex.get(docID);
						if (entites == null)
							continue;
						for (int entity : entites) {
							int typeID = typeMap.get(entity);
							if (map.containsKey(typeID)) {
								HashSet<Integer> docs = map.get(typeID);
								docs.add(docID);
							} else {
								HashSet<Integer> docs = new HashSet<Integer>();
								docs.add(docID);
								map.put(typeID, docs);
							}
						}

					} else {
						// id = id +1;
						printFile(currentKeyword, map);
						currentKeyword = keyword;
						map.clear();
						Set<Integer> entites = invertedIndex.get(docID);
						if (entites == null)
							continue;
						for (int entity : entites) {
							int typeID = typeMap.get(entity);
							if (map.containsKey(typeID)) {
								HashSet<Integer> docs = map.get(typeID);
								docs.add(docID);
							} else {
								HashSet<Integer> docs = new HashSet<Integer>();
								docs.add(docID);
								map.put(typeID, docs);
							}
						}
					}
				} catch (Exception e) {
					System.err.println(temp);
					continue;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		printer.close();
	}

	public static PrintWriter printer;
	static int count = 0;

	public static void printFile(String keyword,
			HashMap<Integer, HashSet<Integer>> map) {
		System.out.println(count++);
		for (Integer key : map.keySet()) {
			HashSet<Integer> set = map.get(key);
			for (Integer docID : set)
				printer.println(keyword + "," + key + "," + docID);
		}
	}

	public static HashMap<Integer, HashMap<Integer, HashSet<Integer>>> createV4KT() {
		HashMap<Integer, HashMap<Integer, HashSet<Integer>>> map = new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();
		for (int docID : invertedIndex.keySet()) {
			HashSet<Integer> entities = invertedIndex.get(docID);
			if (entities == null)
				continue;
			HashMap<Integer, HashSet<Integer>> typeEntity = new HashMap<Integer, HashSet<Integer>>();
			for (int entityID : entities) {
				int typeID = typeMap.get(entityID);
				if (typeEntity.containsKey(typeID)) {
					HashSet<Integer> docs = typeEntity.get(typeID);
					docs.add(entityID);
				} else {
					HashSet<Integer> docs = new HashSet<Integer>();
					docs.add(entityID);
					typeEntity.put(typeID, docs);
				}
			}
			if (typeEntity.isEmpty())
				continue;
			map.put(docID, typeEntity);
		}
		return map;
	}

	public static void printV3DT() {
		try {
			printer = new PrintWriter(new FileWriter(dir + "v3td.txt"));
			for (Integer docID : typeInvertedIndex.keySet()) {
				HashMap<Integer, HashSet<Integer>> typeEntites = typeInvertedIndex
						.get(docID);
				for (Integer key : typeEntites.keySet()) {
					HashSet<Integer> entites = typeEntites.get(key);
					if (entites == null)
						continue;
					for (int entityID : entites) {
						printer.println(docID + "," + key + "," + entityID);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		printer.close();
	}

	public static void printFile2(String keyword,
			HashMap<Integer, HashSet<Integer>> map) {
		System.out.println(count++);
		for (Integer key : map.keySet()) {
			HashSet<Integer> set = map.get(key);
			for (Integer docID : set) {
				HashMap<Integer, HashSet<Integer>> typeEntites = typeInvertedIndex
						.get(docID);
				HashSet<Integer> entites = typeEntites.get(key);
				if (entites == null)
					continue;
				for (int entityID : entites) {
					printer.println(keyword + "," + key + "," + docID + ","
							+ entityID);
				}
			}
		}
	}

	public static HashMap<Integer, Integer> loadEntityEntityTypeTable() {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ entityentitytype));
			String temp = "";

			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				map.put(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static HashMap<Integer, HashSet<Integer>> LoadEntityDocument() {
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir
					+ entityDocument));
			String temp = "";

			while ((temp = reader.readLine()) != null) {
				String[] args = temp.split(",");
				//System.out.println(temp);
				int docID = Integer.parseInt(args[0]);
				int entityID = Integer.parseInt(args[1].trim());
				// map.put(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
				if (map.containsKey(docID)) {
					HashSet<Integer> entities = map.get(docID);
					entities.add(entityID);

				} else {
					HashSet<Integer> entities = new HashSet<Integer>();
					entities.add(entityID);
					map.put(docID, entities);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) {
		typeMap = loadEntityEntityTypeTable();
		invertedIndex = LoadEntityDocument();

		typeInvertedIndex = createV4KT();
		printV3DT();
		//createV1KT();
		//createV6K();
		//createV2KT();
		//reWriteKeywordTable();
		//reWriteEntityDocument();
	}
}
