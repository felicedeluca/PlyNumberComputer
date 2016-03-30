package linesweep;

import java.util.Map;
import java.util.Set;

public class LineSweepAlgorithm {
	
	Set<Circle> circles;
	Set<Circle> activeCircles;
	
	Map<Double, Set<Event>> events;
	
	public void startOnCircles(Set<Circle> circles){
		
		EventsMng em = new EventsMng();
		
		Map<Double, Set<Event>> eventsMap = em.computeStartingAndEndingEvents(circles);
		
	}

}
