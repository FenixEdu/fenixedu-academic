package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class VacationsEvent implements GanttDiagramEvent {

    MultiLanguageString title;

    int month;

    Interval eventInterval;

    List<Interval> leaveIntervals = new ArrayList<Interval>();

    public VacationsEvent(MultiLanguageString title, int month, Interval eventInterval) {

	super();

	setTitle(title);
	setMonth(month);
	setEventInterval(eventInterval);
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

    public void setLeaveIntervals(List<Interval> leaveIntervals) {
	this.leaveIntervals = leaveIntervals;
    }

    public MultiLanguageString getTitle() {
	return title;
    }

    public int getMonth() {
	return month;
    }

    public Interval getEventInterval() {
	return eventInterval;
    }

    public List<Interval> getLeaveIntervals() {
	return leaveIntervals;
    }

    @Override
    public MultiLanguageString getGanttDiagramEventName() {
	return getTitle();
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
	Collections.sort(leaveIntervals, new BeanComparator("startMillis"));
	return leaveIntervals;
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

    public void addNewInterval(Interval interval) {
	if (getEventInterval().overlaps(interval)) {
	    getLeaveIntervals().add(getEventInterval().overlap(interval));
	}
    }

    @Service
    static public VacationsEvent create(final MultiLanguageString title, final int month, final Interval eventInterval) {
	return new VacationsEvent(title, month, eventInterval);
    }
}
