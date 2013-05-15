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
import net.sourceforge.fenixedu.injectionCode.AccessControl;
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
     * @see
     * pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk
     * .ServiceRequest, pt.utl.ist.berserk.ServiceResponse)
     */
    @Override
    public void execute(Object[] parameters) throws FilterException, Exception {
        try {
            IUserView id = AccessControl.getUserView();
            Integer idInternal = ((InfoObject) parameters[1]).getIdInternal();
            boolean isNew = (idInternal == null) || idInternal.equals(Integer.valueOf(0));

            if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType()))) || (id == null)
                    || (id.getRoleTypes() == null) || ((!isNew) && (!verifyCondition(id, (InfoObject) parameters[1])))) {
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
    @Override
    abstract protected RoleType getRoleType();

    abstract protected boolean verifyCondition(IUserView id, InfoObject infoOject);
}