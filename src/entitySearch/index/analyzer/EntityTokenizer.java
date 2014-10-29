package entitySearch.index.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import util.ByteConvert;
import util.ByteList;
import entitySearch.index.EntityTypeIndex;


public class EntityTokenizer  extends TokenStream{
	ArrayList<String> list;
	String field;
	EntityTypeIndex eti;
	public EntityTokenizer(String content)
	{	//StandardTokenizer st = new StandardTokenizer();
		String[] args =  content.split("\t");
		
		HashSet<String> set = new HashSet<String>();
		
		for (String arg : args) {
			set.add(arg);
		}
		list = new ArrayList<String>(set);		
		bytelist = new ByteList();
		
		for (String id : list) {
			byte[] bytes = ByteConvert.convertToByte(Integer.parseInt(id));
			bytelist.append(bytes);			
		}
	}
	ByteList bytelist;
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
///		EntityTypeIndex eti = new EntityTypeIndex(Configure.entityList);
		EntityTokenizer ts=new EntityTokenizer("1	2	3	4");
		Token t;
		try {
			while((t=ts.next()) != null) {
				System.out.println(t.termText());
			}		
		int [] array = ByteConvert.convertToIntArray(ts.bytelist.toBytes());
		 for (int x : array) {
			 System.out.println(x);
		 }
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

