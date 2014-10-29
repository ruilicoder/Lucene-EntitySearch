package entitySearch.index.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Payload;

public class EntityTokenFilter extends TokenStream{
	TokenStream ts;
	byte[] bytes;
	public EntityTokenFilter(TokenStream ts, byte[] bytes) {
		this.ts = ts;
		this.bytes = bytes;
	}
	public Token next() throws IOException
	{
		
			Token t=ts.next();
			if (t != null) {
				Payload payload = new Payload();
				payload.setData(bytes);
				t.setPayload(payload);
			}
			return t;
	}
}
