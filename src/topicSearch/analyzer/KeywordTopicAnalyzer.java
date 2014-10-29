package topicSearch.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import twitter.TwitterByteConvertor;
import util.ByteList;
import util.StopWord;
import entitySearch.index.analyzer.EntityTokenFilter;

public class KeywordTopicAnalyzer extends StandardAnalyzer {
	public KeywordTopicAnalyzer() {
		super(StopWord.stopwords);
	}

	public TokenStream reusableTokenStream(String fieldName, Reader reader)
			throws IOException {

		StringBuilder sb = new StringBuilder();
		int c;
		try {
			while ((c = reader.read()) != -1) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(s);
		if (sb.length() == 0) {
			StandardTokenizer tokenStream = new StandardTokenizer(reader, false);
			return tokenStream;
		}
		String content = sb.toString();
		String[] args = content.split(" entityrli ");

		if (args.length == 2) {
			String[] topics = args[1].split("\t");
			ByteList bytelist = new ByteList();
			for (String topic : topics) {
				byte[] bytes = TwitterByteConvertor.convertToByte(Integer
						.parseInt(topic));
				bytelist.append(bytes);
			}
			byte[] bytes = bytelist.toBytes();

			TokenStream result = new StandardTokenizer(
					new StringReader(args[0]), false);
			result = new LowerCaseFilter(result);
			result = new PorterStemFilter(result);

			TokenStream tokenStream = new EntityTokenFilter(result, bytes);

			return tokenStream;
		} else {
			StandardTokenizer tokenStream = new StandardTokenizer(reader, false);
			TokenStream result = new StandardFilter(tokenStream);
			result = new LowerCaseFilter(result);
			result = new PorterStemFilter(result);
			return result;
		}
	}

}
