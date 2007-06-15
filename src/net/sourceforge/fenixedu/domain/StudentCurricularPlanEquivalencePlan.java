package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanEquivalencePlan extends StudentCurricularPlanEquivalencePlan_Base {

    public StudentCurricularPlanEquivalencePlan(final StudentCurricularPlan studentCurricularPlan) {
	super();

	init(studentCurricularPlan);
    }

    private void init(StudentCurricularPlan oldStudentCurricularPlan) {
	checkParameters(oldStudentCurricularPlan);

	super.setOldStudentCurricularPlan(oldStudentCurricularPlan);
    }

    private void checkParameters(StudentCurricularPlan oldStudentCurricularPlan) {
	if (oldStudentCurricularPlan == null) {
	    throw new DomainException(
		    "error.StudentCurricularPlanEquivalencePlan.oldStudentCurricularPlan.cannot.be.null");
	}
    }

}
