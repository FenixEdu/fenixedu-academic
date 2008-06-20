package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class ReadCurricularCoursesToEnrollSuperUser extends ReadCurricularCoursesToEnroll {

	@Override
	protected List<CurricularCourse> findCurricularCourses(
			final List<CurricularCourse> curricularCourses,
			final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {

		final List<CurricularCourse> result = new ArrayList<CurricularCourse>();

		for (final CurricularCourse curricularCourse : curricularCourses) {
			if (!studentCurricularPlan
					.isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(
							curricularCourse, executionSemester)
					&& !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(
							curricularCourse, executionSemester)) {

				result.add(curricularCourse);
			}
		}
		return result;
	}
}