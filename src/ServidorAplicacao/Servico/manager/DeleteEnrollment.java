/*
 * Created on Feb 18, 2005
 *
 */
package ServidorAplicacao.Servico.manager;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IAttends;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteEnrollment implements IService {

    public void run(final Integer enrollmentId) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final IEnrollment enrollment = (IEnrollment) persistentEnrollment.readByOID(Enrolment.class, enrollmentId);
        deleteAssociatedEnrollmentInformation(persistentSupport, enrollment);
        persistentEnrollment.delete(enrollment);
    }

    public static void deleteAssociatedEnrollmentInformation(
            final ISuportePersistente persistentSupport,
            final IEnrollment enrollment) throws ExcepcaoPersistencia {
        final IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = persistentSupport
        .getIPersistentEnrolmentEvaluation();
        final IFrequentaPersistente persistentAttend = persistentSupport.getIFrequentaPersistente();

        final List enrollmentEvaluations = enrollment.getEvaluations();
        for (final Iterator iterator = enrollmentEvaluations.iterator(); iterator.hasNext();) {
            final IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
            persistentEnrolmentEvaluation.delete(enrolmentEvaluation);
        }

        IAttends attend = persistentAttend.readByEnrolment(enrollment);
        if (attend != null) {
            persistentAttend.simpleLockWrite(attend);
            attend.setEnrolment(null);
        }
    }

}
