package util;

import java.util.ArrayList;

public class ByteList extends ArrayList<Byte> {
	public void append(byte[] bytes){
		for (int i=0;i<bytes.length;i++)
			add(bytes[i]);
	}
	public byte[] toBytes(){
		byte[] bytes=new byte[size()];
		for (int i=0;i<bytes.length;i++)
			bytes[i]=get(i);
		return bytes;
	}
}
