package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;

public class AffinityCyclesManagement {
    private StudentCurricularPlan studentCurricularPlan;

    public AffinityCyclesManagement(final StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = studentCurricularPlan;
    }

    private StudentCurricularPlan getStudentCurricularPlan() {
	return this.studentCurricularPlan;
    }

    public Registration enrol(final CycleCourseGroup cycleCourseGroup) {
	return separateSecondCycle();
    }

    protected Registration separateSecondCycle() {
	return new SeparationCyclesManagement().separateSecondCycle(getStudentCurricularPlan());
    }

}
