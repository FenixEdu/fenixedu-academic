/**
* Aug 1, 2005
*/
package net.sourceforge.fenixedu.persistenceTier.OJB.student;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentNotNeedToEnrollInCurricularCourse;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ricardo Rodrigues
 *
 */

public class NotNeedToEnrollInCurricularCourseOJB extends PersistentObjectOJB implements IPersistentNotNeedToEnrollInCurricularCourse {
    
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(NotNeedToEnrollInCurricularCourse.class, criteria);
    }

}


