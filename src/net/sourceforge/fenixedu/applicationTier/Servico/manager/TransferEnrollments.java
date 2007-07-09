package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class TransferEnrollments extends Service {

    public void run(final Integer destinationStudentCurricularPlanId,
	    final Integer[] enrollmentIDsToTransfer, final Integer destinationCurriculumGroupID) {

	if (destinationCurriculumGroupID != null) {

	    CurriculumGroup curriculumGroup = (CurriculumGroup) rootDomainObject
		    .readCurriculumModuleByOID(destinationCurriculumGroupID);
	    StudentCurricularPlan studentCurricularPlan = curriculumGroup.getStudentCurricularPlan();

	    for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
		Enrolment enrolment = (Enrolment) rootDomainObject
			.readCurriculumModuleByOID(enrollmentIDToTransfer);

		fixEnrolmentCurricularCourse(studentCurricularPlan, enrolment);

		enrolment.setCurriculumGroup(curriculumGroup);
		enrolment.removeStudentCurricularPlan();
	    }

	} else {

	    final StudentCurricularPlan studentCurricularPlan = rootDomainObject
		    .readStudentCurricularPlanByOID(destinationStudentCurricularPlanId);
	    for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
		final Enrolment enrollment = (Enrolment) rootDomainObject
			.readCurriculumModuleByOID(enrollmentIDToTransfer);

		fixEnrolmentCurricularCourse(studentCurricularPlan, enrollment);

		if (enrollment.getStudentCurricularPlan() != studentCurricularPlan) {
		    enrollment.setStudentCurricularPlan(studentCurricularPlan);
		    enrollment.removeCurriculumGroup();
		}

	    }
	}
    }

    private void fixEnrolmentCurricularCourse(final StudentCurricularPlan studentCurricularPlan,
	    final Enrolment enrollment) {
	if (enrollment.getCurricularCourse().getDegreeCurricularPlan() != studentCurricularPlan
		.getDegreeCurricularPlan()) {
	    CurricularCourse curricularCourse = studentCurricularPlan.getDegreeCurricularPlan()
		    .getCurricularCourseByCode(enrollment.getCurricularCourse().getCode());
	    if (curricularCourse != null) {
		enrollment.setCurricularCourse(curricularCourse);
	    }
	}
    }

}