package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class TransferEnrollments extends Service {

    public void run(final Integer destinationStudentCurricularPlanId,
            final Integer[] enrollmentIDsToTransfer) throws ExcepcaoPersistencia {

        final StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(destinationStudentCurricularPlanId);

        for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
            final Integer enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
            final Enrolment enrollment = (Enrolment) rootDomainObject.readCurriculumModuleByOID(enrollmentIDToTransfer);

            enrollment.setStudentCurricularPlan(studentCurricularPlan);
        }
    }

}