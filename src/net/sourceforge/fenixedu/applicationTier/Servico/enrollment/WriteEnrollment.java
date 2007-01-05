package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrollment extends Service {

    // some of these arguments may be null. they are only needed for filter
    public Integer run(Integer executionDegreeId, Registration registration, Integer curricularCourseID,
	    Integer executionPeriodID, CurricularCourseEnrollmentType enrollmentType,
	    Integer enrollmentClass, IUserView userView) {

	final StudentCurricularPlan studentCurricularPlan = registration
		.getActiveStudentCurricularPlan();

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(curricularCourseID);

	ExecutionPeriod executionPeriod = null;
	if (executionPeriodID == null) {
	    executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	} else {
	    executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
	}

	final Enrolment enrollment = studentCurricularPlan
		.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);

	if (enrollment == null) {

	    if (enrollmentClass == null || enrollmentClass.intValue() == 1
		    || enrollmentClass.intValue() == 0) {
		new Enrolment(studentCurricularPlan, curricularCourse, executionPeriod,
			getEnrollmentCondition(enrollmentType), userView.getUtilizador());
	    } else if (enrollmentClass.intValue() == 2) {
		new EnrolmentInOptionalCurricularCourse(studentCurricularPlan, curricularCourse,
			executionPeriod, getEnrollmentCondition(enrollmentType), userView
				.getUtilizador());
	    } else {
		new Enrolment(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse,
			executionPeriod, getEnrollmentCondition(enrollmentType), userView
				.getUtilizador(), true);
	    }

	} else {
	    if (enrollment.getEnrolmentCondition() == EnrollmentCondition.INVISIBLE) {
		enrollment.setEnrolmentCondition(getEnrollmentCondition(enrollmentType));
	    }
	    if (enrollment.getEnrollmentState() == EnrollmentState.ANNULED) {
		enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
	    }
	}
	return (enrollment != null) ? enrollment.getIdInternal() : null;
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