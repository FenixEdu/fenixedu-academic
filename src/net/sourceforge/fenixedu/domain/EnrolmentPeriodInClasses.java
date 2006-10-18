/*
 * Created on 2004/08/24
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author Luis Cruz
 */
public class EnrolmentPeriodInClasses extends EnrolmentPeriodInClasses_Base {

    public EnrolmentPeriodInClasses(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionPeriod executionPeriod, final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionPeriod, startDate, endDate);
    }

}