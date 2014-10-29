package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class StringMap {
	HashMap<String, Integer> map;
	ArrayList<String> list;
	FileWriter writer = null;

	public StringMap(String filePath, boolean allowAdding) throws IOException {
		map = new HashMap<String, Integer>();
		list = new ArrayList<String>();
		File f = new File(filePath);
		if (!f.exists()) {
			System.out.println(f);
			f.createNewFile();
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"utf-8"));
		String line;
		int id = 0;
		while ((line = r.readLine()) != null) {
			map.put(line, id);
			list.add(line);
			id++;
		}
		r.close();
		if (allowAdding) {
			writer = new FileWriter(filePath, true);
		}
	}

	public String getString(int id) {
		if (id < 0 || id >= list.size())
			return null;
		return list.get(id);
	}

	public Integer getId(String str) {
		return map.get(str);
	}

	public void addString(String str) throws IOException {
		assert (writer != null);
		map.put(str, map.size());
		list.add(str);
		writer.write(str + "\n");
		writer.flush();
	}

	
	// If the string contained in the map, return the id with m bytes, otherwise
	// store the whole string.
	public byte[] compressString(String s, int m) {
		assert (m >= 1 && m <= 4);
		Integer id = map.get(s);
		byte[] result;
		if (id == null || id < -1 || id > NumberUtil.pow(2, m * 8 - 1) - 1) {
			// Store the whole string;
			if (s.length() > 127)
				s = s.substring(0, 127);
			char[] chs = s.toCharArray();
			result = new byte[chs.length + 1];

			result[0] = (byte) (s.length() | 128);
			for (int i = 0; i < chs.length; i++)
				result[i + 1] = (byte) chs[i];
		} else {
			byte[] b = NumberUtil.intToBytes(id);
			result = new byte[m];
			for (int i = 0; i < m; i++)
				result[i] = b[4 - m + i];
		}
		return result;
	}
	// Return the string together with the number of bytes it uses.
	public Pair<String, Integer> decompressString(byte[] bytes, int offset, int m) {
		assert (m >= 1 && m <= 4);
		if ((bytes[offset] & 128) == 0) {
			int id = NumberUtil.compressedBytesToInt(bytes, offset, m);
			assert (id >= 0 && id < list.size());
			return new Pair<String,Integer>(list.get(id),m);
		} else {
			int len=bytes[offset]&127;
			String s=new String(bytes,offset+1,len);
			return new Pair<String,Integer>(s,1+len);
		}
	}
	
	public int size(){
		return map.size();
	}
	
//	
//	
//	public static void main(String[] args) throws IOException{
//		StringMap map=new StringMap(Constant.DATASET_DATA_LOCATION+"text/term", false);
//		/*for (int i=0;i<map.size();i++){
//			byte[] b=map.compressString(map.getString(i), 2);
//			Pair<String, Integer> p=map.decompressString(b, 0, 2);
//			System.out.println(map.getString(i)+" "+p.first+" "+p.second);
//		}*/
//		String s="(d";
//		byte[] b=map.compressString(s, 2);
//		Pair<String, Integer> p=map.decompressString(b, 0, 2);
//		System.out.println(s+" "+p.first+" "+p.second);
//	}
}
