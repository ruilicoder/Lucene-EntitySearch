package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {
	public static void writeObject(Object obj, String path) throws IOException{
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(obj);
		oos.close();
	}
	public static Object readObject(String path) throws IOException, ClassNotFoundException{
		ObjectInputStream iis=new ObjectInputStream(new FileInputStream(path));
		Object obj=iis.readObject();
		iis.close();
		return obj;
		
	}
	public static void main(String[] args) throws Exception{
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("./test/ttest"));
		oos.writeObject(new String("aa"));
		oos.writeObject(new Integer(1210));
		oos.writeObject(null);
		oos.close();
		
		ObjectInputStream iis=new ObjectInputStream(new FileInputStream("./test/ttest"));
		System.out.println(iis.readObject());
		System.out.println(iis.readObject());
		System.out.println(iis.readObject());
		
		iis.close();
	}
	
	
}
