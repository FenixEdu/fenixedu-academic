package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;

public class BolonhaStudentSpecialSeasonEnrolmentLayout extends BolonhaStudentEnrolmentLayout {

    @Override
    protected void generateCycleCourseGroupsToEnrol(final HtmlBlockContainer container,
	    final ExecutionSemester executionSemester, final StudentCurricularPlan studentCurricularPlan, int depth) {

	if (studentCurricularPlan.hasConcludedAnyInternalCycle()
		&& studentCurricularPlan.getDegreeType().hasExactlyOneCycleType()) {
	    return;
	}

	if (isAcademicAdminOfficeEmployee()) {
	    for (final CycleType cycleType : getAllCycleTypesToEnrolPreviousToFirstExistingCycle(studentCurricularPlan)) {
		generateCourseGroupToEnroll(container, buildDegreeModuleToEnrolForCycle(studentCurricularPlan, cycleType,
			executionSemester), depth + getRenderer().getWidthDecreasePerLevel());

	    }
	}

	if (isSelfReferredStudent(studentCurricularPlan))
	    return;

	for (final CycleType cycleType : studentCurricularPlan.getSupportedCycleTypesToEnrol()) {
	    generateCycleCourseGroupToEnrol(container, cycleType, depth + getRenderer().getWidthDecreasePerLevel());
	}
    }

}
