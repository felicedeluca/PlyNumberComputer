package utilities;

import org.apfloat.Apfloat;

import circlegraph.Circle;

public class ApfloatRange {
	
	//Apfloat v1, v2;
	Circle circle;
	
	Apfloat minValue;
	Apfloat maxValue;
	
	
	public ApfloatRange(Apfloat v1, Apfloat v2, Circle circle){
		//this.v1 = v1;//v1.precision(100);
		//this.v2 = v2;//v2.precision(100);
		this.circle = circle;
		
		this.minValue = (v1.compareTo(v2) < 0) ? v1 : v2;
		this.maxValue = (v1.compareTo(v2) > 0) ? v1 : v2;
		
		
	}
	
	public Apfloat getMinimumValue(){
		
		//if(this.minValue==null) this.minValue = (v1.compareTo(v2) < 0) ? v1 : v2;
		
		return this.minValue;
		
	}
	
	public Apfloat getMaximumValue(){
		
		//if(this.maxValue == null) this.maxValue = (v1.compareTo(v2) > 0) ? v1 : v2;
		
		return this.maxValue;
		
	}
	
	public String toString(){
	
		String s = "["+this.getMinimumValue().toString(true)+", "+this.getMaximumValue().toString(true)+"]";
	
	return s;
		
	}
	
	public Circle getCircle(){
		return this.circle;
	}

}
