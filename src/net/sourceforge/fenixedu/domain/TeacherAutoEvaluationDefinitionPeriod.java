package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class TeacherAutoEvaluationDefinitionPeriod extends TeacherAutoEvaluationDefinitionPeriod_Base {
	public TeacherAutoEvaluationDefinitionPeriod() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public TeacherAutoEvaluationDefinitionPeriod(ExecutionYear executionYear, YearMonthDay startDate,
			YearMonthDay endDate) {
		this();
		this.setExecutionYear(executionYear);
		this.setStartDateYearMonthDay(startDate);
		this.setEndDateYearMonthDay(endDate);
	}

	public void edit(YearMonthDay startDate, YearMonthDay endDate) {
		this.setStartDateYearMonthDay(startDate);
		this.setEndDateYearMonthDay(endDate);
	}

	public Boolean isPeriodOpen() {
		
		Interval interval = new Interval(getStartDateYearMonthDay().toDateTimeAtMidnight(), getEndDateYearMonthDay().toDateTimeAtMidnight());
		return interval.contains(new DateTime());
	}

}
