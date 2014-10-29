package entitySearch.preprocess;

import java.io.File;
import java.io.IOException;

import util.BufferedFileReader;
import util.StringUtil;
import entitySearch.Configure;



public class TextReader {
	
	int doc;
	int fileID;
	BufferedFileReader reader;
	String url;
	String content;
	String dir; 
	public TextReader(String dir){
		fileID=-1;
		doc=-1;
		reader=null;
		this.dir = dir;
	}
	
	public boolean nextDoc() throws IOException{
		String headLine=reader.readLine();
		String contentLine=reader.readLine();
		if (headLine==null || contentLine==null){
			return false;
		}
		url=StringUtil.divide(headLine)[1];
		content=contentLine;
		doc++;
		return true;
		
	}
	public int getDoc(){
		return doc;
	}
	public String getDocContent(){
		return content;
	}
	public String getURL(){
		return url;
	}
	public boolean nextFile() throws IOException{
		fileID++;
		String fileStr=""+fileID;
		if (fileStr.length()==1) fileStr="0"+fileStr;
		if (!new File(dir+fileStr).exists()) return false;
		if (reader!=null)
			reader.close();
		reader=new BufferedFileReader(dir +fileStr);
		return true;		
	}

	public static void main(String[] args) throws IOException {
		TextReader r=new TextReader(Configure.docDir);
		while(r.nextFile()){
			while (r.nextDoc())
				System.out.println(r.getDoc()+" "+r.getURL());			
		}
	}

}
