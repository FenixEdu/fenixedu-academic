/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.util.DateFormatUtil;

/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends EnrolmentPeriod_Base {

    public EnrolmentPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionPeriod executionPeriod, final Date startDate, final Date endDate) {
	setDegreeCurricularPlan(degreeCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setStartDate(startDate);
	setEndDate(endDate);
    }
    
    protected void init(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionPeriod executionPeriod, final DateTime startDate, final DateTime endDate) {
	setDegreeCurricularPlan(degreeCurricularPlan);
	setExecutionPeriod(executionPeriod);
	setStartDateDateTime(startDate);
	setEndDateDateTime(endDate);
    }

    public boolean isValid() {
	Date date = new Date();
	return isValid(date);
    }

    public boolean isValid(Date date) {
	return (DateFormatUtil.compareDates("yyyyMMddHHmm", this.getStartDate(), date) <= 0)
		&& (DateFormatUtil.compareDates("yyyyMMddHHmm", this.getEndDate(), date) >= 0);
    }

    public boolean containsDate(DateTime date) {
	return !(getStartDateDateTime().isAfter(date) || getEndDateDateTime().isBefore(date));
    }

}