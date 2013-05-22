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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.ServiceParameters;

/**
 * @author Susana Fernandes
 */
public class ProjectsManagerAuthorizationFilter extends AuthorizationByRoleFilter {
    public final static ProjectsManagerAuthorizationFilter instance = new ProjectsManagerAuthorizationFilter();

    protected ThreadLocal<RoleType> roleToAuthorize = new ThreadLocal<RoleType>();

    public static Filtro getInstance() {
        return instance;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {
        final IUserView userView = getRemoteUser(request);
        ServiceParameters serviceParameters = request.getServiceParameters();
        Object[] parametersArray = serviceParameters.parametersArray();
        String costCenter = (String) parametersArray[1];
        BackendInstance instance = BackendInstance.IST;
        if (parametersArray[parametersArray.length - 1] != null) {
            instance = (BackendInstance) parametersArray[parametersArray.length - 1];
        }
        Integer userNumber = getUserNumber(userView.getPerson());
        if (userNumber == null) {
            throw new NotAuthorizedFilterException();
        }
        serviceParameters.addParameter("userNumber", userNumber.toString(), serviceParameters.parametersArray().length);

        final RoleType roleType;
        if (costCenter != null && !costCenter.equals("")) {
            roleType = instance.institutionalRoleType;
            final Role role = Role.getRoleByRoleType(roleType);
            if (!costCenter.equals(role.getPortalSubApplication()) && StringUtils.isNumeric(costCenter)) {
                if (new PersistentProjectUser().getCCNameByCoordinatorAndCC(userNumber, new Integer(costCenter), instance) != null) {
                    serviceParameters.addParameter("userNumber", costCenter, serviceParameters.parametersArray().length - 1);
                }
            }
        } else {
            roleType = instance.roleType;
            serviceParameters.addParameter("costCenter", "", 1);
        }
        roleToAuthorize.set(roleType);
        super.execute(request, response);
        request.setServiceParameters(serviceParameters);
    }

    private Integer getUserNumber(final Person person) {
        final Employee employee = person == null ? null : person.getEmployee();
        Integer userNumber = null;
        if (employee != null) {
            userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }

    @Override
    protected RoleType getRoleType() {
        return roleToAuthorize.get();
    }

}