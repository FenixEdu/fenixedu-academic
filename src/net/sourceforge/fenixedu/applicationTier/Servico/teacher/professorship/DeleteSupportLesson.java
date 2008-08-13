/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteSupportLesson extends Service {

    public void run(Integer supportLessonID, RoleType roleType) {
        SupportLesson supportLesson = rootDomainObject.readSupportLessonByOID(supportLessonID);
        supportLesson.delete(roleType);
    }

}
