/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.framework;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public abstract class DomainObjectAuthorizationFilter extends AuthorizationByRoleFilter {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    abstract protected RoleType getRoleType();

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        try {
            Object[] arguments = getServiceCallArguments(request);
            IUserView id = getRemoteUser(request);
            Integer idInternal = (Integer) arguments[0];
            boolean isNew = ((idInternal == null) || idInternal.equals(new Integer(0)));

            if (((id != null && id.getRoles() != null && !AuthorizationUtils.containsRole(id.getRoles(),
                    getRoleType())))
                    || (id == null)
                    || (id.getRoles() == null)
                    || ((!isNew) && (!verifyCondition(id, (Integer) arguments[0])))) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException(e.getMessage());
        }
    }

    abstract protected boolean verifyCondition(IUserView id, Integer objectId);
}