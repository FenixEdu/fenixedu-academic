package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * 
 */
public class CoordinatorExecutionDegreeAuthorizationFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, argumentos))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.TIME_TABLE_MANAGER);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
        if (id.hasRoleType(RoleType.TIME_TABLE_MANAGER)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            Integer executionDegreeID = null;
            if (arguments[1] instanceof InfoExecutionDegree) {
                executionDegreeID = ((InfoExecutionDegree) arguments[1]).getIdInternal();
            } else if (arguments[0] instanceof Integer) {
                executionDegreeID = (Integer) arguments[0];
            }

            if (executionDegreeID == null) {
                return false;
            }
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = rootDomainObject
                    .readExecutionDegreeByOID(executionDegreeID);
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