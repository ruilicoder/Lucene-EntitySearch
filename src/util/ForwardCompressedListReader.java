package util;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class ForwardCompressedListReader {
	String path;
	String name;
	RandomAccessFile mapFile, blkFile;
	int size;
	public ForwardCompressedListReader(String path, String name) throws IOException{
		this.path=path;
		this.name=name;
		this.mapFile=new RandomAccessFile(path + name + ".map", "r");
		this.blkFile=new RandomAccessFile(path + name+ ".blk", "r");
		size=(int) (mapFile.length()/12);
	}
	
	public int size(){
		return size;
	}
		
	public byte[] get(int id) throws IOException{
		if (id<0 || id>=size) return null;
		int bid;
		long startOffset, end;
		try {
			mapFile.seek(id*12);
			bid=mapFile.readInt();
			startOffset=mapFile.readLong();
		} catch (EOFException e) {
			return null;
		}
		blkFile.seek(bid*8);
		end=blkFile.readLong();
		
		int nextBid;
		long nextOffset;
		try {
			nextBid=mapFile.readInt();
			nextOffset=mapFile.readLong();
			if (nextBid!=bid)
				nextOffset=end;
		} catch (EOFException e) {
			nextOffset=end;
		}
		
		int size=(int)(nextOffset-startOffset);
		String cmd;
		cmd= "dictzip -d -k -c -s "+startOffset+" -e "+size+" "+path + name + "-" + bid + ".idx.dz";
		Process proc=Runtime.getRuntime().exec(cmd);
		InputStream input=proc.getInputStream();
		byte[] buf=new byte[size];
		input.read(buf, 0, size);
		return buf;
	}
	
	public static void main(String[] args) throws IOException{
		ForwardCompressedListReader reader=new ForwardCompressedListReader("./test/", "testDict");
		for (int i=reader.size()-1;i>=0;i--){
			byte[] bytes=reader.get(i);
			System.out.println(i+":"+bytes.length+":"+new String(bytes));
		}
	}
}
