package util;

public class Util {
	public static int  bytesToInt(byte[] data, int x ) {
		int fromByte = 0;
		for (int i = 0; i < 4; i++)
		{
			int n = (data[x+i] < 0 ? (int)data[x+i] + 256 : (int)data[x+i]) << (8 * i);
			//System.out.println(n);
			fromByte += n;
		}
		return fromByte;
	}
	public static byte[] intTobytes(int x) {
		int id = x;
		byte[] data = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			data[i] =(byte)(id >> (8 * i));
		}	
		return data;
	}
}
