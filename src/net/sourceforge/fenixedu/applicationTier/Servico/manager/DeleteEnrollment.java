/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.utils.enrolment.DeleteEnrolmentUtils;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteEnrollment implements IService {

    public void run(final Integer enrollmentId) throws ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentEnrollment persistentEnrollment = persistentSupport.getIPersistentEnrolment();

        final IEnrolment enrollment = (IEnrolment) persistentEnrollment.readByOID(Enrolment.class, enrollmentId);
        DeleteEnrolmentUtils.deleteEnrollment(persistentSupport,enrollment);
    }

    public static void deleteAssociatedEnrollmentInformation(
            final ISuportePersistente persistentSupport,
            final IEnrolment enrollment) throws ExcepcaoPersistencia {
        final IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = persistentSupport
        .getIPersistentEnrolmentEvaluation();
        final IFrequentaPersistente persistentAttend = persistentSupport.getIFrequentaPersistente();

        final List enrollmentEvaluations = enrollment.getEvaluations();
        for (final Iterator iterator = enrollmentEvaluations.iterator(); iterator.hasNext();) {
            final IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
			
			DeleteEnrolmentUtils.deleteEnrolmentEvaluation(enrolmentEvaluation, persistentEnrolmentEvaluation);
        }

        IAttends attend = persistentAttend.readByEnrolment(enrollment);
        if (attend != null) {
            persistentAttend.simpleLockWrite(attend);
            attend.setEnrolment(null);
        }
    }

}
