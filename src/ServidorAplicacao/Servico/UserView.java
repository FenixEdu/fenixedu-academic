package ServidorAplicacao.Servico;

import java.util.Collection;

import ServidorAplicacao.IUserView;

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