package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class BolonhaOrLEECCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final BolonhaOrLEECCoordinatorAuthorizationFilter instance = new BolonhaOrLEECCoordinatorAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(Integer executionDegreeID) throws NotAuthorizedException {
        Person person = AccessControl.getUserView().getPerson();

        if (!person.hasRole(getRoleType())) {
            throw new NotAuthorizedException();
        }

        if (!(executionDegreeIsBolonhaOrLEEC(executionDegreeID) || isCoordinatorOfExecutionDegree(person, executionDegreeID))) {
            throw new NotAuthorizedException();
        }
    }

    private boolean executionDegreeIsBolonhaOrLEEC(Integer executionDegreeID) {
        if (executionDegreeID == null) {
            return false;
        }

        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeID);

        if (executionDegree.isBolonhaDegree() && executionDegree.getDegree().getSigla().equals("LEEC-pB")) {
            return true;
        }

        return false;
    }

    private boolean isCoordinatorOfExecutionDegree(Person person, Integer executionDegreeID) {
        if (executionDegreeID == null) {
            return false;
        }

        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeID);

        List<Coordinator> coordinators = new ArrayList<Coordinator>();
        coordinators.addAll(person.getCoordinators());
        coordinators.retainAll(executionDegree.getCoordinatorsList());

        return !coordinators.isEmpty();
    }
}