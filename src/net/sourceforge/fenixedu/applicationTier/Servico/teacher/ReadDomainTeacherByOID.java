/**
* Nov 24, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadDomainTeacherByOID extends Service {

    public Teacher run(Integer teacherID) {
        return rootDomainObject.readTeacherByOID(teacherID);
    }
}
