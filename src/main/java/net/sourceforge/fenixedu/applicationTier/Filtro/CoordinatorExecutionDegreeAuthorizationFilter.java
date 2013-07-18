package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class CoordinatorExecutionDegreeAuthorizationFilter extends Filtro {

    public static final CoordinatorExecutionDegreeAuthorizationFilter instance =
            new CoordinatorExecutionDegreeAuthorizationFilter();

    public void execute(Integer executionDegreeId) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, executionDegreeId)) || (id == null)
                || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedException();
        }
    }

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.RESOURCE_ALLOCATION_MANAGER);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    private boolean hasPrivilege(IUserView id, Integer executionDegreeId) {
        if (id.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            Integer executionDegreeID = executionDegreeId;

            if (executionDegreeID == null) {
                return false;
            }
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeID);
            if (executionDegree == null) {
                return false;
            }
            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            if (coordinator != null) {
                return true;
            }
        }
        return false;
    }

}