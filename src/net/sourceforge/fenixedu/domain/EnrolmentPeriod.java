/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends EnrolmentPeriod_Base {

    public EnrolmentPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
	    final Date startDate, final Date endDate) {
	init(degreeCurricularPlan, executionSemester, new DateTime(startDate), new DateTime(endDate));
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
	    final DateTime startDate, final DateTime endDate) {

	if (!endDate.isAfter(startDate)) {
	    throw new DomainException("EnrolmentPeriod.end.date.must.be.after.start.date");
	}

	setDegreeCurricularPlan(degreeCurricularPlan);
	setExecutionPeriod(executionSemester);
	setStartDateDateTime(startDate);
	setEndDateDateTime(endDate);
    }

    public boolean isValid() {
	return containsDate(new DateTime());
    }

    public boolean isValid(final Date date) {
	return containsDate(new DateTime(date));
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