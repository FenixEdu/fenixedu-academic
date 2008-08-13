/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNotNeedToEnrollInCurricularCourses extends Service {

    public void run(Integer studentCurricularPlanID, Integer[] curricularCoursesID) {
	StudentCurricularPlan scp = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

	for (int iter = 0; iter < curricularCoursesID.length; iter++) {
	    Integer curricularCourseID = curricularCoursesID[iter];
	    CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);
	    NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = new NotNeedToEnrollInCurricularCourse();
	    notNeedToEnrollInCurricularCourse.setCurricularCourse(curricularCourse);
	    notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(scp);
	}
    }

}
