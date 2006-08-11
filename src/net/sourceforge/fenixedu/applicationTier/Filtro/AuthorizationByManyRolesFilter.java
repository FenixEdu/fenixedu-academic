/*
 * Created on 6/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Tânia Pousão
 *  
 */
public abstract class AuthorizationByManyRolesFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasPrevilege(id, request.getServiceParameters().parametersArray());

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