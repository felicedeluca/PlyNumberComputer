package linesweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.DoubleRange;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import utilities.ApfloatRange;
import utilities.Configurator;

public class LineSweepAlgorithm {

	Set<Circle> circles;
	Set<Circle> activeCircles;

	Map<Double, Set<Event>> events;

	public int startOnCircles(Set<Circle> circles){

		activeCircles = new HashSet<Circle>();

		//Compute Events
		EventsMng em = new EventsMng();
		Map<Apfloat, Set<Event>> eventsMap = em.computeStartingEndingAndIntersectingEvents(circles);

		//Ordered key list
		ArrayList<Apfloat> eventsX = new ArrayList<Apfloat>(eventsMap.keySet());
		Collections.sort(eventsX);

		int maxPly = 0;
		Set<ApfloatRange> maxPlyRanges = new HashSet<ApfloatRange>();
		
		System.out.println("Events: " + eventsX.size());
		
		int i = 0;

		for(Apfloat x : eventsX){
			
			System.out.print(i+". ");
			i++;

			Set<Event> events = eventsMap.get(x);

			for (Event e : events){
				//Setup circles
				prepareForEvent(e);	
			}

			if(activeCircles.size()>0){
			//Compute all Intersections
			ArrayList<ApfloatRange> intervals = computeIntersections(x);

			//Point of maximum overlap
			Set<ApfloatRange> currPlyRanges = pointOfMaximumOverlap(intervals);
			int currPly = currPlyRanges.size();

			//System.out.println("x: "+x.toString(false)+" currply: "+ currPly);
			System.out.println("currply: "+ currPly);

			
			//Check Ply
			if(currPly>maxPly){
				maxPly = currPly;
				maxPlyRanges = currPlyRanges;}
			}
			else{
				System.out.println("no active circles");
			}

		}

		
		System.out.println("Max Ply: " + maxPly);
		i = 0;
		for(ApfloatRange range : maxPlyRanges){
			System.out.println(i+") "+range.getCircle().getLabel()+" [(" + range.getCircle().getX() + " , " + range.getCircle().getY() + "), " + range.getCircle().radius +"]");
			i++;
		}
		
		return maxPly;

	}


	private void prepareForEvent(Event e){

		switch(e.type){
		case OPENING:{
			if(activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to activating a circle twice: " + e.circle.toString());
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
			Apfloat radius = circle.radius;
		
			//System.out.println("xLine: "+xLine+"\ncenter: (" +xCenter + ", "+ yCenter +") radius: "+ radius );
			


			//Apfloat a = new Apfloat("1", Apfloat.INFINITE);
			Apfloat b = yCenter.multiply(new Apfloat("2", Configurator.apfloatPrecision())).negate();
			Apfloat c = ApfloatMath.sum(ApfloatMath.pow(yCenter, 2),
					 ApfloatMath.pow(ApfloatMath.abs(xLine.subtract(xCenter)), 2),
					 ApfloatMath.pow(radius, 2).negate());


			Apfloat sqrt = ApfloatMath.sqrt(
							ApfloatMath.sum(
									ApfloatMath.pow(b, 2),
										c.multiply(new Apfloat("4", Configurator.apfloatPrecision())).negate()));
			
			Apfloat y1 = b.negate().subtract(sqrt).divide(new Apfloat("2", Configurator.apfloatPrecision()));
			Apfloat y2 = b.negate().add(sqrt).divide(new Apfloat("2", Configurator.apfloatPrecision()));

			ApfloatRange currRange = new ApfloatRange(y1, y2, circle);
			
			//System.out.println(" ["+y1+" , "+y2+"]");

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
			
			ArrayList<ApfloatRange> cR = closingRangesMap.get(k);
			if(cR != null){
				if(!openRanges.containsAll(cR)) throw new IllegalArgumentException("Closing not open Range");
				openRanges.removeAll(cR);
				tempP -= cR.size();
			}

			
		}


		return maxOverlappingRanges;
	}

}
