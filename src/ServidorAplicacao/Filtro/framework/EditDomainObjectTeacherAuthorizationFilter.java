/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.framework;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoObject;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationByRoleFilter;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public abstract class EditDomainObjectTeacherAuthorizationFilter extends AuthorizationByRoleFilter
{
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType()
    {
        return RoleType.TEACHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *          pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception
    {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try
        {
            Integer idInternal = ((InfoObject) arguments[1]).getIdInternal();
            boolean isNew = (idInternal == null) || idInternal.equals(new Integer(0));

            if (((id != null && id.getRoles() != null && !AuthorizationUtils.containsRole(
                            id.getRoles(), getRoleType())))
                            || (id == null)
                            || (id.getRoles() == null)
                            || ((!isNew) && (!domainObjectBelongsToTeacher(id, (InfoObject) arguments[1]))))
            {
                throw new NotAuthorizedException();
            }
        }
        catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    abstract protected boolean domainObjectBelongsToTeacher(IUserView id, InfoObject infoOject);
}
