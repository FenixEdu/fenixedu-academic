/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.framework;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

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
            User id = Authenticate.getUser();

            /*
             * note: if it is neither an Integer nor an InfoObject representing
             * the object to be modified, it is supposed to throw a
             * RuntimeException to be caught and encapsulated in a
             * NotAuthorizedException
             */

            boolean isNew = externalId == null;

            if (((id != null && id.getPerson().getPersonRolesSet() != null && !id.getPerson().hasRole(getRoleType())))
                    || (id == null) || (id.getPerson().getPersonRolesSet() == null)
                    || ((!isNew) && (!verifyCondition(id, externalId)))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    abstract protected boolean verifyCondition(User id, String objectId);
}