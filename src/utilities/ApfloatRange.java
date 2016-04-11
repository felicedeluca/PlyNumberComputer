package utilities;

import org.apfloat.Apfloat;

import linesweep.Circle;

public class ApfloatRange {
	
	Apfloat v1, v2;
	Circle circle;
	
	public ApfloatRange(Apfloat v1, Apfloat v2, Circle circle){
		this.v1 = v1;//v1.precision(100);
		this.v2 = v2;//v2.precision(100);
		this.circle = circle;
		
	}
	
	public Apfloat getMinimumValue(){
		
		return (v1.compareTo(v2) < 0) ? v1 : v2;
		
	}
	
	public Apfloat getMaximumValue(){
		
		return (v1.compareTo(v2) > 0) ? v1 : v2;
		
	}
	
	public String toString(){
	
		String s = "["+this.getMinimumValue().toString(true)+", "+this.getMaximumValue().toString(true)+"]";
	
	return s;
		
	}
	
	public Circle getCircle(){
		return this.circle;
	}

}
