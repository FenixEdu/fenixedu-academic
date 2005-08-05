/**
* Aug 1, 2005
*/
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentNotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Ricardo Rodrigues
 *
 */

public class NotNeedToEnrollInCurricularCourseVO extends VersionedObjectsBase implements IPersistentNotNeedToEnrollInCurricularCourse{
    
    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(NotNeedToEnrollInCurricularCourse.class);
    }

}


