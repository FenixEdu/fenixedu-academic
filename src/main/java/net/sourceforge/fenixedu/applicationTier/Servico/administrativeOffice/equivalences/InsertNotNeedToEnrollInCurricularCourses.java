/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNotNeedToEnrollInCurricularCourses extends FenixService {

    protected void run(Integer studentCurricularPlanID, Integer[] curricularCoursesID) {
        StudentCurricularPlan scp = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

        for (Integer curricularCourseID : curricularCoursesID) {
            CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = new NotNeedToEnrollInCurricularCourse();
            notNeedToEnrollInCurricularCourse.setCurricularCourse(curricularCourse);
            notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(scp);
        }
    }

    // Service Invokers migrated from Berserk

    private static final InsertNotNeedToEnrollInCurricularCourses serviceInstance =
            new InsertNotNeedToEnrollInCurricularCourses();

    @Service
    public static void runInsertNotNeedToEnrollInCurricularCourses(Integer studentCurricularPlanID, Integer[] curricularCoursesID)
            throws NotAuthorizedException {
        try {
            DegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
            serviceInstance.run(studentCurricularPlanID, curricularCoursesID);
        } catch (NotAuthorizedException ex1) {
            try {
                ManagerAuthorizationFilter.instance.execute();
                serviceInstance.run(studentCurricularPlanID, curricularCoursesID);
            } catch (NotAuthorizedException ex2) {
                try {
                    OperatorAuthorizationFilter.instance.execute();
                    serviceInstance.run(studentCurricularPlanID, curricularCoursesID);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}