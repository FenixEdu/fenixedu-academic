/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentNotNeedToEnrollInCurricularCourse;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteNotNeedToEnrollInCurricularCourse extends Service {

    public void run(Integer notNeedToEnrollInCurricularCourseID) throws ExcepcaoPersistencia {
        IPersistentNotNeedToEnrollInCurricularCourse persistentNotNeedToEnrollInCurricularCourse = persistentSupport
                .getIPersistentNotNeedToEnrollInCurricularCourse();

        NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) 
            persistentNotNeedToEnrollInCurricularCourse.readByOID(NotNeedToEnrollInCurricularCourse.class,
                    notNeedToEnrollInCurricularCourseID);
      
        notNeedToEnrollInCurricularCourse.setCurricularCourse(null);
        notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(null);

        persistentNotNeedToEnrollInCurricularCourse.deleteByOID(NotNeedToEnrollInCurricularCourse.class,
                notNeedToEnrollInCurricularCourseID);
    }

}
