package linesweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import circlegraph.Circle;
import circlegraph.CircleGraph;
import linesweep.Event.Type;
import utilities.ApfloatRange;
import utilities.Configurator;

public class LineSweepAlgorithm {

	Set<Circle> circles;
	Set<Circle> activeCircles;

	Map<Double, Set<Event>> events;

	public CircleGraph startOnCircles(Set<Circle> circles){
		
		activeCircles = new HashSet<Circle>();

		//Compute Events
		EventsMng em = new EventsMng();
		Map<Apfloat, Set<Event>> eventsMap = em.computeStartingEndingAndIntersectingEvents(circles);

		//Ordered key list
		ArrayList<Apfloat> eventsX = new ArrayList<Apfloat>(eventsMap.keySet());
		Collections.sort(eventsX);

		int maxPly = 0;
		Set<ApfloatRange> maxPlyRanges = new HashSet<ApfloatRange>();
		Apfloat maxX = new Apfloat(0);
		
		//System.out.println("Events: " + eventsX.size());
		
		//int i = 0;

		for(Apfloat x : eventsX){
			
			//System.out.print(i+". ");
			//i++;

			Set<Event> events = eventsMap.get(x);
			
			Set<Circle> degenerateCiclesToClose = new HashSet<Circle>();

			for (Event e : events){
				//Setup circles
				prepareForEvent(e);	
				
				if(e.getType() == Type.CENTER){
					degenerateCiclesToClose.add(e.circle);
				}
				
			}

			if(activeCircles.size()>0){
			//Compute all Intersections
			ArrayList<ApfloatRange> intervals = computeIntersections(x);

			//Point of maximum overlap
			Set<ApfloatRange> currPlyRanges = pointOfMaximumOverlap(intervals);
			int currPly = currPlyRanges.size();

			//System.out.println("x: "+x.toString(false)+" currply: "+ currPly);

			//Check Ply
			if(currPly>maxPly){//
				//System.out.println("New Ply: " + currPly);
				maxPly = currPly;
				maxPlyRanges = currPlyRanges;
				maxX = x;
				}
			
			
			for(Circle degenerateCircle : degenerateCiclesToClose){
				if(!activeCircles.contains(degenerateCircle))
					throw new IllegalArgumentException("Trying to close a non active degenerate circle: " + degenerateCircle.toString());
				activeCircles.remove(degenerateCircle);
			}
			
			}
			else{
				//System.out.println("no active circles");
			}
			

		}

		
		//System.out.println("Max Ply: " + maxPly);
		//System.out.println("X coordinate: " + maxX);
		
		Set<Circle> maxPlyCircles = new HashSet<Circle>();

		for(ApfloatRange range : maxPlyRanges){
			maxPlyCircles.add(range.getCircle());
		}
		
		CircleGraph cg = new CircleGraph(circles, maxPlyCircles, maxX);
		
		return cg;

	}


