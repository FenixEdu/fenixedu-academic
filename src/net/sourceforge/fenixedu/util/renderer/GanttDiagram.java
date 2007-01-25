package net.sourceforge.fenixedu.util.renderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class GanttDiagram {

    public final static Comparator<Interval> INTERVAL_COMPARATOR_BY_BEGIN = new ComparatorChain();
    public final static Comparator<Interval> INTERVAL_COMPARATOR_BY_END = new ComparatorChain();
    static {	
	((ComparatorChain) INTERVAL_COMPARATOR_BY_BEGIN).addComparator(new BeanComparator("start"));
	((ComparatorChain) INTERVAL_COMPARATOR_BY_END).addComparator(new BeanComparator("end"));
    }
    
    public List<GanttDiagramEvent> events;
    
    public DateTime firstInstant;
    
    public DateTime lastInstant;
    
    public Map<Integer, Integer> years;
    
    public List<DateTime> months;
    
    
    public GanttDiagram(List<GanttDiagramEvent> events_) {
	setEvents(events_);		
	calculateFirstAndLastInstant();	
	generateMonthsAndYears();
    }
   
    private void calculateFirstAndLastInstant() {		
	SortedSet<Interval> allIntervalsSortedByBeginDate = new TreeSet<Interval>(INTERVAL_COMPARATOR_BY_BEGIN);
	for (GanttDiagramEvent event : getEvents()) {
	    allIntervalsSortedByBeginDate.addAll(event.getEventSortedIntervalsForGanttDiagram());
	}
	setFirstInstant(allIntervalsSortedByBeginDate.first().getStart());
	SortedSet<Interval> allIntervalsSortedByEndDate = new TreeSet<Interval>(INTERVAL_COMPARATOR_BY_END);
	for (GanttDiagramEvent event : getEvents()) {
	    allIntervalsSortedByEndDate.addAll(event.getEventSortedIntervalsForGanttDiagram());
	}	
	setLastInstant(allIntervalsSortedByEndDate.last().getEnd());
    }

    private void generateMonthsAndYears() {
	if(!getEvents().isEmpty()) {
	   	    	    	    	   
	    DateTime firstMonthDateTime = getFirstInstant();
	    DateTime lastMontDateTime = getLastInstant();
	    
	    while ((firstMonthDateTime.getYear() < lastMontDateTime.getYear())
		    || (firstMonthDateTime.getYear() == lastMontDateTime.getYear() && 
			    firstMonthDateTime.getMonthOfYear() <= lastMontDateTime.getMonthOfYear())) {
		
		getMonths().add(firstMonthDateTime);				
		
		if(getYears().containsKey(Integer.valueOf(firstMonthDateTime.getYear()))) {
		   getYears().put(Integer.valueOf(firstMonthDateTime.getYear()), getYears().get(Integer.valueOf(firstMonthDateTime.getYear())) + 1);
		
		} else {
		    getYears().put(Integer.valueOf(firstMonthDateTime.getYear()), 1);
		}		
		
		firstMonthDateTime = firstMonthDateTime.plusMonths(1);		
	    }	     	    	 
	}	
    }
        
    public List<GanttDiagramEvent> getEvents() {
        return events;
    }

    public void setEvents(List<GanttDiagramEvent> events) {
        this.events = events;
    }

    public DateTime getFirstInstant() {
        return firstInstant;
    }

    public void setFirstInstant(DateTime firstInstant) {
        this.firstInstant = firstInstant;
    }

    public DateTime getLastInstant() {
        return lastInstant;
    }

    public void setLastInstant(DateTime lastInstant) {
        this.lastInstant = lastInstant;
    }

    public List<DateTime> getMonths() {
	if(months == null) {
	    months = new ArrayList<DateTime>();
	}
	return months;
    }

    public void setMonths(List<DateTime> months) {
        this.months = months;
    }

    public Map<Integer, Integer> getYears() {
	if(years == null) {
	    years = new TreeMap<Integer, Integer>();
	}
        return years;
    }

    public void setYears(Map<Integer, Integer> years) {
        this.years = years;
    }
     
}
