/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.framework;

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

    public void preFiltragem(IUserView id, Object[] arguments)
        throws NotAuthorizedException
    {
        try
        {
            if (((id != null
                && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
                || (id == null)
                || (id.getRoles() == null)
                || !domainObjectBelongsToTeacher(id, (InfoObject) arguments[1]))
            {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    abstract protected boolean domainObjectBelongsToTeacher(IUserView id, InfoObject infoOject);
}
