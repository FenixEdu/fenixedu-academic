/**
 * Nov 22, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainSupportLessonByOID extends Service {

    public SupportLesson run(Integer supportLessonID) {
	return rootDomainObject.readSupportLessonByOID(supportLessonID);
    }
}
