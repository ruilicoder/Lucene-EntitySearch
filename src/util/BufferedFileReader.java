package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedFileReader extends BufferedReader {

	public BufferedFileReader(String fileName) throws IOException {
		super(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
	}

}
