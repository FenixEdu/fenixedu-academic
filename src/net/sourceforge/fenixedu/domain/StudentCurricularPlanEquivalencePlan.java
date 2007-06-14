package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanEquivalencePlan extends StudentCurricularPlanEquivalencePlan_Base {

    public StudentCurricularPlanEquivalencePlan(final StudentCurricularPlan studentCurricularPlan) {
	super();

	init(studentCurricularPlan);
    }

    private void init(StudentCurricularPlan studentCurricularPlan) {
	checkParameters(studentCurricularPlan);

	super.setStudentCurricularPlan(studentCurricularPlan);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan) {
	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.StudentCurricularPlanEquivalencePlan.studentCurricularPlan.cannot.be.null");
	}
    }

}
