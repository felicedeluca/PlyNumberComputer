package linesweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.DoubleRange;

public class LineSweepAlgorithm {
	
	Set<Circle> circles;
	Set<Circle> activeCircles;
	
	Map<Double, Set<Event>> events;
	
	public double startOnCircles(Set<Circle> circles){
		
		//Compute Events
		EventsMng em = new EventsMng();
		Map<Double, Set<Event>> eventsMap = em.computeStartingAndEndingEvents(circles);
		
		//Ordered key list
		ArrayList<Double> eventsX = new ArrayList<Double>(eventsMap.keySet());
		Collections.sort(eventsX);
		
		double maxPly = 0;
		
		for(Double x : eventsX){
			
			Set<Event> events = eventsMap.get(x);
			
			for (Event e : events){
				//Setup circles
				prepareForEvent(e);	
			}
			
			//Compute all Intersections
			computeIntersections(x);
			
			//Point of maximum overlap
			double currPly = pointOfMaximumOverlap();

			
			//Check Ply
			if(currPly>maxPly) maxPly = currPly;
			
			
		}
		
		return maxPly;
		
	}
	
	
	private void prepareForEvent(Event e){
	
		switch(e.type){
		case OPENING:{
			if(activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to activating a circle twice");
			activeCircles.add(e.circle);
		}
			break;
		case CLOSING:{
			if(!activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to close a non active circle");
			activeCircles.remove(e.circle);
		}
			break;
		case INTERSECTION:{
			Circle c1 = null;
			Circle c2 = null;
			if(!activeCircles.contains(c1) &&
			   !activeCircles.contains(c2))
				throw new IllegalArgumentException("Intersection between non active circles");

		}
			break;
		default:
			break;
		}
		
	}
	
	private ArrayList<DoubleRange> computeIntersections(double xLine){
		
		ArrayList<DoubleRange> rangeSet = new ArrayList<DoubleRange>();
		
		for(Circle circle : this.activeCircles){
			
			double xCenter = circle.getX();
			double yCenter = circle.getY();
			double radius = circle.radius;
			
			
			double a = 1;
			double b = -2*Math.pow(yCenter, 2);
			double c = Math.pow(yCenter, 2)+Math.pow((xLine-xCenter), 2)-Math.pow(radius, 2);
			
			double y1 = (-b-Math.sqrt(Math.pow(b, 2)-4*a*c))/2*a;
			double y2 = (-b+Math.sqrt(Math.pow(b, 2)-4*a*c))/2*a;
			
			DoubleRange currRange = new DoubleRange(y1, y2);
			
			rangeSet.add(currRange);
			
		}

		return rangeSet;

	}
	
	private double pointOfMaximumOverlap(){
		
		double p = 0;
		
		
		return p;
	}

}
