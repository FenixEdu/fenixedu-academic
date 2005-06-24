/*
 * Created on Jan 11, 2005
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Filtro.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;

/**
 * @author Susana Fernandes
 */
public class ProjectsManagerAuthorizationFilter extends AuthorizationByRoleFilter {
    public final static ProjectsManagerAuthorizationFilter instance = new ProjectsManagerAuthorizationFilter();

    protected RoleType roleToAuthorize = RoleType.PROJECTS_MANAGER;

    public static Filtro getInstance() {
        return instance;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {

        String username = (String) request.getServiceParameters().parametersArray()[0];
        String costCenter = (String) request.getServiceParameters().parametersArray()[1];

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ServiceParameters s = request.getServiceParameters();

        ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(username);
        Integer userNumber = null;
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        if (userNumber == null)
            throw new NotAuthorizedFilterException();
        s.addParameter("userNumber", userNumber.toString(), s.parametersArray().length);

        if (costCenter != null && !costCenter.equals("")) {
            IRole role = sp.getIPersistentRole().readByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
            if (!costCenter.equals(role.getPortalSubApplication())) {
                PersistentSuportOracle p = PersistentSuportOracle.getInstance();
                if (p.getIPersistentProjectUser().getCCNameByCoordinatorAndCC(userNumber, new Integer(costCenter))!=null) {
                    s.addParameter("userNumber", costCenter, s.parametersArray().length - 1);
                }
            }
            roleToAuthorize = RoleType.INSTITUCIONAL_PROJECTS_MANAGER;
        } else {
            roleToAuthorize = RoleType.PROJECTS_MANAGER;
            s.addParameter("costCenter", "", 1);
        }
        super.execute(request, response);
        request.setServiceParameters(s);
    }

    protected RoleType getRoleType() {
        return roleToAuthorize;
    }

}