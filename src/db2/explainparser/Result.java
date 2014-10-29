package db2.explainparser;

public class Result {
	int[] x  ;
	Result(int i) {
		x =new int[1];
		x[0] = i;
	}
	Result(int i, int j) {
		x =new int[2];
		x[0] = i;
		x[1] = j;
	}
	Result(int i, int j, int k) {
		x = new int[3];
		x[0] = i;
		x[1] = j;
		x[2] = k;
	}
	public  String  toString() {
		String str = "";
		for (int y : x) {
			str  += y + ":";
		}
		return str;
	}
	
}
