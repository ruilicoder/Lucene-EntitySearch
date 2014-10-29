package entitySearch.index.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import util.StopWord;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.Fields;

public class KeywordEntityAnalyzer extends StandardAnalyzer {
	public KeywordEntityAnalyzer() {
		super (StopWord.stopwords);
	}
	

	public TokenStream reusableTokenStream(String fieldName, Reader reader)
			throws IOException {
			
			if (fieldName.compareTo(Fields.TEXT) == 0) {
					StringBuilder sb = new StringBuilder();
					
					int c ;
					try {
						while( (c=reader.read()) != -1) {
							sb.append((char)c);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(s);
					if (sb.length() == 0) {
						StandardTokenizer tokenStream = new StandardTokenizer(reader, false);
						return tokenStream;
					}
					String content = sb.toString();
					String[] args = content.split(" entityrli ");
					
					if (args.length == 2) {
					EntityTokenizer ets = new EntityTokenizer(args[1]);
					byte[] bytes = ets.bytelist.toBytes();
					
					TokenStream result = new StandardTokenizer(new StringReader(args[0]),false);
					result = new LowerCaseFilter(result);
					result = new PorterStemFilter(result);
							
					TokenStream tokenStream = new EntityTokenFilter(result,bytes);
					
					return tokenStream; 
					}
					return null;
				}
				StandardTokenizer tokenStream = new StandardTokenizer(reader, false);
				TokenStream result = new StandardFilter(tokenStream);
				result = new LowerCaseFilter(result);
				result = new PorterStemFilter(result);
				
				return result;
				
		}

}

