/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package Dominio;

import java.util.Collection;

import Util.RoleType;

/**
 * @author jpvl
 */
public class Role extends DomainObject implements IRole{
	private String portalAction;
	private String portalActionNameProperty;
	private Collection rolePersons;
	private RoleType roleType;

	public Role() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return (
			(obj instanceof Role)
				&& (((Role) obj).getRoleType().equals(getRoleType())));
	}

	/**
	 * @return String
	 */
	public String getPortalAction() {
		return portalAction;
	}

	/**
	 * @return String
	 */
	public String getPortalActionNameProperty() {
		return portalActionNameProperty;
	}

	/* (non-Javadoc)
	 * @see Dominio.IRole#getRolePersons()
	 */
	public Collection getRolePersons() {
		return this.rolePersons;
	}


	/**
	 * @return RoleType
	 */
	public RoleType getRoleType() {
		return roleType;
	}


	/**
	 * Sets the portalAction.
	 * @param portalAction The portalAction to set
	 */
	public void setPortalAction(String portalAction) {
		this.portalAction = portalAction;
	}

	/**
	 * Sets the portalActionNameProperty.
	 * @param portalActionNameProperty The portalActionNameProperty to set
	 */
	public void setPortalActionNameProperty(String portalActionNameProperty) {
		this.portalActionNameProperty = portalActionNameProperty;
	}

	/* (non-Javadoc)
	 * @see Dominio.IRole#setRolePersons(java.util.List)
	 */
	public void setRolePersons(Collection rolePersons) {
		this.rolePersons = rolePersons;
		
	}

	/**
	 * Sets the roleType.
	 * @param roleType The roleType to set
	 */
	public void setRoleType(RoleType roleName) {
		this.roleType = roleName;
	}

}
