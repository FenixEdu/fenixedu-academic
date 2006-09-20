package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

public class WriteBolonhaEnrolmentsList extends WriteEnrollmentsList {

    @Override
    protected void createEnrollment(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass, IUserView userView) {
	final Enrolment enrollment = studentCurricularPlan
		.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);

	if (enrollment == null) {

	    if (enrollmentClass == null || enrollmentClass.intValue() == 0
		    || enrollmentClass.intValue() == 1) {

		new Enrolment(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse, executionPeriod,
			getEnrollmentCondition(enrollmentType), userView.getUtilizador());

	    } else if (enrollmentClass.intValue() == 2) {

		new EnrolmentInOptionalCurricularCourse(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse,
			executionPeriod, getEnrollmentCondition(enrollmentType), userView
				.getUtilizador());

	    } else {
		new EnrolmentInExtraCurricularCourse(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse,
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

}
