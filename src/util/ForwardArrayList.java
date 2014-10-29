package util;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ForwardArrayList {

	String path;
	String name;
	RandomAccessFile mapFile;
	RandomAccessFile dataFile;

	// Store the forward list in the file
	public ForwardArrayList(String path, String name, boolean createNew)
			throws IOException {
		this.path = path;
		this.name = name;
		if (createNew) {
			new File(path + name + ".map").delete();
			new File(path + name + ".idx").delete();
		}
		mapFile = new RandomAccessFile(path + name + ".map", "rw");
		dataFile = new RandomAccessFile(path + name + ".idx", "rw");
	}

	public void append(byte[] data) throws IOException {
		append(data, 0, data.length);
	}

	public void append(byte[] data, int offset, int length) throws IOException {
		mapFile.seek(mapFile.length());
		dataFile.seek(dataFile.length());
		mapFile.writeLong(dataFile.length());
		dataFile.write(data, offset, length);
	}

	public byte[] get(int id) throws IOException {
		long startOffset, endOffset;
		try {
			mapFile.seek(id * 8);
			startOffset = mapFile.readLong();
		} catch (EOFException e) {
			return null;
		}
		try {
			endOffset = mapFile.readLong();
		} catch (EOFException e) {
			endOffset = dataFile.length();
		}
		byte[] result = new byte[(int) (endOffset - startOffset)];
		dataFile.seek(startOffset);
		dataFile.read(result);
		return result;
	}
	
	public static boolean exists(String path, String name){
		return new File(path + name + ".map").exists();
	}
	
	public int size() throws IOException {
		return (int)(mapFile.length()/8);
	}
	
	public void close() throws IOException{
		mapFile.close();
		dataFile.close();
	}

	public static void main(String[] args) throws IOException {
		ForwardArrayList list = new ForwardArrayList("./test/", "test", true);
		/*
		 * for (int i=0;i<100;i++){ list.append(("fdasfaaaa"+i).getBytes()); }
		 */
		for (int i = 103; i >= 0; i--) {
			byte[] bytes = list.get(i);
			if (bytes == null)
				System.out.println("~~~");
			else {
				String s = new String(bytes);
				System.out.println(s);
			}
		}
	}
}