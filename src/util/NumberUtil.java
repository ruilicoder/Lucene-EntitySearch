package util;

import java.util.Random;

public class NumberUtil {
	public static Random random=new Random();
	
	public static int getRandom(){
		return random.nextInt();
	}
	
	public static int pow(int base, int p){
		int result=1;
		for (int i=0;i<p;i++)
			result*=base;
		return result;
	}
	
	public static int bytesToInt(byte[] bytes, int offset) {
		return compressedBytesToInt(bytes,offset,4);
	}

	public static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}
	
	public static byte[] longToBytes(long num){
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (num >>> (56 - i * 8));
		}
		return b;	
	}
	
	// The length of bytes is either m or 4.
	// If the first bit is 0, the length is m (0~[2^m-1]),
	// otherwise 4 (0 ~ 2147483647).
	// return the int and the length of used bytes.
	public static Pair<Integer,Integer> unsignedBytesToInt(byte[] bytes, int offset, int m){
		assert(m>0 && m<=4);
		int b=bytes[offset];
		int l;
		if ((b&128)==0)
			l=m;
		else
			l=4;
		byte[] tmp=new byte[l];
		for (int i=0;i<l;i++){
			tmp[i]=bytes[offset+i];
		}
		tmp[0]=(byte)(tmp[0]&127);
		return new Pair<Integer, Integer>(compressedBytesToInt(tmp,0,l), l);
	}
	public static Pair<Integer,Integer> unsignedBytesToInt(byte[] bytes, int offset){
		return unsignedBytesToInt(bytes, offset, 1);	
	}
	
	public static byte[] unsignedIntToBytes(int num, int m){
		assert(m>0 && m<=4);
		assert(num>=0 && num<=2147483647);
		int upper=pow(2, m*8-1)-1;
		byte[] b = intToBytes(num);
		if (num>=0 && num<=upper){
			byte[] result=new byte[m];
			for (int j=0;j<m;j++)
				result[j]=b[4-m+j];
			return result;
		}
		else{
			b[0]=(byte)(b[0]|128);
			return b;
		}				
	}
	public static byte[] unsignedIntToBytes(int num){
		return unsignedIntToBytes(num, 1);
	}
	
	
	
	// Conversion between byte array (unfixed length) and int.
	public static int compressedBytesToInt(byte[] bytes,int offset, int len){
		assert(len>0 && len<=4);
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = offset; i < offset + len; i++) {
			res <<= 8;
			temp = bytes[i] & mask;
			res |= temp;
		}
		return res;
	}	
	public static byte[] intToCompressedBytes(int num){
		byte[] b = intToBytes(num);
		int i=0;
		while (b[i]==0 && i<3) i++;
		byte[] result=new byte[4-i];
		for (int j=0;j<4-i;j++)
			result[j]=b[i+j];
		return result;
	}
	
	public static void copyBytes(byte[] oriBytes, byte[] targetBytes, int offset){
		for (int i=0;i<oriBytes.length;i++)
			targetBytes[i+offset]=oriBytes[i];
	}
	
	public static void main(String[] args){
		for (int i=0;i<100000;i++){
			byte[] b=unsignedIntToBytes(i,2);
			int num=unsignedBytesToInt(b,0,2).first;
		}
	}
}
