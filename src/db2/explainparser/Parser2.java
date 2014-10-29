package db2.explainparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import entitySearch.Configure;
import entitySearch.plan.Query;

public class Parser2 {

	public Node parse(String file, Query q) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int keywords = 0;
			while (!reader.readLine().contains("Optimizer Plan"))
				reader.readLine();
			String temp = "";
			Node root = null;
			JoinNode last = null;
			;
			Queue<JoinNode> queue = new LinkedList<JoinNode>();
			while ((temp = reader.readLine()) != null) {
				// System.out.println(temp);
				if (temp.contains("NLJOIN")) {
					JoinNode n = new JoinNode();
					n.JoinMethod = Parser.NEStEDLOOPJOIN;
					if (root == null) {
						root = n;
						queue.add(n);
					} else {
						queue.peek().leftChild = n;
						queue.add(n);
					}
				//	System.out.println("-----");

				//	printer(root);
				}
				if (temp.contains("HSJOIN")) {
					JoinNode n = new JoinNode();
					n.JoinMethod = Parser.HASHJOIN;
					if (root == null) {
						root = n;
						queue.add(n);
					} else {
						queue.peek().leftChild = n;
						queue.add(n);
					}
				//	System.out.println("-----");

				//	printer(root);

				}
				if (temp.contains(AccessNode.KeywordIndex)) {
					int index = temp.indexOf(AccessNode.KeywordIndex);
					String temp2 = temp.substring(index
							+ AccessNode.KeywordIndex.length());
					if (temp2.contains(AccessNode.KeywordIndex)) {
						AccessNode an = new AccessNode();

						KeywordIndexAccess kia = new KeywordIndexAccess();
						an.child = kia;

						an.columnID = new int[1];
						an.columnID[0] = Node.DOCID;
						an.setKey(q.keywords[keywords]);
						keywords++;

						if (root == null) {
							root = an;
						} else {
							if (queue.isEmpty()) {
								last.leftChild = an;
								break;
							} else {
								queue.peek().rightNode = an;
								last = queue.remove();
							}
						}
			//			System.out.println("-----");
						
			//			printer(root);
					}
					AccessNode an = new AccessNode();
					an.columnID = new int[1];
					an.columnID[0] = Node.DOCID;
					an.setKey(q.keywords[keywords]);
					keywords++;

					KeywordIndexAccess kia = new KeywordIndexAccess();
					an.child = kia;
					if (root == null) {
						root = an;
					} else {
						if (queue.isEmpty()) {
							last.leftChild = an;
							break;
						} else {
							queue.peek().rightNode = an;
							last = queue.remove();
						}
					}
					
	//				System.out.println("-----");

		//			printer(root);

				}
				if (temp.contains(AccessNode.KeywordIndex)) {

				}
				if (temp.contains(AccessNode.EntityTypeIndex)) {
					TypeEntityAccess te = new TypeEntityAccess();
					AccessNode an = new AccessNode();
					an.child = te;
					an.columnID = new int[1];
					an.columnID[0] = Node.ENTITY;
					an.setKey(String.valueOf(q.type));
					
					if (root == null) {
						root = an;
					} else {
						if (queue.isEmpty()) {
							last.leftChild = an;
						} else {
							queue.peek().rightNode = an;
							last = queue.remove();
						}
					}
					
	//				System.out.println("-----");

//					printer(root);

				}
				if (temp.contains(AccessNode.DocumentEntityIndex)) {
					AccessNode an = new AccessNode();
					DocumentEntityIndexAccess deia = new DocumentEntityIndexAccess();
					an.child = deia;
					an.columnID = new int[1];
					an.columnID[0] = Node.ENTITY;
					an.setKey("?");
					
					if (root == null) {
						root = an;
					} else {
						if (queue.isEmpty()) {
							last.leftChild = an;
						} else {
							queue.peek().rightNode = an;
							last = queue.remove();
						}
					}
					System.out.println("-----");
//					printer(root);

				}

			}
			return root;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	int ii= 0;
/*	public void printer(Node n) {
		if (n instanceof AccessNode) {
			System.out.println("table scan");
			if (((AccessNode) n).child instanceof KeywordIndexAccess) {
				System.out.println(i++ + " keyword access node"
						+ ((AccessNode) n).key);
			}
			if (((AccessNode) n).child instanceof TypeEntityAccess) {
				System.out.println(i++ + " Entity Access" + ((AccessNode) n).key);
			}
			if (((AccessNode) n).child instanceof DocumentEntityIndexAccess) {
				System.out.println(i++ +" document access" + ((AccessNode) n).key);
			}
			return;
		}
		if (n instanceof JoinNode) {
			
			printer(((JoinNode) n).leftChild);
			printer(((JoinNode) n).rightNode);
			System.out.println(i++  + " " + ((JoinNode) n).JoinMethod);
			return;
		}
		System.out.println("error" + n);
	}*/
	public void printSequence(Node n) {
		
		if (n instanceof AccessNode) {
			//System.out.println("table scan");
			if (((AccessNode) n).child instanceof KeywordIndexAccess) {
				
				System.out.print(ii + "K");
				ii++;
			}
			if (((AccessNode) n).child instanceof TypeEntityAccess) {
				
				System.out.print( "E");
			//	ii++;
				
			}
			if (((AccessNode) n).child instanceof DocumentEntityIndexAccess) {
				System.out.print("D");
			//	ii++;
				
			}
			return;
		}
		if (n instanceof JoinNode) {
			
			printSequence(((JoinNode) n).leftChild);			
			System.out.print(((JoinNode) n).JoinMethod.charAt(0));			
		//	ii++;
			printSequence(((JoinNode) n).rightNode);
			return;		
		}
		System.out.println("error" + n);
	}
	public static void main(String[] args) {
		Parser2 p = new Parser2();
		ArrayList<Query> queries = Query.loadQuery(Configure.indexDir +  "query.txt" );
		// System.out.println(p.root instanceof AccessNode);
		for (int i = 0 ; i < 368; i++) {
			Node root = p.parse("D:\\sigmodexp\\entity2\\explain" + i, queries.get(i));
			//list.add(root);
			System.out.print(i + "\t");

			p.printSequence(root);
			p.ii = 0;
			System.out.println();
		}
	/*	String[] keywords = {"project","hardwar"};
	 Query q = new Query(keywords,34);
		Node n = p.parse("D:\\sigmodexp\\entity2\\explain1",q);
		System.out.println("**************");
		p.printSequence(n); */
	}
}
