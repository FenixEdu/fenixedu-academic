/*
 * Created on Feb 18, 2005
 *
 */
package ServidorAplicacao.Servico.manager;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan implements IService {

    public void run(final Integer studentCurricularPlanId) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistentStudentCurricularPlan
                .readByOID(StudentCurricularPlan.class, studentCurricularPlanId);
        final List enrollments = studentCurricularPlan.getEnrolments();
        for (final Iterator iterator = enrollments.iterator(); iterator.hasNext();) {
            final IEnrollment enrollment = (IEnrollment) iterator.next();
            DeleteEnrollment.deleteAssociatedEnrollmentInformation(persistentSupport, enrollment);
            persistentEnrollment.delete(enrollment);
        }

        persistentStudentCurricularPlan.delete(studentCurricularPlan);
    }

}
