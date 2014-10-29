package twitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.lucene.document.DateTools;


public class dataproducer {
	public static void main (String[] args) {
		File index = new File("D:\\data\\twitter2");
		TwitterDataFileReader reader = new TwitterDataFileReader(index,
				TwitterStatus.COMPLEX_TWITTER_STATUS);
		
		try {
			//PrintWriter printer = new PrintWriter(new FileWriter("D:\\data\\twitterexp\\text.txt"));
			PrintWriter printer2 = new PrintWriter(new FileWriter("D:\\data\\twitterexp\\time.txt"));
			int count = 0;
			while (reader.hasNextStatus()) {
				TwitterStatus ts = reader.getNextTwitterStatus();
				//System.out.println(ts.getText());
				if (ts == null) continue;
				if (ts.isEnglishChar()) {
					//printer.println(ts.getText());
					Date d = Util.convertStringToDate(ts.getCreatedAt(), Util.twitterTimePattern);
					String day = DateTools.dateToString(d,DateTools.Resolution.DAY);
					
					printer2.println(day);
					count++;
				}
			}
			//printer.close();
			printer2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
