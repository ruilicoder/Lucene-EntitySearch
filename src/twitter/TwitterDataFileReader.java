package twitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;

import twitter.TwitterStatus;

public class TwitterDataFileReader {
	private BufferedReader reader;
	private String type;
	private boolean label;
	private LinkedList<File> fileList;
	private String currentFileName;
	private int totalFileNum;
	private int totalTwitterStatus;
	public  TwitterDataFileReader(File  file, String type) {
		fileList = new LinkedList<File>();
		if (file.isDirectory()) {
			for (File f: file.listFiles()) {
				if (!f.isDirectory())
					fileList.add(f);
					System.out.println(f.toString());
			}
		} else {
			fileList.add(file);
		}		
		this.type  = type;
		Collections.sort(fileList);
		reader = getNextFileReader();
		this.totalFileNum = fileList.size();
	}
	private BufferedReader  getNextFileReader() {
		BufferedReader reader = null;
		while (reader == null) {
			String fileName = this.getNextFileName();
			System.out.println("Finish" + this.currentFileName);
			if (fileName == null) {
				break;
			} else {
				try {
					this.currentFileName = fileName;
					System.out.println("Begin process the new file " + this.currentFileName);
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
							
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						
			}
		}
		return reader;
	}
	private String getNextFileName() {
		File f = fileList.pollFirst();
		if (f != null) return f.getAbsolutePath();
		else return null;
	}
	public int getTotalStatus() {
		return this.totalTwitterStatus;
	}
		
	public boolean hasNextStatus() throws IOException {
		if (reader == null) return false;
		String content = reader.readLine();
	
		if (content == null) {			
			reader.close();
			reader = this.getNextFileReader();
			return this.hasNextStatus();
		}
		while (content.compareTo(TwitterStatus.BEGIN) != 0) {
			content = reader.readLine();
			if (content == null) {
				reader = this.getNextFileReader();
				return this.hasNextStatus();
			}
		}
		return true;
	}
	public TwitterStatus getNextTwitterStatus() {
		try {
			String content = reader.readLine();
			if (content == null) return null;
			long id = Long.parseLong(content);
	
			String text = reader.readLine();
			String createAt = reader.readLine();
			boolean isReplyTo = Boolean.parseBoolean(reader.readLine());	
			if (type.compareTo(TwitterStatus.SIMPLE_TWITTER_STATUS)==0) {
				TwitterStatus ts = new TwitterStatus(id, text, createAt, isReplyTo);
				this.totalTwitterStatus++;
				return ts;
			}
			if(type.compareTo(TwitterStatus.COMPLEX_TWITTER_STATUS) ==0) {
				boolean isF = Boolean.parseBoolean(reader.readLine());
				long userID = Long.parseLong(reader.readLine());
				long nfo = Long.parseLong(reader.readLine());
				long nfr = Long.parseLong(reader.readLine());
				long nfa = Long.parseLong(reader.readLine());				
				TwitterStatus ts = new TwitterStatus(id, text, createAt, isReplyTo,isF, userID, nfo, nfr, nfa);
				return ts;
			}			
		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
