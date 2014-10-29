package db2.explainparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	String file;
	static String ACCESSTABLE = "Access Table Name";
	static String HASHJOIN = "Hash Join";
	static String NEStEDLOOPJOIN = "Nested Loop Join";
	static String RETURN = "Return Data Completion";


	Node root;
	public Parser(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (!reader.readLine().contains("Estimated Cardinality")) 
			reader.readLine();
			String temp = "";
			Node current= null;
			Node parant  = null;
			while((temp = reader.readLine()).compareTo("Return Data Completion") != 0) {
				
				if (temp.contains("Access Table Name")) {
					Node node = new AccessNode();
					parant = current;
					current = node;
					root = node;
					if (parant != null && parant instanceof JoinNode) {
						((JoinNode)parant).rightNode = node;
						root  = parant;
					}
				//	System.out.println("access" + temp);
				//	printer(root);
					
				}
				if (temp.contains(this.HASHJOIN) && !temp.contains("Table")) {
					//System.out.println("come to join Hash" + temp);
					JoinNode node = new JoinNode();
					node.leftChild = root;
					node.JoinMethod = node.HASH_JOIN;
					current = node;
					root = current;	
					
					parant = null;
					
				}
				if (temp.contains(this.NEStEDLOOPJOIN)) {
					//System.out.println("come to join nested"  + temp);
					JoinNode node = new JoinNode();
					node.leftChild = root;
					current = node;
					root = current;
					node.JoinMethod = this.NEStEDLOOPJOIN;
					//printer(root);
					
					parant = null;
				}
				
				if (temp.contains(AccessNode.KeywordIndex)) {
					if (current instanceof AccessNode) {
						KeywordIndexAccess ki = new KeywordIndexAccess();
						((AccessNode) current).child  = ki;
						current.columnID = new int[1];
						current.columnID[0] = Node.DOCID;
					} else {
						System.err.println("parsing error in index: " + temp);					
					}
				}
				if (temp.contains(AccessNode.EntityTypeIndex)) {
					if (current instanceof AccessNode) {
						TypeEntityAccess te = new TypeEntityAccess();
						((AccessNode) current).child  = te;
					} else {
						System.err.println("parsing error in index: " + temp);					
					}
					current.columnID = new int[1];
					current.columnID[0] = Node.ENTITY;

				}
				if (temp.contains(AccessNode.DocumentEntityIndex)) {
					if (current instanceof AccessNode) {
						DocumentEntityIndexAccess dei = new DocumentEntityIndexAccess();
						((AccessNode) current).child  = dei;
					} else {
						System.err.println("parsing error in index: " + temp);					
					}

					current.columnID = new int[1];
					current.columnID[0] = Node.ENTITY;
				}
				if (temp.contains("Start Key:")) {
					//System.out.println(temp);
					temp = reader.readLine();
					temp = reader.readLine();
					//System.out.println(temp);
						int index = temp.indexOf(":") ;
						String key = temp.substring(index+1).trim();
						key = key.replaceAll("'","");
						//System.out.println("key:" + key);
						if (current instanceof AccessNode) {
							((AccessNode) current).setKey(key);
						}
					
				}
						
			}
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void printer(Node n) {
		if (n instanceof AccessNode) {
			System.out.println("table scan");
		if (((AccessNode) n).child instanceof KeywordIndexAccess) {
				System.out.println(" keyword access node" + 	((AccessNode) n).key
						 );				
						}
			if (((AccessNode) n).child instanceof TypeEntityAccess) {
				System.out.println(" Entity Access" + 	((AccessNode) n).key
						 );				
			}
			if (((AccessNode) n).child instanceof DocumentEntityIndexAccess) {
				System.out.println(" document access" + 	((AccessNode) n).key
						);
			}			
			return; 
		} 
		if (n instanceof JoinNode) {
			printer(((JoinNode) n).leftChild);
			System.out.println(((JoinNode) n).JoinMethod);
			printer(((JoinNode) n).rightNode);
			return ;
		}
		System.out.println("error" + root);
	}
	public static void main(String[] args) {
		Parser p = new Parser("D:\\sigmodexp\\entity2\\explain5");
		//System.out.println(p.root instanceof AccessNode);
try{		
		BufferedReader reader = new BufferedReader(new FileReader("D:\\sigmodexp\\entity2\\explain5"));
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		System.out.println("test");
		reader.mark(10000);
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		reader.reset();
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		
} catch (IOException e) {	
	e.printStackTrace();
}
	//	p.printer(p.root);
	}

}
