package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class BolonhaOrLEECCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
	return RoleType.COORDINATOR;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	Person person = getRemoteUser(request).getPerson();

	if (!person.hasRole(getRoleType()))
	    throw new NotAuthorizedFilterException();

	Object[] args = getServiceCallArguments(request);
	Integer executionDegreeID = (Integer) args[0];

	if (!(executionDegreeIsBolonhaOrLEEC(executionDegreeID) || isCoordinatorOfExecutionDegree(person, executionDegreeID)))
	    throw new NotAuthorizedFilterException();
    }

    private boolean executionDegreeIsBolonhaOrLEEC(Integer executionDegreeID) {
	if (executionDegreeID == null)
	    return false;

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);

	if (executionDegree.isBolonhaDegree() && executionDegree.getDegree().getSigla().equals("LEEC-pB"))
	    return true;

	return false;
    }

    private boolean isCoordinatorOfExecutionDegree(Person person, Integer executionDegreeID) {
	if (executionDegreeID == null)
	    return false;

	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);

	List<Coordinator> coordinators = new ArrayList<Coordinator>();
	coordinators.addAll(person.getCoordinators());
	coordinators.retainAll(executionDegree.getCoordinatorsList());

	return !coordinators.isEmpty();
    }
}