package ServidorAplicacao.Servico;

import java.util.Collection;
import java.util.Set;

import ServidorAplicacao.IUserView;

/**
 * @author jorge
 */
public class UserView implements IUserView {
	private String utilizador;
	private Set _privilegios;
	private Collection roles;

	public UserView(String utilizador, Set privilegios) {
		setUtilizador(utilizador);
		setPrivilegios(privilegios);
	}

	public String getUtilizador() {
		return utilizador;
	}

	public Set getPrivilegios() {
		return _privilegios;
	}

	public void setUtilizador(String utilizador) {
		this.utilizador = utilizador;
	}

	public void setPrivilegios(Set privilegios) {
		_privilegios = privilegios;
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