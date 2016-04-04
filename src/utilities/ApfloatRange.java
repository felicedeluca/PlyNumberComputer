package utilities;

import org.apfloat.Apfloat;

public class ApfloatRange {
	
	Apfloat v1, v2;
	
	public ApfloatRange(Apfloat v1, Apfloat v2){
		this.v1 = v1.precision(100);
		this.v2 = v2.precision(100);
		
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

}
