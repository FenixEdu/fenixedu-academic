/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.DateTime;

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
	
	if (!endDate.after(startDate)) {
	    throw new DomainException("EnrolmentPeriod.end.date.must.be.after.start.date");
	}
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

    public void delete() {
	removeDegreeCurricularPlan();
	removeExecutionPeriod();
	removeRootDomainObject();
	deleteDomainObject();
    }

}