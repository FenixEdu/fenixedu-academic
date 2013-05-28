/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

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

    public void execute(Integer executionDegreeId) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !isCoordinatorOfExecutionDegree(id, executionDegreeId)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean isCoordinatorOfExecutionDegree(IUserView id, Integer executionDegreeId) {
        boolean result = false;
        if (executionDegreeId == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();
            ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeId);
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