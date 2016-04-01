package utilities;

import org.apfloat.Apfloat;

public class ApfloatRange {
	
	Apfloat v1, v2;
	
	public ApfloatRange(Apfloat v1, Apfloat v2){
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Apfloat getMinimumValue(){
		
		return (v1.compareTo(v2) < 0) ? v1 : v2;
		
	}
	
	public Apfloat getMaximumValue(){
		
		return (v1.compareTo(v2) > 0) ? v1 : v2;
		
	}

}
