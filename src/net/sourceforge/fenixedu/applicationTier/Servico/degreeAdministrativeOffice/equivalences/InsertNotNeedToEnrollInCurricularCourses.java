/**
* Jul 29, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

/**
 * @author Ricardo Rodrigues
 *
 */

public class InsertNotNeedToEnrollInCurricularCourses extends Service {

    public void run(Integer studentCurricularPlanID, Integer[] curricularCoursesID) throws ExcepcaoPersistencia{
        IPersistentStudentCurricularPlan scpDAO = persistentSupport.getIStudentCurricularPlanPersistente();
        IPersistentCurricularCourse ccDAO = persistentSupport.getIPersistentCurricularCourse();
        
        StudentCurricularPlan scp = (StudentCurricularPlan) scpDAO.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
        
        for (int iter = 0; iter < curricularCoursesID.length; iter++) {
            Integer curricularCourseID = curricularCoursesID[iter];
            CurricularCourse curricularCourse = (CurricularCourse) ccDAO.readByOID(CurricularCourse.class, curricularCourseID);
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = DomainFactory.makeNotNeedToEnrollInCurricularCourse();
            notNeedToEnrollInCurricularCourse.setCurricularCourse(curricularCourse);
            notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(scp);
        }        
    }
    
}


