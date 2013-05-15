/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class DegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public DegreeCoordinatorAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Object[] argumentos = request.getServiceParameters().parametersArray();

        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !isCoordinatorOfExecutionDegree(id, argumentos)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean isCoordinatorOfExecutionDegree(IUserView id, Object[] argumentos) {
        boolean result = false;
        if (argumentos == null) {
            return result;
        }
        if (argumentos[0] == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID((Integer) argumentos[0]);
            if (executionDegree == null) {
                return false;
            }
            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            result = coordinator != null;

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}