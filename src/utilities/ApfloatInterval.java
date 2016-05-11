package utilities;

import org.apfloat.Apfloat;

import circlegraph.Circle;

public class ApfloatInterval implements Comparable<ApfloatInterval>{
	
	Circle circle;
	
	Apfloat min;
	Apfloat max;
	
	
	public ApfloatInterval(Apfloat v1, Apfloat v2, Circle circle){
		this.circle = circle;
		this.min = (v1.compareTo(v2) < 0) ? v1 : v2;
		this.max = (v1.compareTo(v2) > 0) ? v1 : v2;	
	}
	
	public Apfloat getMinimumValue(){
		return this.min;
	}
	
	public Apfloat getMaximumValue(){		
		return this.max;
	}
	
	public String toString(){
	
		String s = "["+this.getMinimumValue().toString(true)+", "+this.getMaximumValue().toString(true)+"]";
	
	return s;
		
	}
	
	public Circle getCircle(){
		return this.circle;
	}
	
	
    public boolean intersects(ApfloatInterval that) {
        if (that.max.compareTo(this.min) == -1) return false;
        if (this.max.compareTo(that.min) == -1) return false;
        return true;
    }


	public boolean contains(Apfloat x) {
        return (min.compareTo(x) <= 0) && (x.compareTo(max) <= 0);
    }
	
	@Override
	public int compareTo(ApfloatInterval that) {
        if      (this.min.compareTo(that.min) == -1) return -1;
        else if (this.min.compareTo(that.min) == +1) return +1;
        else if (this.max.compareTo(that.max) == -1) return -1;
        else if (this.max.compareTo(that.max) == +1) return +1;
        else                          return  0;
    }
	
	public boolean isDegenerate(){
		return (this.min.compareTo(this.max) == 0);
	}

}
