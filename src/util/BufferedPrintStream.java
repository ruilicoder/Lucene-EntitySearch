package util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class BufferedPrintStream extends PrintStream {

	public BufferedPrintStream(String fileName) throws FileNotFoundException {
		super(new BufferedOutputStream(new FileOutputStream(fileName)));
	}

}
