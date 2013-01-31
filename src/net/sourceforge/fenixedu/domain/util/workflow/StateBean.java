package net.sourceforge.fenixedu.domain.util.workflow;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class StateBean implements Serializable {

	private String nextState;

	private Person responsible;

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

	public Person getResponsible() {
		return responsible;
	}

	public void setResponsible(final Person responsible) {
		this.responsible = responsible;
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

	protected void setStateDateTime(final DateTime dateTime) {
		this.stateDateTime = dateTime;
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
			setStateDateTime(null);
			return;
		}

		setStateDateTime(new YearMonthDay().equals(ymd) ? ymd.toDateTimeAtCurrentTime() : ymd.toDateTimeAtMidnight());
	}

}
