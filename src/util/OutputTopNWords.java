package util;

import java.io.IOException;
import java.io.PrintStream;

public class OutputTopNWords {
	String fileName;
	int n;
	Counter c;
	public OutputTopNWords(String fileName, int n){
		this.fileName=fileName;
		this.n=n;
		c=new Counter();
	}
	public void add(String s){
		c.addTerm(s);
		if (c.size()>n*5) c.truncate(n*2);
	}
	public void finish() throws IOException{
		c.truncate(n);
		PrintStream output=new PrintStream(fileName);
		PairList<String, Integer> list=c.getSortedList();
		for (int i=0;i<list.size();i++)
			output.println(list.getFirst(i));
		output.close();
	}
}
