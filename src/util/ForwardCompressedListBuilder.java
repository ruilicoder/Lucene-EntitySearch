package util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class ForwardCompressedListBuilder {

	// Need the support of dictzip.

	String path;
	String name;
	RandomAccessFile mapFile;
	RandomAccessFile blkFile;
	BufferedOutputStream dataFile;
	int blockId;
	long pointer;

	public ForwardCompressedListBuilder(String path, String name)
			throws FileNotFoundException {
		this.path = path;
		this.name = name;
		this.blockId = 0;
		this.pointer = 0;
		mapFile = new RandomAccessFile(path + name + ".map", "rw");
		blkFile = new RandomAccessFile(path + name + ".blk", "rw");
		dataFile = null;
	}

	// Create a compression block.
	public void createBlock() throws IOException {
		if (dataFile != null)
			throw new IOException("Run closeBlcok() before create a new block.");
		dataFile = new BufferedOutputStream(new FileOutputStream(path + name
				+ "-" + blockId + ".idx"));
		pointer = 0;
	}

	// Close a compression block.
	public void closeBlock() throws IOException {
		blkFile.writeLong(pointer);
		if (dataFile == null)
			throw new IOException("createBlock() first.");
		dataFile.close();

		Process proc = Runtime.getRuntime().exec(
				"dictzip " + path + name + "-" + blockId + ".idx");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				proc.getErrorStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		reader.close();

		blockId++;
		dataFile = null;
	}

	public void append(byte[] data) throws IOException {
		append(data, 0, data.length);
	}

	public void append(byte[] data, int offset, int length) throws IOException {
		if (dataFile == null)
			throw new IOException("createBlock() first.");

		mapFile.writeInt(blockId);
		mapFile.writeLong(pointer);
		dataFile.write(data, offset, length);
		pointer += length;
	}

	// Close the list.
	public void close() throws IOException {
		if (dataFile != null)
			closeBlock();
		mapFile.close();
		blkFile.close();
	}

	public static void main(String[] args) throws IOException {
		ForwardCompressedListBuilder builder = new ForwardCompressedListBuilder(
				"./test/", "testDict");
		for (int i = 0; i < 10; i++) {
			builder.createBlock();
			for (int k = 0; k < 20+i; k++) {
				String s = i + "-------" + k;
				builder.append(s.getBytes());
			}
			builder.closeBlock();
		}
		builder.close();
	}

}
