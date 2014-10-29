package entitySearch.plan;

import util.ExcutionTimer;
import entitySearch.Configure;
import entitySearch.index.DocumentEntityIndex;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.KeywordDocumentEntityIndex;
import entitySearch.index.KeywordDocumentIndex;
import entitySearch.index.TypeDocumentEntityIndex;
import entitySearch.index.TypeEntityDocumentIndex;
import entitySearch.index.TypeKeywordDocumentEntityIndex;
import entitySearch.index.TypeKeywordDocumentIndex;

public class InsertTime {
	ExcutionTimer te;
	EntityTypeIndex eti;
	public InsertTime() {
		 te = new ExcutionTimer();
		 eti = new EntityTypeIndex(Configure.entityList);
		
		
	}
	
	public void warmup() {
		te.setStart();
		KeywordDocumentIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.indexDir);
		te.setEnd();
		long kds = te.getTime();
	//	System.out.println("kds \t" + kds);
		te.setStart(); 
		
		DocumentEntityIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		te.setEnd();
		long des = te.getTime();
		DocumentEntityIndex dei = new DocumentEntityIndex(Configure.indexDir);
		
	//	System.out.println("des \t" + des);
		
		te.setStart(); 		
		TypeEntityDocumentIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();		
		long teds  = te.getTime();
		//System.out.println("teds \t" + teds); 
		
		te.setStart();
		TypeKeywordDocumentIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();
		long tkds = te.getTime();
		
		//System.out.println("tkds\t"  + tkds);
	
		
		te.setStart();
		TypeKeywordDocumentEntityIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();
		
		long tkdes = te.getTime();		
		//System.out.println("tkdes\t" + tkdes); 
		
		
		te.setStart();
		TypeDocumentEntityIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();
		
		long tdes = te.getTime();		
		//System.out.println("tdes\t" + tdes);
		
		
		double b1 = kds + des ;
		double b2 = kds + teds;
		double p1 = tkds + tdes;
		double p2 = tkdes + tkds;
		double p3 = tkdes;
		double p4 = kds + tdes;  
		//System.out.println(b1 + "\t" + b2 + "\t" + p1 + "\t" + p2 + "\t" + p3 + "\t" + p4);
	}
	public void testInsert() {
		
		te.setStart();
		KeywordDocumentEntityIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		te.setEnd();
		long kdes = te.getTime();
		System.out.println("kds \t" + kdes);
			
	/*te.setStart();
		KeywordDocumentIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.indexDir);
		te.setEnd();
		long kds = te.getTime();
	//	System.out.println("kds \t" + kds);
		te.setStart(); 
		
		DocumentEntityIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		te.setEnd();
		long des = te.getTime();
		DocumentEntityIndex dei = new DocumentEntityIndex(Configure.indexDir);
		
	//	System.out.println("des \t" + des);
		
		te.setStart(); 		
		TypeEntityDocumentIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();		
		long teds  = te.getTime();
		//System.out.println("teds \t" + teds); 
		
		te.setStart();
		TypeKeywordDocumentIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();
		long tkds = te.getTime();
		
		//System.out.println("tkds\t"  + tkds);
	
		
		te.setStart();
		TypeKeywordDocumentEntityIndex.insert(Configure.entitydocumentDir + "text.txt", Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();
		
		long tkdes = te.getTime();		
		//System.out.println("tkdes\t" + tkdes); 
		
		
		te.setStart();
		TypeDocumentEntityIndex.insert(Configure.entitydocumentDir + "id.txt", Configure.indexDir, eti);
		te.setEnd();
		
		long tdes = te.getTime();		
		//System.out.println("tdes\t" + tdes);
		
		
		
		double b1 = kds + des ;
		double b2 = kds + teds;
		double p1 = tkds + tdes;
		double p2 = tkdes + tkds;
		double p3 = tkdes;
		double p4 = kds + tdes;  
		System.out.println(b1 + "\t" + b2 + "\t" + p1 + "\t" + p2 + "\t" + p3 + "\t" + p4);
	*/
	}
	
	public static void main(String[] args) {
		InsertTime it = new InsertTime();
		//it.warmup();
		for (int i = 0 ; i < 10; i++) {
			it.testInsert();
		}
		
	}
}
