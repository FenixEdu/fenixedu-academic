/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
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
        enrollment.delete();
    }
}
