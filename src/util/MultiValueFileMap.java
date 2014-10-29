package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiValueFileMap extends HashMap<String, String[]>{
	private static final long serialVersionUID = -6538971731616170475L;
	String filePath;
	public MultiValueFileMap(String filePath) throws IOException{
		this.filePath=filePath;
		BufferedFileReader r=new BufferedFileReader(filePath);
		String line;
		while ((line=r.readLine())!=null){
			if (line.equals("")) break;
			String[] keys=StringUtil.divide(line);
			line=r.readLine();
			String[] values=StringUtil.divide(line);
			for (int i=0;i<keys.length;i++)
				this.put(keys[i], values);
		}
		r.close();		
	}
}
