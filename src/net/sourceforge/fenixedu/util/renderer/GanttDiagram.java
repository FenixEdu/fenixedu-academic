package net.sourceforge.fenixedu.util.renderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GanttDiagram {

    public final static Comparator<Interval> INTERVAL_COMPARATOR_BY_BEGIN = new ComparatorChain();
    public final static Comparator<Interval> INTERVAL_COMPARATOR_BY_END = new ComparatorChain();
    
    static {	
	((ComparatorChain) INTERVAL_COMPARATOR_BY_BEGIN).addComparator(new BeanComparator("start"));
	((ComparatorChain) INTERVAL_COMPARATOR_BY_END).addComparator(new BeanComparator("end"));
    }
    
    private Locale locale;
    
    private List<GanttDiagramEvent> events;
    
    private ViewType viewType;
    
    private DateTime firstInstant, lastInstant;
    
    private Map<Integer, Integer> yearsView;
    
    private Map<YearMonthDay, Integer> monthsView;
    
    private List<DateTime> months, days;
    
    
    public static GanttDiagram getNewTotalGanttDiagram(List<GanttDiagramEvent> events_) throws InvalidArgumentException {
	return new GanttDiagram(events_, ViewType.TOTAL);
    }
    
    public static GanttDiagram getNewTotalGanttDiagram(List<GanttDiagramEvent> events_, YearMonthDay begin, YearMonthDay end) throws InvalidArgumentException {
	return new GanttDiagram(events_, ViewType.TOTAL, begin, end);
    }
    
        
    public static GanttDiagram getNewWeeklyGanttDiagram(List<GanttDiagramEvent> events_, YearMonthDay begin) throws InvalidArgumentException {
	return new GanttDiagram(events_, ViewType.WEEKLY, begin);
    }
    
    public static GanttDiagram getNewDailyGanttDiagram(List<GanttDiagramEvent> events_, YearMonthDay begin) throws InvalidArgumentException {
	return new GanttDiagram(events_, ViewType.DAILY, begin);
    }
    
    public static GanttDiagram getNewMonthlyGanttDiagram(List<GanttDiagramEvent> events_, YearMonthDay begin) throws InvalidArgumentException {
	return new GanttDiagram(events_, ViewType.MONTHLY, begin);
    }
    
    public static GanttDiagram getNewMonthlyTotalGanttDiagram(List<GanttDiagramEvent> events_, YearMonthDay begin) throws InvalidArgumentException {
	return new GanttDiagram(events_, ViewType.MONTHLY_TOTAL, begin);
    }
    
            
    private GanttDiagram(List<GanttDiagramEvent> events_, ViewType type) throws InvalidArgumentException {
	setViewType(type);
	setEvents(events_);		
	init(type, null, null);	
    }
    
    private GanttDiagram(List<GanttDiagramEvent> events_, ViewType type, YearMonthDay begin) throws InvalidArgumentException {
	setViewType(type);
	setEvents(events_);		
	init(type, begin, null);	
    }
    
    private GanttDiagram(List<GanttDiagramEvent> events_, ViewType type, YearMonthDay begin, YearMonthDay end) throws InvalidArgumentException {
	setViewType(type);
	setEvents(events_);		
	init(type, begin, end);	
    }
    
    private void init(ViewType type, YearMonthDay begin, YearMonthDay end) throws InvalidArgumentException {
	switch (type) {
	
	case TOTAL:
	    calculateFirstAndLastInstantInTotalMode(begin, end);
	    generateYearsViewAndMonths();
	    break;
	
	case MONTHLY_TOTAL:
	    calculateFirstAndLastInstantInMonthlyMode(begin);
	    generateYearsViewAndMonths();	    
	    break;
	    
	case MONTHLY:
	    calculateFirstAndLastInstantInMonthlyMode(begin);
	    generateYearsViewMonthsViewAndDays();	    
	    break;
	    
	case WEEKLY:
	    calculateFirstAndLastInstantInWeeklyMode(begin);
	    generateYearsViewMonthsViewAndDays();
	    break;
	  
	case DAILY:
	    calculateFirstAndLastInstantInDailyMode(begin);
	    generateYearsViewMonthsViewAndDays();
	    break;
	    
	default:
	    break;
	}
    }
    
    private void calculateFirstAndLastInstantInMonthlyMode(YearMonthDay begin) throws InvalidArgumentException {
	if(begin == null) {
	    throw new InvalidArgumentException();
	}
	DateTime beginDateTime = begin.toDateTimeAtMidnight();
	beginDateTime = (beginDateTime.getDayOfMonth() != 1) ? beginDateTime.withDayOfMonth(1) : beginDateTime;	
	setFirstInstant(beginDateTime);
	setLastInstant(beginDateTime.plusMonths(1).minusDays(1));	
    }

    private void calculateFirstAndLastInstantInDailyMode(YearMonthDay begin) throws InvalidArgumentException {
	if(begin == null) {
	    throw new InvalidArgumentException();
	}			
	setFirstInstant(begin.toDateTimeAtMidnight());
	setLastInstant(begin.toDateTimeAtMidnight());	
    }
    
    private void calculateFirstAndLastInstantInWeeklyMode(YearMonthDay begin) throws InvalidArgumentException {
	if(begin == null) {
	    throw new InvalidArgumentException();
	}	
	DateTime beginDateTime = begin.toDateTimeAtMidnight();
	beginDateTime = (beginDateTime.getDayOfWeek() != 1) ? beginDateTime.withDayOfWeek(1) : beginDateTime;	
	setFirstInstant(beginDateTime);
	setLastInstant(beginDateTime.plusDays(6));	
    }

    private void calculateFirstAndLastInstantInTotalMode(YearMonthDay begin, YearMonthDay end) throws InvalidArgumentException {		
	
	if(begin == null) {
            SortedSet<Interval> allIntervalsSortedByBeginDate = new TreeSet<Interval>(INTERVAL_COMPARATOR_BY_BEGIN);
            for (GanttDiagramEvent event : getEvents()) {
                allIntervalsSortedByBeginDate.addAll(event.getEventSortedIntervalsForGanttDiagram());
            }
            setFirstInstant(allIntervalsSortedByBeginDate.first().getStart().toDateMidnight().toDateTime());	
	} else {
	  setFirstInstant(begin.toDateTimeAtMidnight());  
	}
	
	if(end == null) {
            SortedSet<Interval> allIntervalsSortedByEndDate = new TreeSet<Interval>(INTERVAL_COMPARATOR_BY_END);
            for (GanttDiagramEvent event : getEvents()) {
                allIntervalsSortedByEndDate.addAll(event.getEventSortedIntervalsForGanttDiagram());
            }	
            setLastInstant(allIntervalsSortedByEndDate.last().getEnd().toDateMidnight().toDateTime());
	} else {
	  setLastInstant(end.toDateTimeAtMidnight());  
	}
	
        if(getFirstInstant().isAfter(getLastInstant()) || getFirstInstant().isEqual(getLastInstant())) {
            throw new InvalidArgumentException();
        }
    }

    private void generateYearsViewMonthsViewAndDays() {

	DateTime firstMonthDateTime = getFirstInstant();
	DateTime lastMontDateTime = getLastInstant();

	while ((firstMonthDateTime.getYear() < lastMontDateTime.getYear())
		|| (firstMonthDateTime.getYear() == lastMontDateTime.getYear() && firstMonthDateTime
			.getDayOfYear() <= lastMontDateTime.getDayOfYear())) {

	    getDays().add(firstMonthDateTime);

	    YearMonthDay day = firstMonthDateTime.toYearMonthDay().withDayOfMonth(1);
	    if (getMonthsView().containsKey(day)) {
		getMonthsView().put(day, getMonthsView().get(day) + 1);
	    } else {
		getMonthsView().put(day, 1);
	    }

	    if (getYearsView().containsKey(Integer.valueOf(firstMonthDateTime.getYear()))) {
		getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()), 
			getYearsView().get(Integer.valueOf(firstMonthDateTime.getYear())) + 1);
	    } else {
		getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()), 1);
	    }

	    firstMonthDateTime = firstMonthDateTime.plusDays(1);
	}
    }        
        
    private void generateYearsViewAndMonths() {
		   	    	    	    	   
	DateTime firstMonthDateTime = getFirstInstant();
	DateTime lastMontDateTime = getLastInstant();

	while ((firstMonthDateTime.getYear() < lastMontDateTime.getYear())
		|| (firstMonthDateTime.getYear() == lastMontDateTime.getYear() && firstMonthDateTime
			.getMonthOfYear() <= lastMontDateTime.getMonthOfYear())) {

	    getMonths().add(firstMonthDateTime);

	    if (getYearsView().containsKey(Integer.valueOf(firstMonthDateTime.getYear()))) {
		getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()),
			getYearsView().get(Integer.valueOf(firstMonthDateTime.getYear())) + 1);
	    } else {
		getYearsView().put(Integer.valueOf(firstMonthDateTime.getYear()), 1);
	    }

	    firstMonthDateTime = firstMonthDateTime.plusMonths(1);
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

    public Map<Integer, Integer> getYearsView() {
	if(yearsView == null) {
	    yearsView = new TreeMap<Integer, Integer>();
	}
        return yearsView;
    }

    public void setYearsView(Map<Integer, Integer> years) {
        this.yearsView = years;
    }

    public Map<YearMonthDay, Integer> getMonthsView() {       
        if(monthsView == null) {
            monthsView = new TreeMap<YearMonthDay, Integer>();
	}
        return monthsView;
    }

    public void setMonthsView(Map<YearMonthDay, Integer> monthsView) {
        this.monthsView = monthsView;
    }      

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }
        
    public Locale getLocale() {
	if(locale == null) {
	    locale = LanguageUtils.getLocale();
	}
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<DateTime> getDays() {       
        if(days == null) {
            days = new ArrayList<DateTime>();
	}
	return days;
    }

    public void setDays(List<DateTime> days) {
        this.days = days;
    }
    
    public enum ViewType {
	TOTAL,
	MONTHLY,
	MONTHLY_TOTAL,
	WEEKLY,
	DAILY;
    }
}
