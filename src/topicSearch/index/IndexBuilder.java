package topicSearch.index;

import entitySearch.Configure;


public class IndexBuilder {
		public void build() {
			
			
//		KeywordDocumentIndex.buildIndex(Configure.indexDir);
//		DocTopicIndex.buildIndex(Configure.twitterTopicsID, Configure.indexDir);
//		TimeDocumentIndex.buildIndex(Configure.twitterTime, Configure.indexDir);
//		TimeKeywordDocumentIndex.buildIndex(Configure.indexDir);	
//		KeywordTopicDocumentIndex.buildIndex(Configure.twitterDocs, Configure.twitterTopicsID, Configure.indexDir);	
		TimeKeywordTopicIndex.buildIndex(Configure.twitterDocs, Configure.twitterTopicsID, Configure.twitterTime, Configure.indexDir);	
		//*/
//		DocumentTimeIndex.buildIndex(Configure.twitterTime, Configure.indexDir);
		
		
/*		EntityDocumentIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		DocumentEntityIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		TypeEntityDocumentIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		KeywordDocumentEntityIndex.buildIndex(Configure.docDir, Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		TypeKeywordDocumentIndex.buildIndex(Configure.docDir, Configure.entitydocumentDir + "id.txt", Configure.indexDir);
		TypeDocumentEntityIndex.buildIndex(Configure.entitydocumentDir + "id.txt", Configure.indexDir);
	 	TypeKeywordDocumentEntityIndex.buildIndex(Configure.docDir, Configure.entitydocumentDir  + "id.txt", Configure.indexDir);
		*/
		}
		public static void main (String[] args) {
			IndexBuilder index = new IndexBuilder();
			index.build();
		}
	
}
