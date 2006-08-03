package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class ReadCurricularCoursesToEnrollSuperUser extends ReadCurricularCoursesToEnroll {

	@Override
	protected List<CurricularCourse> findCurricularCourses(
			final List<CurricularCourse> curricularCourses,
			final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod) {

		final List<CurricularCourse> result = new ArrayList<CurricularCourse>();

		for (final CurricularCourse curricularCourse : curricularCourses) {
			if (!studentCurricularPlan
					.isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(
							curricularCourse, executionPeriod)
					&& !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(
							curricularCourse, executionPeriod)) {

				result.add(curricularCourse);
			}
		}
		return result;
	}
}