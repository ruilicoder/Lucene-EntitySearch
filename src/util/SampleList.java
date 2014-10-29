package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SampleList<T> extends ArrayList<T>{
	private static final long serialVersionUID = 1L;
	int capacity;
	int actualSize;
	Random r;
	
	public SampleList(int capacity){
		this.capacity=capacity;
		actualSize=0;
		r=new Random();
	}
	
	public void addItemWithSampling(T item){
		actualSize++;
		if (size()<capacity){
			add(item);
		}
		else{
			int s=r.nextInt(actualSize);
			if (s<capacity)
				set(s, item);			
		}
	}
	
	public void sample(){
		Collections.shuffle(this);
		while (size()>capacity){
			remove(size()-1);
		}
	}
	public int actualSize(){
		return actualSize;
	}
}
