/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
