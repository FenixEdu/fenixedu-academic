/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteEnrollment extends Service {

    public void run(final Integer enrollmentId) throws ExcepcaoPersistencia {
        final Enrolment enrollment = (Enrolment) rootDomainObject.readCurriculumModuleByOID(enrollmentId);
        enrollment.delete();
    }

}
