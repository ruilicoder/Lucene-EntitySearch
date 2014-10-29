package entitySearch.preprocess;

import entitySearch.Configure;

public class Preprocessor {
	public Preprocessor() {
		
	}
	public void Preprocess() {
		EntityMatcher.processDocumentSet(Configure.docDir, Configure.entitydocumentDir);
	}
	public static void main(String[] args) {
		Preprocessor pre = new Preprocessor();
		pre.Preprocess();
	}
}
