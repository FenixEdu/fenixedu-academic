package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class CandidacyPeriodInDegreeCurricularPlan extends CandidacyPeriodInDegreeCurricularPlan_Base {

    public CandidacyPeriodInDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan,
	    final ExecutionPeriod executionPeriod, final Date startDate, final Date endDate) {
	super();
	init(degreeCurricularPlan, executionPeriod, startDate, endDate);
    }

}
