/**
* Nov 22, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadDomainSupportLessonByOID extends Service {

    public SupportLesson run(Integer supportLessonID) throws ExcepcaoPersistencia{
        
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        return (SupportLesson) persistentSupport.getIPersistentSupportLesson().readByOID(
                SupportLesson.class, supportLessonID);
    }
}


