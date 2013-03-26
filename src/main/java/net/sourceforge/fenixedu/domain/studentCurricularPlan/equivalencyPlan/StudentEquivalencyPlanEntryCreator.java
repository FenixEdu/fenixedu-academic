package net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;

public class StudentEquivalencyPlanEntryCreator extends EquivalencePlanEntryCreator {

    private final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan;

    public StudentEquivalencyPlanEntryCreator(final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan,
            final DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan) {
        super(studentCurricularPlanEquivalencePlan);
        this.degreeCurricularPlanEquivalencePlan = degreeCurricularPlanEquivalencePlan;
    }

    public DegreeCurricularPlanEquivalencePlan getDegreeCurricularPlanEquivalencePlan() {
        return degreeCurricularPlanEquivalencePlan;
    }

    public StudentCurricularPlanEquivalencePlan getStudentCurricularPlanEquivalencePlan() {
        return (StudentCurricularPlanEquivalencePlan) getEquivalencePlan();
    }

}
