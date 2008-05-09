package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public class DegreeContext extends DegreeContext_Base {

    protected DegreeContext() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public DegreeContext(final DegreeCurricularPlan degreeCurricularPlan, final CurricularPeriodType periodType,
	    final AcademicInterval beginInterval, final AcademicInterval endInterval) {
	checkParameters(degreeCurricularPlan, periodType, beginInterval, endInterval);
	setBeginInterval(beginInterval);
	setEndInterval(endInterval);
	setPeriodType(periodType);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan, CurricularPeriodType periodType,
	    AcademicInterval beginInterval, AcademicInterval endInterval) {
	if (degreeCurricularPlan == null || periodType == null || beginInterval == null) {
	    throw new DomainException("error.degree.context.wrong.arguments");
	}
	// TODO verificar que beginInterval <= endInterval
	/*
	 * if (endInterval != null && beginInterval.isA) { }
	 */
    }

}
