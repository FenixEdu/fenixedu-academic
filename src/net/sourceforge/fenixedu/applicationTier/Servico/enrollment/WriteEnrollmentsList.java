package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class WriteEnrollmentsList extends Service {

	public void run(final Registration registration, DegreeType degreeType, ExecutionPeriod executionPeriod,
			List<String> curricularCourses, Map optionalEnrollments, IUserView userView)
			throws FenixServiceException {

		if (registration == null) {
			throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
		}

		final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
		}

		for (final String enrolmentInfo : curricularCourses) {
			final Integer curricularCourseId = Integer.valueOf(enrolmentInfo.split("-")[0]);
			final CurricularCourse curricularCourse = readCurricularCourse(curricularCourseId);
			
            final Integer enrollmentclass = Integer.valueOf(optionalEnrollments.get(
					curricularCourseId.toString()).toString());

			createEnrollment(studentCurricularPlan, curricularCourse, executionPeriod,
					CurricularCourseEnrollmentType.VALIDATED, enrollmentclass, userView);
		}
	}

	private CurricularCourse readCurricularCourse(final Integer curricularCourseId) {
		return (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
	}

	protected void createEnrollment(final StudentCurricularPlan studentCurricularPlan,
			final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod,
			final CurricularCourseEnrollmentType enrollmentType, final Integer enrollmentClass,
			final IUserView userView) {

		final Enrolment enrollment = studentCurricularPlan
				.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);

		if (enrollment == null) {

			if (enrollmentClass == null || enrollmentClass.intValue() == 0
					|| enrollmentClass.intValue() == 1) {

				new Enrolment(studentCurricularPlan, curricularCourse, executionPeriod,
						getEnrollmentCondition(enrollmentType), userView.getUtilizador());

			} else if (enrollmentClass.intValue() == 2) {

				new EnrolmentInOptionalCurricularCourse(studentCurricularPlan, curricularCourse,
						executionPeriod, getEnrollmentCondition(enrollmentType), userView
								.getUtilizador());

			} else {
				new EnrolmentInExtraCurricularCourse(studentCurricularPlan, curricularCourse,
						executionPeriod, getEnrollmentCondition(enrollmentType), userView
								.getUtilizador());
			}

		} else {
			if (enrollment.getCondition() == EnrollmentCondition.INVISIBLE) {
				enrollment.setCondition(getEnrollmentCondition(enrollmentType));
			}
			if (enrollment.getEnrollmentState() == EnrollmentState.ANNULED) {
				enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
			}
		}
	}

	protected EnrollmentCondition getEnrollmentCondition(CurricularCourseEnrollmentType enrollmentType) {
		switch (enrollmentType) {
		case TEMPORARY:
			return EnrollmentCondition.TEMPORARY;
		case DEFINITIVE:
			return EnrollmentCondition.FINAL;
		case NOT_ALLOWED:
			return EnrollmentCondition.IMPOSSIBLE;
		case VALIDATED:
			return EnrollmentCondition.VALIDATED;
		default:
			return null;
		}
	}
}