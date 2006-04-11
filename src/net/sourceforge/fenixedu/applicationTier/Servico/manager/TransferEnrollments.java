package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class TransferEnrollments extends Service {

    public void run(final Integer destinationStudentCurricularPlanId, final Integer[] enrollmentIDsToTransfer) {
        final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(destinationStudentCurricularPlanId);
        for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
            final Enrolment enrollment = studentCurricularPlan.findEnrolmentByEnrolmentID(enrollmentIDToTransfer);
            enrollment.setStudentCurricularPlan(studentCurricularPlan);
        }
    }

}