/*
 * Created on 8/Set/2003, 14:37:23
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro;
import ServidorAplicacao.IServico;
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
	public final static ManagerOrSeminariesCoordinatorFilter instance= new ManagerOrSeminariesCoordinatorFilter();
	/**
	 * The singleton access method of this class.
	 *
	 * @return Returns the instance of this class responsible for the
	 * authorization access to services.
	 **/
	public static Filtro getInstance()
	{
		return instance;
	}
	public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception
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
    
    protected RoleType getRoleType1() {
        return RoleType.MANAGER;
    }
    
    protected RoleType getRoleType2() {
        return RoleType.SEMINARIES_COORDINATOR;
    }
}
