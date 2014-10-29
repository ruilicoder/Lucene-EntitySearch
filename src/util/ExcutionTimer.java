package util;

public class ExcutionTimer {
	private  long excutionStart;
	private  long excutionEnd;
	public ExcutionTimer() {
		
	}
	public  void setStart(){
		this.excutionStart = System.currentTimeMillis();
	}
	
	public void setEnd() {
		this.excutionEnd = System.currentTimeMillis();
	}
	public long getTime() {
		return this.excutionEnd - this.excutionStart;
	}
}
