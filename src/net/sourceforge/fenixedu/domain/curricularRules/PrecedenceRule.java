package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

public abstract class PrecedenceRule extends PrecedenceRule_Base {

    protected PrecedenceRule() {
	super();
    }

    @Override
    public boolean appliesToContext(final Context context) {
	return (super.appliesToContext(context) && appliesToPeriod(context));
    }

    protected boolean appliesToPeriod(final Context context) {
	return (hasNoCurricularPeriodOrder() || (this.getAcademicPeriod().equals(
		context.getCurricularPeriod().getAcademicPeriod()) && this.getCurricularPeriodOrder().equals(
		context.getCurricularPeriod().getChildOrder())));
    }

    protected boolean hasNoCurricularPeriodOrder() {
	return (this.getAcademicPeriod() == null || this.getCurricularPeriodOrder() == null || (this.getAcademicPeriod().equals(
		AcademicPeriod.SEMESTER) && this.getCurricularPeriodOrder().equals(0)));
    }

    @Override
    protected void removeOwnParameters() {
	removePrecedenceDegreeModule();
    }
}
