package topicSearch.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import util.StopWord;

public class TwitterAnalyzer extends StandardAnalyzer {
	public TwitterAnalyzer() {
		super(StopWord.stopwords);
	}


	public TokenStream reusableTokenStream(String fieldName, Reader reader)
			throws IOException {

		StandardTokenizer tokenStream = new StandardTokenizer(reader, false);
		TokenStream result = new StandardFilter(tokenStream);
		result = new LowerCaseFilter(result);
		result = new PorterStemFilter(result);
		return result;

	}
}
