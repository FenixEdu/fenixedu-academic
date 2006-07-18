/*
 * Created on Jan 11, 2005
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Filtro.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
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

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {

        final IUserView userView = getRemoteUser(request);
        String costCenter = (String) request.getServiceParameters().parametersArray()[1];

        ServiceParameters s = request.getServiceParameters();

        Teacher teacher = Teacher.readTeacherByUsername(userView.getUtilizador());
        Integer userNumber = null;
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            Employee employee = userView.getPerson().getEmployee();
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        if (userNumber == null)
            throw new NotAuthorizedFilterException();
        s.addParameter("userNumber", userNumber.toString(), s.parametersArray().length);

        if (costCenter != null && !costCenter.equals("")) {
            Role role = Role.getRoleByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
            if (!costCenter.equals(role.getPortalSubApplication())) {
                PersistentSuportOracle p = PersistentSuportOracle.getInstance();
                if (p.getIPersistentProjectUser().getCCNameByCoordinatorAndCC(userNumber,
                        new Integer(costCenter)) != null) {
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