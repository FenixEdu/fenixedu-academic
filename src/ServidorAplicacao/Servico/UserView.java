package ServidorAplicacao.Servico;

import java.util.Collection;
import java.util.Iterator;

import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import Util.RoleType;

/**
 * @author jorge
 */
public class UserView implements IUserView {
	private String utilizador;
	private Collection roles;

	public UserView(String utilizador, Collection roles) {
		setUtilizador(utilizador);
		this.roles = roles;
	}


	public boolean hasRoleType(RoleType roleType){
		Iterator iterator = this.roles.iterator();
		while (iterator.hasNext()) {
			if (((InfoRole) iterator.next()).getRoleType().equals(roleType))
				return true;
		}
		return false;
	}

	public String getUtilizador() {
		return utilizador;
	}


	public void setUtilizador(String utilizador) {
		this.utilizador = utilizador;
	}


	/**
	 * @return Collection
	 */
	public Collection getRoles() {
		return roles;
	}

	/**
	 * Sets the roles.
	 * @param roles The roles to set
	 */
	public void setRoles(Collection roles) {
		this.roles = roles;
	}

}