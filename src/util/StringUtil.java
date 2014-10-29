package util;

import java.util.ArrayList;

public class StringUtil {
	
	public static Pair<String,Integer> byteToString(byte[] bytes, int offset){
		Pair<Integer,Integer> p=NumberUtil.unsignedBytesToInt(bytes, offset);
		offset+=p.second;
		String s=new String(bytes,offset,p.first);
		return new Pair<String,Integer>(s, p.second+p.first);
	}
	
	public static byte[] stringToByte(String s){
		ByteList l=new ByteList();
		byte[] bytes=s.getBytes();
		l.append(NumberUtil.unsignedIntToBytes(bytes.length));
		l.append(s.getBytes());
		return l.toBytes();
	}

	public static String merge(String[] strs){
		StringBuffer result=new StringBuffer();
		for (int i=0;i<strs.length;i++){
			String s=clearString(strs[i]).replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,");
			if (i==0)
				result.append(s);
			else
				result.append(","+s);
		}
		return result.toString();
	}	
	
	public static String clearString(String s){
		StringBuffer buf=new StringBuffer(s);
		for (int i=0;i<buf.length();i++)
			if (!(buf.charAt(i)>=32 && buf.charAt(i)<=126)) buf.setCharAt(i, ' ');
		return buf.toString();
	}
	public static String[] divide(String s){
		ArrayList<String> list=new ArrayList<String>();
		StringBuffer c=new StringBuffer();
		for (int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			if (ch=='\\'){
				assert(i<s.length()-1);
				if (s.charAt(i+1)=='\\')
					c.append('\\');
				else if (s.charAt(i+1)==',')
					c.append(',');
				else
					assert(false);
				i=i+1;
			}
			else if (ch==','){
				list.add(c.toString());
				c=new StringBuffer();
			}
			else{
				c.append(ch);
			}
		}
		list.add(c.toString());
		return list.toArray(new String[0]);
	}
	
	public static String toString(Object[] objs){
		StringBuffer buf=new StringBuffer();
		for (int i=0;i<objs.length;i++){
			if (i!=0) buf.append(",");
			buf.append(objs[i]);
		}
		return buf.toString();
	}
	
	public static String toString(ArrayList objs){
		StringBuffer buf=new StringBuffer();
		for (int i=0;i<objs.size();i++){
			if (i!=0) buf.append(",");
			buf.append(objs.get(i));
		}
		return buf.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(clearString(""));
	}

}
