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
import ServidorAplicacao.NotAuthorizedException;
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
		RoleType roleType = getRoleType();
		if (((id != null
			&& id.getRoles() != null
			&& !containsRole(id.getRoles())))
			|| (id == null)
			|| (id.getRoles() == null)) {
			throw new NotAuthorizedException();
		}

	}
	/**
	 * @param collection
	 * @return boolean
	 */
	private boolean containsRole(Collection roles) {
		Iterator rolesIterator = roles.iterator();
		while (rolesIterator.hasNext()) {
			InfoRole infoRole = (InfoRole) rolesIterator.next();
			if (infoRole.getRoleType().equals(getRoleType()))
				return true;	
		}
		return false;
	}
	/**
	 * This method returns the role that we want to authorize.
	 * @return RoleType
	 */
	abstract protected RoleType getRoleType();

}
