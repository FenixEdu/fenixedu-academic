/*
 * Created on 6/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
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
     * @return The Needed Roles to Execute The Service but with InfoObjects
     */
    protected List<RoleType> getRoleList(Collection<Role> roles) {
        List<RoleType> result = new ArrayList<RoleType>();
        for (final Role role : roles) {
        	result.add(role.getRoleType());
        }
        return result;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    abstract protected String hasPrevilege(IUserView id, Object[] arguments);
}