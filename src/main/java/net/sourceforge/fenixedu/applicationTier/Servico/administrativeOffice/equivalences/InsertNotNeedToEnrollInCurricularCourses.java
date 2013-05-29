/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNotNeedToEnrollInCurricularCourses {

    protected void run(String studentCurricularPlanID, String[] curricularCoursesID) {
        StudentCurricularPlan scp = AbstractDomainObject.fromExternalId(studentCurricularPlanID);

        for (String curricularCourseID : curricularCoursesID) {
            CurricularCourse curricularCourse = (CurricularCourse) AbstractDomainObject.fromExternalId(curricularCourseID);
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = new NotNeedToEnrollInCurricularCourse();
            notNeedToEnrollInCurricularCourse.setCurricularCourse(curricularCourse);
            notNeedToEnrollInCurricularCourse.setStudentCurricularPlan(scp);
        }
    }

    // Service Invokers migrated from Berserk

    private static final InsertNotNeedToEnrollInCurricularCourses serviceInstance =
            new InsertNotNeedToEnrollInCurricularCourses();

    @Service
    public static void runInsertNotNeedToEnrollInCurricularCourses(String studentCurricularPlanID, String[] curricularCoursesID)
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