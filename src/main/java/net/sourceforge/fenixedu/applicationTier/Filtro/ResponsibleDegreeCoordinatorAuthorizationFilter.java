/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 */
public class ResponsibleDegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ResponsibleDegreeCoordinatorAuthorizationFilter instance =
            new ResponsibleDegreeCoordinatorAuthorizationFilter();

    public ResponsibleDegreeCoordinatorAuthorizationFilter() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(String executionDegreeId) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        try {
            if ((id == null) || (id.getPerson().getPersonRolesSet() == null) || !id.getPerson().hasRole(getRoleType())
                    || !isResponsibleCoordinatorOfExecutionDegree(id, executionDegreeId)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean isResponsibleCoordinatorOfExecutionDegree(User id, String executionDegreeId) {
        boolean result = false;
        if (executionDegreeId == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();

            ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            result = (coordinator != null) && coordinator.getResponsible().booleanValue();

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}