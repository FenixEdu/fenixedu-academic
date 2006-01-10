/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.equivalences;

import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentNotNeedToEnrollInCurricularCourse;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteNotNeedToEnrollInCurricularCourse implements IService {

    public void run(Integer notNeedToEnrollInCurricularCourseID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentNotNeedToEnrollInCurricularCourse persistentNotNeedToEnrollInCurricularCourse = sp
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
