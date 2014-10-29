package entitySearch.index.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import util.StopWord;
import entitySearch.index.EntityTypeIndex;
import entitySearch.index.Fields;

public class EntityAnalyzer extends StandardAnalyzer {
	public EntityAnalyzer() {
		super(StopWord.stopwords);
	}

	EntityTypeIndex eti;

	public EntityAnalyzer(EntityTypeIndex kti) {
		this.eti = kti;
	}

	public TokenStream reusableTokenStream(String fieldName, Reader reader)
			throws IOException {

		if (fieldName.compareTo(Fields.TYPE) == 0) {
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
				StandardTokenizer tokenStream = new StandardTokenizer(reader,
						false);
				return tokenStream;
			}
			//EntityTokenizer ets = new EntityTokenizer(sb.toString());
			//byte[] bytes = ets.bytelist.toBytes();

			String content = sb.toString();
			ArrayList<Integer> list = new ArrayList<Integer>();
			String[] args = content.split("\t");
			for (String id : args) {
				list.add(Integer.parseInt(id));
			}
			TokenStream tokenStream = new TypeEntityTokenizer(sb.toString(),
					this.eti, list);
			return tokenStream;
		}
		StandardTokenizer tokenStream = new StandardTokenizer(reader, false);
		TokenStream result = new StandardFilter(tokenStream);
		result = new LowerCaseFilter(result);
		result = new PorterStemFilter(result);
		return result;

	}

}
