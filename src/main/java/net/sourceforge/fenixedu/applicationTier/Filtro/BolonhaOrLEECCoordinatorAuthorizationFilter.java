package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

public class BolonhaOrLEECCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final BolonhaOrLEECCoordinatorAuthorizationFilter instance = new BolonhaOrLEECCoordinatorAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(String executionDegreeID) throws NotAuthorizedException {
        Person person = Authenticate.getUser().getPerson();

        if (!person.hasRole(getRoleType())) {
            throw new NotAuthorizedException();
        }

        if (!(executionDegreeIsBolonhaOrLEEC(executionDegreeID) || isCoordinatorOfExecutionDegree(person, executionDegreeID))) {
            throw new NotAuthorizedException();
        }
    }

    private boolean executionDegreeIsBolonhaOrLEEC(String executionDegreeID) {
        if (executionDegreeID == null) {
            return false;
        }

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

        if (executionDegree.isBolonhaDegree() && executionDegree.getDegree().getSigla().equals("LEEC-pB")) {
            return true;
        }

        return false;
    }

    private boolean isCoordinatorOfExecutionDegree(Person person, String executionDegreeID) {
        if (executionDegreeID == null) {
            return false;
        }

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);

        List<Coordinator> coordinators = new ArrayList<Coordinator>();
        coordinators.addAll(person.getCoordinators());
        coordinators.retainAll(executionDegree.getCoordinatorsList());

        return !coordinators.isEmpty();
    }
}