/*
 * Created on 8/Set/2003, 14:37:23
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 8/Set/2003, 14:37:23
 *  
 */
public class ManagerOrSeminariesCoordinatorFilter extends Filtro
{
    public ManagerOrSeminariesCoordinatorFilter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        if (((id != null && id.getRoles() != null
                        && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType1()) && !AuthorizationUtils
                        .containsRole(id.getRoles(), getRoleType2())))
                        || (id == null) || (id.getRoles() == null))
        {
            throw new NotAuthorizedException();
        }
    }

    protected RoleType getRoleType1()
    {
        return RoleType.MANAGER;
    }

    protected RoleType getRoleType2()
    {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}
