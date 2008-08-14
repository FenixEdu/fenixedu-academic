package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;

public class StudentEquivalencyPlanEntryCreator extends EquivalencePlanEntryCreator {

    private final DomainReference<DegreeCurricularPlanEquivalencePlan> degreeCurricularPlanEquivalencePlan;

    public StudentEquivalencyPlanEntryCreator(final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan,
	    final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan) {
	super(studentCurricularPlanEquivalencePlan);
	this.degreeCurricularPlanEquivalencePlan = degreeCurricularPlanEquivalencePlan == null ? null
		: new DomainReference<DegreeCurricularPlanEquivalencePlan>(degreeCurricularPlanEquivalencePlan);
    }

    public DegreeCurricularPlanEquivalencePlan getDegreeCurricularPlanEquivalencePlan() {
	return degreeCurricularPlanEquivalencePlan == null ? null : degreeCurricularPlanEquivalencePlan.getObject();
    }

    public StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan() {
	return (StudentCurricularPlanEquivalencePlan) getEquivalencePlan();
    }

}
