/*
 * Created on 5/Set/2003, 16:22:14
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Filtro.Seminaries;
import java.util.Collection;
import java.util.Iterator;
import DataBeans.InfoRole;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 5/Set/2003, 16:22:14
 * 
 */
public class CandidaciesAccessFilter extends Filtro
{
	//  the singleton of this class
	public final static CandidaciesAccessFilter filter= new CandidaciesAccessFilter();
	public static CandidaciesAccessFilter getInstance()
	{
		return filter;
	}
	//  the singleton of this class
	public void preFiltragem(IUserView id, IServico servico, Object[] argumentos) throws Exception
	{
		if (((id != null
			&& id.getRoles() != null
			&& !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())))
			|| (id == null)
			|| (id.getRoles() == null))
		{
			throw new NotAuthorizedException();
		}
	}
	private RoleType getRoleType()
	{
		return RoleType.SEMINARIES_COORDINATOR;
	}
}
