/*
 * Created on 13/Mar/2003 by jpvl
 *
 */
package ServidorAplicacao.Filtro;

import java.util.Collection;
import java.util.Iterator;

import DataBeans.InfoRole;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import Util.RoleType;

/**
 * @author jpvl
 */
public abstract class AuthorizationByRoleFilter extends Filtro {

	/**
	 * @see ServidorAplicacao.Filtro.Filtro#preFiltragem(IUserView, IServico, Object[])
	 * @throws ServidorAplicacao.NotAuthorizedException if the user doesn't contains role returned by getRoleType() function
	 */
	public void preFiltragem(
		IUserView id,
		IServico servico,
		Object[] argumentos)
		throws Exception {
			Collection roles = id.getRoles();
			Iterator iter = roles.iterator();
			while (iter.hasNext()) {
				InfoRole role = (InfoRole) iter.next();
			}

		if (((id != null
			&& id.getRoles() != null
			&& !AuthorizationUtils.containsRole(id.getRoles(),getRoleType())))
			|| (id == null)
			|| (id.getRoles() == null)) {
			throw new NotAuthorizedException();
		}

	}
	/**
	 * This method returns the role that we want to authorize.
	 * @return RoleType
	 */
	abstract protected RoleType getRoleType();

}
