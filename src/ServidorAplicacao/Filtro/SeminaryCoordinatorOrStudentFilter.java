/*
 * Created on 8/Set/2003, 14:55:43
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 8/Set/2003, 14:55:43
 *  
 */
public class SeminaryCoordinatorOrStudentFilter extends Filtro
{

    public final static SeminaryCoordinatorOrStudentFilter instance =
        new SeminaryCoordinatorOrStudentFilter();
    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    public void preFiltragem(IUserView id, Object[] argumentos) throws Exception
    {
        if (((id != null
            && id.getRoles() != null
            && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType1())
            && !AuthorizationUtils.containsRole(id.getRoles(), getRoleType2())))
            || (id == null)
            || (id.getRoles() == null))
        {
            throw new NotAuthorizedException();
        }
    }

    protected RoleType getRoleType1()
    {
        return RoleType.STUDENT;
    }

    protected RoleType getRoleType2()
    {
        return RoleType.SEMINARIES_COORDINATOR;
    }

}