	private void prepareForEvent(Event e){

		switch(e.type){
		case OPENING:{
			if(activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to activate a circle twice: " + e.circle.toString());
			activeCircles.add(e.circle);
		}
		break;
		case CLOSING:{
			if(!activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to close a non active circle: " + e.circle.toString());
			activeCircles.remove(e.circle);
		}
		break;
		case INTERSECTION:{
			Circle c1 = e.c1;
			Circle c2 = e.c2;
			if(!activeCircles.contains(c1) &&
					!activeCircles.contains(c2))
				throw new IllegalArgumentException("Intersection between non active circles: " + e.c1.toString()+ " , "+ e.c2.toString() );
		}
		case DUPLICATED:{
			
		}
		break;
		case CENTER:{
			if(activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to activate a circle twice: " + e.circle.toString());
			activeCircles.add(e.circle);
		}
		break;
		default:
			break;
		}

	}

	private ArrayList<ApfloatRange> computeIntersections(Apfloat xLine){

		ArrayList<ApfloatRange> rangeSet = new ArrayList<ApfloatRange>();

		for(Circle circle : this.activeCircles){
			

			Apfloat xCenter = circle.getX();
			Apfloat yCenter = circle.getY();
			Apfloat squaredRadius = circle.getSquaredRadius();
			
			if(circle.hasRadiusZero()){
				ApfloatRange currRange = new ApfloatRange(yCenter, yCenter, circle);
				rangeSet.add(currRange);
				continue;
			}
		
			//System.out.println("xLine: "+xLine+"\ncenter: (" +xCenter + ", "+ yCenter +") radius: "+ radius );
			
			//Apfloat a = new Apfloat("1", Apfloat.INFINITE);
			Apfloat b = yCenter.multiply(new Apfloat("2", Configurator.apfloatPrecision())).negate(); //-2yc
			
			Apfloat c = ApfloatMath.sum(ApfloatMath.pow(yCenter, 2),
					 ApfloatMath.pow(ApfloatMath.abs(xLine.subtract(xCenter)), 2),
					 squaredRadius.negate()); // yc^2+(xl-xc)^2-r^2

			
			Apfloat disc = ApfloatMath.sum(
					ApfloatMath.pow(b, 2),
					c.multiply(new Apfloat("4", Configurator.apfloatPrecision())).negate());

			Apfloat sqrt = ApfloatMath.sqrt(disc);
			
			Apfloat y1 = b.negate().subtract(sqrt).divide(new Apfloat("2", Configurator.apfloatPrecision()));
			Apfloat y2 = b.negate().add(sqrt).divide(new Apfloat("2", Configurator.apfloatPrecision()));

			ApfloatRange currRange = new ApfloatRange(y1, y2, circle);
			rangeSet.add(currRange);

		}

		return rangeSet;

	}

	private Set<ApfloatRange> pointOfMaximumOverlap(ArrayList<ApfloatRange> intervals){

		Map<Apfloat, ArrayList<ApfloatRange>> openingRangesMap = new HashMap<Apfloat, ArrayList<ApfloatRange>>();
		Map<Apfloat, ArrayList<ApfloatRange>> closingRangesMap = new HashMap<Apfloat, ArrayList<ApfloatRange>>();
		Map<Apfloat, ArrayList<ApfloatRange>> degenerateRangesMap = new HashMap<Apfloat, ArrayList<ApfloatRange>>();
		
		Set<Apfloat> allKeysSet = new HashSet<Apfloat>();

	
		for (ApfloatRange r : intervals){

			if(r.getMaximumValue() == r.getMinimumValue()){
				Apfloat key = r.getMinimumValue();
				ArrayList<ApfloatRange> dR = degenerateRangesMap.get(key);
				if(dR == null) dR = new ArrayList<ApfloatRange>();
				dR.add(r);
				degenerateRangesMap.put(key, dR);
				allKeysSet.add(key);

			}else{

				Apfloat minKey = r.getMinimumValue();
				ArrayList<ApfloatRange> oR = openingRangesMap.get(minKey);
				if(oR == null) oR = new ArrayList<ApfloatRange>();
				oR.add(r);
				openingRangesMap.put(minKey, oR);
				allKeysSet.add(minKey);


				Apfloat maxKey = r.getMaximumValue();
				ArrayList<ApfloatRange> cR = closingRangesMap.get(maxKey);
				if(cR == null) cR = new ArrayList<ApfloatRange>();
				cR.add(r);
				closingRangesMap.put(maxKey, cR);
				allKeysSet.add(maxKey);
			}
		}
		
		ArrayList<Apfloat> allKeys = new ArrayList<Apfloat>(allKeysSet);
		Collections.sort(allKeys);
		
		int p = -1;
		int tempP = 0;
		
		Set<ApfloatRange> maxOverlappingRanges = new HashSet<ApfloatRange>();
		Set<ApfloatRange> openRanges = new HashSet<ApfloatRange>();

		for(Apfloat k : allKeys){
			
			ArrayList<ApfloatRange> cR = closingRangesMap.get(k);
			if(cR != null){
				if(!openRanges.containsAll(cR)) throw new IllegalArgumentException("Closing not open Range");
				openRanges.removeAll(cR);
				tempP -= cR.size();
			}
			
			ArrayList<ApfloatRange> oR = openingRangesMap.get(k);
			if(oR != null){
				openRanges.addAll(oR);
				tempP +=oR.size();
				
			}
			
			ArrayList<ApfloatRange> dR = degenerateRangesMap.get(k);
			if(dR != null){
				tempP += dR.size();
				if(tempP > p){
					p = tempP;
					maxOverlappingRanges = new HashSet<ApfloatRange>(openRanges);
					maxOverlappingRanges.addAll(dR);
				}
				tempP -= dR.size();
			}
			
			if(tempP > p){
				p = tempP;
				maxOverlappingRanges = new HashSet<ApfloatRange>(openRanges);
			}

			
		}


		return maxOverlappingRanges;
	}

}
