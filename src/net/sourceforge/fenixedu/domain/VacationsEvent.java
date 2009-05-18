package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class VacationsEvent implements GanttDiagramEvent {

    MultiLanguageString title;

    int month;

    Interval eventInterval;

    Map<Interval, DayType> leaveIntervals = new HashMap<Interval, DayType>();

    Integer employeeNumber;

    public VacationsEvent(MultiLanguageString title, int month, Interval eventInterval, Integer employeeNumber) {

	super();

	setTitle(title);
	setMonth(month);
	setEventInterval(eventInterval);
	setEmployeeNumber(employeeNumber);
    }

    public void setTitle(MultiLanguageString title) {
	this.title = title;
    }

    public void setMonth(int month) {
	this.month = month;
    }

    public void setEventInterval(Interval eventInterval) {
	this.eventInterval = eventInterval;
    }

    public void setLeaveIntervals(Map<Interval, DayType> leaveIntervals) {
	this.leaveIntervals = leaveIntervals;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
	this.employeeNumber = employeeNumber;
    }

    public MultiLanguageString getTitle() {
	return title;
    }

    public Interval getEventInterval() {
	return eventInterval;
    }

    public Map<Interval, DayType> getLeaveIntervals() {
	return leaveIntervals;
    }

    public Integer getEmployeeNumber() {
	return employeeNumber;
    }

    @Override
    public MultiLanguageString getGanttDiagramEventName() {
	return getTitle();
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
	List<Interval> intervals = new ArrayList<Interval>();

	for (Interval leaveInterval : leaveIntervals.keySet()) {
	    intervals.add(leaveInterval);
	}

	Collections.sort(intervals, new BeanComparator("startMillis"));
	return intervals;
    }

    public Integer getGanttDiagramEventMonth() {
	return new Integer(month);
    }

    public String getGanttDiagramEventUrlAddOns() {
	if (getEmployeeNumber() != null) {
	    return "&amp;employeeNumber=" + getEmployeeNumber().toString();
	} else {
	    return null;
	}
    }

    public boolean isGanttDiagramEventIntervalsLongerThanOneDay() {
	return true;
    }

    public boolean isGanttDiagramEventToMarkWeekendsAndHolidays() {
	return true;
    }

    public DayType getGanttDiagramEventDayType(Interval interval) {
	return getLeaveIntervals().get(interval);
    }

    public int getGanttDiagramEventOffset() {
	return 0;
    }

    public String getGanttDiagramEventPeriod() {
	return "-";
    }

    public String getGanttDiagramEventObservations() {
	return "-";
    }

    public String getGanttDiagramEventIdentifier() {
	return "GanttDiagramEvent";
    }

    public void addNewInterval(Interval interval, DayType dayType) {
	if (getEventInterval().overlaps(interval)) {
	    getLeaveIntervals().put(getEventInterval().overlap(interval), dayType);
	}
    }

    @Service
    static public VacationsEvent create(final MultiLanguageString title, final int month, final Interval eventInterval,
	    final Integer employeeNumber) {
	return new VacationsEvent(title, month, eventInterval, employeeNumber);
    }
}
