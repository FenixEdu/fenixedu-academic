package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentCurricularPlanWithRulesForEnrollments extends ReadStudentCurricularPlanForEnrollments {

	@Override
	public StudentCurricularPlan run(Integer executionDegreeId, Registration student) throws FenixServiceException {

		final ExecutionPeriod actualExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
		final StudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(student);

		if (studentCurricularPlan.hasSpecialSeasonFor(actualExecutionPeriod)) {
			return studentCurricularPlan;

		} else {
			
			final EnrolmentPeriodInCurricularCourses enrolmentPeriod = getEnrolmentPeriod(studentCurricularPlan);
			if (enrolmentPeriod.getExecutionPeriod() == actualExecutionPeriod) {
				return studentCurricularPlan;
			}
			throw new OutOfCurricularCourseEnrolmentPeriod(enrolmentPeriod.getStartDate(), enrolmentPeriod.getEndDate());
		}
	}
	
	private EnrolmentPeriodInCurricularCourses getEnrolmentPeriod(
			final StudentCurricularPlan studentActiveCurricularPlan)
			throws OutOfCurricularCourseEnrolmentPeriod {

		final EnrolmentPeriodInCurricularCourses enrolmentPeriod = studentActiveCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod();
		if (enrolmentPeriod == null) {
			final EnrolmentPeriodInCurricularCourses nextEnrolmentPeriod = studentActiveCurricularPlan.getDegreeCurricularPlan().getNextEnrolmentPeriod();
			final Date startDate = (nextEnrolmentPeriod != null) ? nextEnrolmentPeriod.getStartDate() : null;
			final Date endDate = (nextEnrolmentPeriod != null) ? nextEnrolmentPeriod.getEndDate() : null;
			throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
		}
		return enrolmentPeriod;
	}
}