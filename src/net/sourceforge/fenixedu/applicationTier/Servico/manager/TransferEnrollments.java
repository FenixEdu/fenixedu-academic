package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

public class TransferEnrollments extends Service {

    public void run(final Integer destinationStudentCurricularPlanId,
            final Integer[] enrollmentIDsToTransfer) throws ExcepcaoPersistencia {

        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, destinationStudentCurricularPlanId);

        for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
            final Integer enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
            final Enrolment enrollment = (Enrolment) persistentEnrollment.readByOID(Enrolment.class, enrollmentIDToTransfer);

            enrollment.setStudentCurricularPlan(studentCurricularPlan);
        }
    }

}