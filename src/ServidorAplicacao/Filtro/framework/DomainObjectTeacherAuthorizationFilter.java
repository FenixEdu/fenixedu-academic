/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.framework;

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
public abstract class DomainObjectTeacherAuthorizationFilter extends AuthorizationByRoleFilter
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

    /**
     * Executes the filtering
     */
    public void preFiltragem(IUserView id, Object[] arguments)
        throws NotAuthorizedException
    {
        try
        {
            boolean isNew = ((arguments[0] == null) || ((Integer) arguments[0]).equals(new Integer(0)));

            if (((id != null
                && id.getRoles() != null
                && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
                || (id == null)
                || (id.getRoles() == null)
                || ((!isNew) && (!domainObjectBelongsToTeacher(id, (Integer) arguments[0]))))
            {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e)
        {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    abstract protected boolean domainObjectBelongsToTeacher(IUserView id, Integer objectId);
}
