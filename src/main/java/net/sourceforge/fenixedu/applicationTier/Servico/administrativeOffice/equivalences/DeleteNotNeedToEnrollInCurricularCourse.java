/**
 * Jul 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences;

import net.sourceforge.fenixedu.applicationTier.Filtro.AcademicAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteNotNeedToEnrollInCurricularCourse {

    protected void run(String notNeedToEnrollInCurricularCourseID) {
        NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse =
                FenixFramework.getDomainObject(notNeedToEnrollInCurricularCourseID);
        notNeedToEnrollInCurricularCourse.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteNotNeedToEnrollInCurricularCourse serviceInstance = new DeleteNotNeedToEnrollInCurricularCourse();

    @Service
    public static void runDeleteNotNeedToEnrollInCurricularCourse(String notNeedToEnrollInCurricularCourseID)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(notNeedToEnrollInCurricularCourseID);
        } catch (NotAuthorizedException ex1) {
            try {
                AcademicAdministrativeOfficeAuthorizationFilter.instance.execute();
                serviceInstance.run(notNeedToEnrollInCurricularCourseID);
            } catch (NotAuthorizedException ex2) {
                try {
                    DegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(notNeedToEnrollInCurricularCourseID);
                } catch (NotAuthorizedException ex3) {
                    try {
                        OperatorAuthorizationFilter.instance.execute();
                        serviceInstance.run(notNeedToEnrollInCurricularCourseID);
                    } catch (NotAuthorizedException ex4) {
                        throw ex4;
                    }
                }
            }
        }
    }

}