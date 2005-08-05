/**
* Aug 1, 2005
*/
package net.sourceforge.fenixedu.persistenceTier.student;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Ricardo Rodrigues
 *
 */

public interface IPersistentNotNeedToEnrollInCurricularCourse extends IPersistentObject {
    
    public List readAll() throws ExcepcaoPersistencia;

}


