package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class TransferEnrollments implements IService {

    public void run(final Integer destinationStudentCurricularPlanId,
            final Integer[] enrollmentIDsToTransfer) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, destinationStudentCurricularPlanId);

        for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
            final Integer enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
            final IEnrollment enrollment = (IEnrollment) persistentEnrollment.readByOID(Enrolment.class, enrollmentIDToTransfer);

            persistentEnrollment.simpleLockWrite(enrollment);
            enrollment.setStudentCurricularPlan(studentCurricularPlan);
        }
    }

}