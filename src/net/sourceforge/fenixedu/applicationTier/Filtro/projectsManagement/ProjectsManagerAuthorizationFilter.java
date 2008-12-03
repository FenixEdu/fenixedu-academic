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
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;
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

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {
	roleToAuthorize.set(RoleType.PROJECTS_MANAGER);
	final IUserView userView = getRemoteUser(request);
	ServiceParameters serviceParameters = request.getServiceParameters();
	Object[] parametersArray = serviceParameters.parametersArray();
	String costCenter = (String) parametersArray[1];
	Boolean it = false;
	if (parametersArray.length > 1 && parametersArray[2] != null) {
	    it = (Boolean) parametersArray[2];
	}

	final Person person = userView.getPerson();
	final Teacher teacher = person == null ? null : person.getTeacher();
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
	serviceParameters.addParameter("userNumber", userNumber.toString(), serviceParameters.parametersArray().length);

	if (costCenter != null && !costCenter.equals("")) {
	    Role role = Role.getRoleByRoleType(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
	    if (!costCenter.equals(role.getPortalSubApplication())) {
		if (new PersistentProjectUser().getCCNameByCoordinatorAndCC(userNumber, new Integer(costCenter), false) != null) {
		    serviceParameters.addParameter("userNumber", costCenter, serviceParameters.parametersArray().length - 1);
		}
	    }
	    roleToAuthorize.set(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
	} else {
	    if (it) {
		roleToAuthorize.set(RoleType.IT_PROJECTS_MANAGER);
	    }
	    serviceParameters.addParameter("costCenter", "", 1);
	}
	super.execute(request, response);
	request.setServiceParameters(serviceParameters);
    }

    protected RoleType getRoleType() {
	return roleToAuthorize.get();
    }

}