package util;

public class ByteConvert {
	public static byte[] convertToByte (int id) {
		byte[] data = new byte[2];
			for (int i = 0; i < 2; i++)
			{
			data[i] =(byte)(id >> (8 * i));
			}
		return data;
		}

	public static Integer convertToInt (byte[]data, int x) {
		int fromByte = 0;
		for (int i = 0; i < 2; i++)
		{
			int n = (data[x+i] < 0 ? (int)data[x+i] + 256 : (int)data[x+i]) << (8 * i);
			//System.out.println(n);
			fromByte += n;
		}
		return fromByte;
	}
	
	public static int[] convertToIntArray(byte[] data) {
		int[] array = new int[data.length/2];
		for (int i = 0; i < array.length; i++) {
			array[i] = convertToInt(data, i * 2);
		}
		return array;
	}
}
