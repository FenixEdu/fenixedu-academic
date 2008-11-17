package net.sourceforge.fenixedu.domain.util.workflow;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class StateBean {

    private String nextState;

    private DateTime stateDateTime;

    public StateBean() {
	super();
	this.stateDateTime = null;
    }

    public StateBean(final String nextState) {
	this();
	this.nextState = nextState;
    }

    public StateBean(final String nextState, final YearMonthDay stateDate) {
	this(nextState);
	setStateDate(stateDate);
    }

    public String getNextState() {
	return nextState;
    }

    public void setNextState(final String nextState) {
	this.nextState = nextState;
    }

    public DateTime getStateDateTime() {
	return stateDateTime;
    }

    /**
     * For presentation usage only.
     * 
     */
    public YearMonthDay getStateDate() {
	return stateDateTime == null ? null : stateDateTime.toYearMonthDay();
    }

    public void setStateDate(final YearMonthDay ymd) {
	if (ymd == null) {
	    this.stateDateTime = null;
	    return;
	}

	this.stateDateTime = new YearMonthDay().equals(ymd) ? ymd.toDateTimeAtCurrentTime() : ymd.toDateTimeAtMidnight();
    }

}
