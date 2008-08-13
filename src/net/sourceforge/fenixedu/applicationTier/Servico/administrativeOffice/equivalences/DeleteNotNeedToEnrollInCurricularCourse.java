/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteNotNeedToEnrollInCurricularCourse extends Service {

    public void run(Integer notNeedToEnrollInCurricularCourseID) {
        NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = rootDomainObject.readNotNeedToEnrollInCurricularCourseByOID(
                    notNeedToEnrollInCurricularCourseID);
        notNeedToEnrollInCurricularCourse.delete();
    }

}
