/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteNotNeedToEnrollInCurricularCourse extends Service {

    public void run(Integer notNeedToEnrollInCurricularCourseID) throws ExcepcaoPersistencia {
        NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) 
            persistentObject.readByOID(NotNeedToEnrollInCurricularCourse.class,
                    notNeedToEnrollInCurricularCourseID);
      
        notNeedToEnrollInCurricularCourse.setCurricularCourse(null);
        notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(null);

        persistentObject.deleteByOID(NotNeedToEnrollInCurricularCourse.class,
                notNeedToEnrollInCurricularCourseID);
    }

}
