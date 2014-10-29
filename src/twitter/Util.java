package twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
public static final String twitterTimePattern = "EEE MMMM dd hh:mm:ss Z yyyy";

public static  Date convertStringToDate(String time, String pattern) {
	SimpleDateFormat sdf=new SimpleDateFormat(pattern, Locale.US);		
	try {
	Date d = sdf.parse(time);
	return d;
	} catch (ParseException e){	//System.out.println(d.toGMTString());
		//System.out.println(DateTools.dateToString(d,DateTools.Resolution.HOUR));
		e.printStackTrace();
	}
	return null;

}
}



