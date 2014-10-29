package entitySearch.index;

import entitySearch.Configure;


public class IndexBuilder {
	public void build() {
		
		
	KeywordDocumentIndex.buildIndex(Configure.indexDir);
	EntityDocumentIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
	DocumentEntityIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
	TypeEntityDocumentIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
	KeywordDocumentEntityIndex.buildIndex(Configure.docDir, Configure.entitydocumentDir + "id.txt", Configure.indexDir);
	TypeKeywordDocumentIndex.buildIndex(Configure.docDir, Configure.entitydocumentDir + "id.txt", Configure.indexDir);
	TypeDocumentEntityIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
 	TypeKeywordDocumentEntityIndex.buildIndex(Configure.docDir, Configure.entitydocumentDir  + "id.txt", Configure.indexDir);
	
	}
	public static void main (String[] args) {
		IndexBuilder index = new IndexBuilder();
		index.build();
	}
}
