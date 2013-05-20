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

/**
 * @author João Mota
 * 
 */
public class DegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final DegreeCoordinatorAuthorizationFilter instance = new DegreeCoordinatorAuthorizationFilter();

    public DegreeCoordinatorAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(Integer executionDegreeId) throws Exception {
        IUserView id = AccessControl.getUserView();
        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !isCoordinatorOfExecutionDegree(id, executionDegreeId)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean isCoordinatorOfExecutionDegree(IUserView id, Integer executionDegreeId) {
        boolean result = false;
        if (executionDegreeId == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
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