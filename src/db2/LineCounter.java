package db2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LineCounter {
	public static void main(String[] args) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader("D:\\sigmodexp\\entitySearch\\V2KT.txt"));
		String temp = "";
		int count = 0; 
		while ((temp = reader.readLine()) != null) {
			 count ++;
			 if (count%1000000==0) System.out.println(count);
		 }
		System.out.println(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
