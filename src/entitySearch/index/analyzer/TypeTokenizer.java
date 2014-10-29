package entitySearch.index.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;

public class TypeTokenizer  extends TokenStream{
		ArrayList<String> list;
		EntityTypeIndex eti;
		public TypeTokenizer(String content, EntityTypeIndex eti)
		{	
			String[] args =  content.split("\t");
			
			HashSet<String> set = new HashSet<String>();
			
			for (String arg : args) {
			//	System.out.println("category:" + arg);
				Integer categoryID = eti.getTypeID(Integer.parseInt(arg));
				set.add(categoryID.toString());
			}
			list = new ArrayList<String>(set);
			this.eti = eti;
		}
		int current=0;
		public Token next() throws IOException
		{
			
			if(current<list.size())
			{
				Token t=new Token();
				t.setTermText(list.get(current));
				current++;
				return t;
			}
			else
			{
				return null;
			}
		}
		public void reset() throws IOException {}
		{
			current=0;
		}
		public static void main(String[] args)
		{
			EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
			TokenStream ts=new TypeTokenizer("1	2	3	4", eti );
			Token t;
			try {
				while((t=ts.next()) != null) {
					System.out.println(t.termText());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
