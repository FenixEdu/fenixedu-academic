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
public class ResponsibleDegreeCoordinatorAuthorizationFilter extends AuthorizationByRoleFilter {

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist
     * .berserk.ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Object[] arguments = request.getServiceParameters().parametersArray();

        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !isResponsibleCoordinatorOfExecutionDegree(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean isResponsibleCoordinatorOfExecutionDegree(IUserView id, Object[] argumentos) {
        boolean result = false;
        if (argumentos == null) {
            return result;
        }
        try {
            final Person person = id.getPerson();

            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID((Integer) argumentos[0]);
            Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

            result = (coordinator != null) && coordinator.getResponsible().booleanValue();

        } catch (Exception e) {
            return false;
        }

        return result;
    }

}