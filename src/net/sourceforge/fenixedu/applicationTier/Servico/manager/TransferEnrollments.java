package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class TransferEnrollments extends Service {

    public void run(final Integer destinationStudentCurricularPlanId,
	    final Integer[] enrollmentIDsToTransfer, final Integer destinationCurriculumGroupID) {

	if (destinationCurriculumGroupID != null) {

	    CurriculumGroup curriculumGroup = (CurriculumGroup) rootDomainObject
		    .readCurriculumModuleByOID(destinationCurriculumGroupID);
	    for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
		Enrolment enrolment = (Enrolment) rootDomainObject
			.readCurriculumModuleByOID(enrollmentIDToTransfer);
		enrolment.setCurriculumGroup(curriculumGroup);
		enrolment.removeStudentCurricularPlan();
	    }

	} else {

	    final StudentCurricularPlan studentCurricularPlan = rootDomainObject
		    .readStudentCurricularPlanByOID(destinationStudentCurricularPlanId);
	    for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
		final Enrolment enrollment = (Enrolment) rootDomainObject
			.readCurriculumModuleByOID(enrollmentIDToTransfer);		
		if (enrollment.getStudentCurricularPlan() != studentCurricularPlan) {
		    enrollment.setStudentCurricularPlan(studentCurricularPlan);
		    enrollment.removeCurriculumGroup();
		}

	    }
	}
    }

}