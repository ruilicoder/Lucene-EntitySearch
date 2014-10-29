package entitySearch.index.analyzer;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Payload;

import util.ByteConvert;
import util.ByteList;
import entitySearch.Configure;
import entitySearch.index.EntityTypeIndex;

public class TypeEntityTokenizer  extends TypeTokenizer{
	public TypeEntityTokenizer(String content, EntityTypeIndex eti, ArrayList<Integer> entities)
	{	
		super(content,eti);
		this.entites  =  entities;
		
	}
	ArrayList<Integer> entites;
	//byte[] bytes;
	//ArrayList<byte[]> byteslist;;
	public Token next() throws IOException
	{
		
		if(current<list.size())
		{
			ByteList bl = new ByteList();
			for (Integer eid : entites) {
				int typeID = eti.getTypeID(eid);
				String str = list.get(current); 
				Integer tid = Integer.parseInt(str);
				if (typeID  == tid) {
					bl.append(ByteConvert.convertToByte(eid));
				}			
			}
			
			byte[] bytes = bl.toBytes();
			Token t=new Token();
			t.setTermText(list.get(current));
			Payload payload = new Payload();
			payload.setData(bytes);
			t.setPayload(payload);
			//t.setType(this.field);
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
		EntityTokenizer ets = new EntityTokenizer("1	2	3	4");
		byte[] bytes = ets.bytelist.toBytes();
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		TokenStream ts=new TypeEntityTokenizer("1	2	3	4", eti, list);
		Token t;
		try {
			while((t=ts.next()) != null) {
				System.out.println(t.termText());
				System.out.println(t.getPayload());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

