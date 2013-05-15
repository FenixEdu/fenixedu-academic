/*
 * Created on 6/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Tânia Pousão
 * 
 */
public abstract class AuthorizationByManyRolesFilter extends Filtro {

    @Override
    public void execute(Object[] parameters) throws Exception {
        IUserView id = AccessControl.getUserView();
        String messageException = hasPrevilege(id, parameters);

        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && messageException != null) || (id == null)
                || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException(messageException);
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    abstract protected String hasPrevilege(IUserView id, Object[] arguments);
}