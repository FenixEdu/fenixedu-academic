/*
 * Created on 6/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Tânia Pousão
 *  
 */
public abstract class AuthorizationByManyRolesFilter extends Filtro {
    /**
     * @return The Needed Roles to Execute The Service
     */
    abstract protected Collection getNeededRoles();

    /**
     * @return The Needed Roles to Execute The Service but with InfoObjects
     */
    protected List getRoleList(List roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        String messageException = hasPrevilege(id, request.getArguments());

        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && messageException != null) || (id == null)
                || (id.getRoles() == null)) {
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