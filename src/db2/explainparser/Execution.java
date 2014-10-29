package db2.explainparser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.plan.Query;

public class Execution {
	Node root;
	public Execution() {
	
	}

	public ArrayList<Result> process(Node n) {
		if (n instanceof AccessNode) {
			//System.out.println("table scan");
			if (((AccessNode) n).child instanceof KeywordIndexAccess) {
			//	System.out.println(" keyword access node"
			//			+ ((AccessNode) n).key); 
				AccessNode c = ((AccessNode) n).child;
				KeywordIndexAccess ka = (KeywordIndexAccess) c;
				// c.key.replace("'", "");
				if (n.columnID[0] != n.DOCID) {
					System.err.println("error");
				}
				n.numColumn = 1;
				return ka.getDocuments(((AccessNode) n).key);
			}
			if (((AccessNode) n).child instanceof TypeEntityAccess) {
				/*System.out.println(" Entity Access" + ((AccessNode) n).key); */
				AccessNode c = ((AccessNode) n).child;
				TypeEntityAccess tea = (TypeEntityAccess) c;
				// c.key.replace("'", "");
				if (n.columnID[0] != n.ENTITY) {
					System.err.println("error");
				}
				n.numColumn = 1;
				return tea.getList(Integer.parseInt(((AccessNode) n).key));
			}
			if (((AccessNode) n).child instanceof DocumentEntityIndexAccess) {
				//System.out.println(" document access" + ((AccessNode) n).key); 
				AccessNode c = ((AccessNode) n).child;
				DocumentEntityIndexAccess dea = (DocumentEntityIndexAccess) c;
				// c.key.replace("'", "");
				if (n.columnID[0] != n.ENTITY) {
					System.err.println("error");
				}

				n.numColumn = 1;
				return dea.getResults((Integer.parseInt(((AccessNode) n).key)));

			}

			System.err.println("error in processing AcessNode");
		}
		if (n instanceof JoinNode) {

			//System.out.println(((JoinNode) n).JoinMethod);
			if (((JoinNode) n).JoinMethod.compareTo(Parser.HASHJOIN) == 0) {
				JoinNode j = (JoinNode) n;
				ArrayList<Result> leftResult = process(((JoinNode) n).leftChild);
				ArrayList<Result> results = new ArrayList<Result>();
				String key = ((AccessNode) j.rightNode).key;
				if (key.compareTo("?") == 0) {
					/**
					 * Hash Join left has no key to retrival results; then
					 * combine them;
					 * 
					 **/
					System.err.println("eror processing HashJOIN");
					return null;

				} else {
					/**
					 * Hash Join left has key to retrival results; then combine
					 * them;
					 * 
					 **/

					ArrayList<Result> rightResult = process(j.rightNode);
					int index = -1;
					for (int i = 0; i < j.leftChild.numColumn; i++) {
						if (j.leftChild.columnID[i] == j.rightNode.columnID[0]) {
							index = i;
						}
					}
					if (index != -1) {
						HashMap<Integer, ArrayList<Result>> map = new HashMap<Integer, ArrayList<Result>>();
						for (Result r : leftResult) {
							if (map.containsKey(r.x[index])) {
								ArrayList<Result> list = map.get(r.x[index]);
								list.add(r);
							} else {
								ArrayList<Result> temp = new ArrayList<Result>();
								temp.add(r);
								map.put(r.x[index], temp);
							}
						}

						for (Result r : rightResult) {
							ArrayList<Result> ll = map.get(r.x[0]);
							if (ll != null)
							results.addAll(ll);
						}
						n.columnID = j.leftChild.columnID;
						n.numColumn = j.leftChild.numColumn;
						
						
						return results;

					} else {
						System.out.println("error in HashJoin");
						return null;
					}

				}
			}
			if (((JoinNode) n).JoinMethod.compareTo(Parser.NEStEDLOOPJOIN) == 0) {
				JoinNode j = (JoinNode) n;
				ArrayList<Result> leftResult = process(((JoinNode) n).leftChild);
				ArrayList<Result> results = new ArrayList<Result>();
				String key = ((AccessNode) j.rightNode).key;
				if (key.compareTo("?") == 0) {
					/**
					 * Nasted LOOp Join left has no key to retrival results;
					 * then combine them;
					 * 
					 **/
					if (j.leftChild.numColumn == 1) {
					for (Result l : leftResult) {
							((AccessNode) j.rightNode).key = String.valueOf(l.x[0]);
							ArrayList<Result> rightResult = process(j.rightNode);
							for (Result r:rightResult) {
								if (l.x.length == 1) {
									Result res = new Result(l.x[0], r.x[0]);
									results.add(res);
								}
							}
					}
					n.columnID = new int[j.leftChild.numColumn + 1];
					for (int i = 0; i < j.leftChild.numColumn; i++) {
						n.columnID[i] = j.leftChild.columnID[i];
					}
					n.columnID[j.leftChild.numColumn] = j.rightNode.columnID[0];
					n.numColumn = j.leftChild.numColumn + 1;
					return results;
				}
					System.err.println("processing error in nasted loop");
					return null;
				} else {
					/**
					 * Nasted Loop Join left has key to retrival results; then
					 * combine them;
					 * 
					 **/
					ArrayList<Result> rightResult = process(j.rightNode);
					int index = -1;
					for (int i = 0; i < j.leftChild.numColumn; i++) {
						if (j.leftChild.columnID[i] == j.rightNode.columnID[0]) {
							index = i;
						}
					}
					if (index != -1) {
						for (Result l : leftResult)
							for (Result r : rightResult) {
								if (l.x[index] == r.x[0]) {
									results.add(l);
								}
							}
						n.columnID = j.leftChild.columnID;
						n.numColumn = j.leftChild.numColumn;
						return results;

					} else {
						for (Result l : leftResult)
							for (Result r : rightResult) {
								if (l.x.length == 1) {
									Result res = new Result(l.x[0], r.x[0]);
									results.add(res);
								}
								if (l.x.length == 2) {
									Result res = new Result(l.x[0], l.x[1],
											r.x[0]);
									results.add(res);
								}
							}
						n.columnID = new int[j.leftChild.numColumn + 1];
						for (int i = 0; i < j.leftChild.numColumn; i++) {
							n.columnID[i] = j.leftChild.columnID[i];
						}
						n.columnID[j.leftChild.numColumn] = j.rightNode.columnID[0];
						n.numColumn = j.leftChild.numColumn + 1;
						return results;
					}

				}

			}
			System.err.println("error in processing Nested Loop Join");
			// printer(((JoinNode) n).rightNode);
			return null;
		}
		System.err.println("error in processing node");
		return null;
	}
	public static void main(String[] args) {
		//Parser p = new Parser();
		
		Execution e = new Execution();
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		
		ArrayList<Node> list = new ArrayList<Node>();
		Parser2 p = new Parser2();
		for (int i = 364 ; i < 368; i++) {
			Node root = p.parse("D:\\sigmodexp\\entity2\\explain"+i, queries.get(i));
			list.add(root);
		}
		System.out.println("begin:");
		ExcutionTimer timer = new ExcutionTimer();
		ArrayList<Integer> results = new ArrayList<Integer>();
		for (int i = 0 ; i< list.size(); i++) {
			e.process(list.get(i));
			results.add(0);
		}
		for (int j = 0; j < 10; j++) {
		for (int i = 0 ; i< list.size(); i++) {
			//System.out.println(i);
			timer.setStart();				
			e.process(list.get(i));
			timer.setEnd();
			int time =(int) (results.get(i) + timer.getTime());
			results.set(i, time);
			} 
		}
		try {
		PrintWriter printer =new PrintWriter(new FileWriter("dbres2.txt"));
		for (int x: results) {
			printer.println(x);
		}
		printer.close();
		}catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
