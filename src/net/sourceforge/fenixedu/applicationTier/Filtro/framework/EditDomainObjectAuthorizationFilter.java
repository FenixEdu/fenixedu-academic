/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.framework;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public abstract class EditDomainObjectAuthorizationFilter extends AuthorizationByRoleFilter {
    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        try {
            Object[] arguments = request.getServiceParameters().parametersArray();
            IUserView id = (IUserView) request.getRequester();
            Integer idInternal = ((InfoObject) arguments[1]).getIdInternal();
            boolean isNew = (idInternal == null) || idInternal.equals(new Integer(0));

            if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType())))
                    || (id == null)
                    || (id.getRoleTypes() == null)
                    || ((!isNew) && (!verifyCondition(id, (InfoObject) arguments[1])))) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    abstract protected RoleType getRoleType();

    abstract protected boolean verifyCondition(IUserView id, InfoObject infoOject);
}