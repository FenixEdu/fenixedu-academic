/**
 * Nov 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteTeacherAdviseServiceByOID {

    protected void run(Integer teacherAdviseServiceID, RoleType roleType) {
        TeacherAdviseService teacherAdviseService =
                (TeacherAdviseService) AbstractDomainObject.fromExternalId(teacherAdviseServiceID);
        teacherAdviseService.delete(roleType);
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTeacherAdviseServiceByOID serviceInstance = new DeleteTeacherAdviseServiceByOID();

    @Service
    public static void runDeleteTeacherAdviseServiceByOID(Integer teacherAdviseServiceID, RoleType roleType)
            throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(teacherAdviseServiceID, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(teacherAdviseServiceID, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(teacherAdviseServiceID, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}