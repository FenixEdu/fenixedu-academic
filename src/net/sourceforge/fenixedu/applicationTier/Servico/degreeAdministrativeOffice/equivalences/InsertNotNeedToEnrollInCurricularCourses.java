/**
* Jul 29, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.equivalences;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class InsertNotNeedToEnrollInCurricularCourses implements IService {

    public void run(Integer studentCurricularPlanID, Integer[] curricularCoursesID) throws ExcepcaoPersistencia{
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan scpDAO = sp.getIStudentCurricularPlanPersistente();
        IPersistentCurricularCourse ccDAO = sp.getIPersistentCurricularCourse();
        
        IStudentCurricularPlan scp = (IStudentCurricularPlan) scpDAO.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        
        for (int iter = 0; iter < curricularCoursesID.length; iter++) {
            Integer curricularCourseID = curricularCoursesID[iter];
            ICurricularCourse curricularCourse = (ICurricularCourse) ccDAO.readByOID(CurricularCourse.class, curricularCourseID);
            INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = new NotNeedToEnrollInCurricularCourse();
            notNeedToEnrollInCurricularCourse.setCurricularCourse(curricularCourse);
            notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(scp);
        }        
    }
    
}


