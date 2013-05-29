/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.framework;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public abstract class DomainObjectAuthorizationFilter extends AuthorizationByRoleFilter {

    @Override
    abstract protected RoleType getRoleType();

    public void execute(String externalId) throws NotAuthorizedException {
        try {
            IUserView id = AccessControl.getUserView();

            /*
             * note: if it is neither an Integer nor an InfoObject representing
             * the object to be modified, it is supposed to throw a
             * RuntimeException to be caught and encapsulated in a
             * NotAuthorizedException
             */

            boolean isNew = ((externalId == null) || externalId.equals(Integer.valueOf(0)));

            if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType()))) || (id == null)
                    || (id.getRoleTypes() == null) || ((!isNew) && (!verifyCondition(id, externalId)))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    abstract protected boolean verifyCondition(IUserView id, String objectId);
}