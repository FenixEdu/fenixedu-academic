/*
 * Created on 12/Mar/2003 by jpvl
 *
 */
package DataBeans;

import Util.RoleType;

/**
 * @author jpvl
 */
public class InfoRole{
	private String portalAction;
	private String portalActionNameProperty;
	private RoleType roleType;

	public InfoRole(){
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

	/**
	 * Sets the roleType.
	 * @param roleType The roleType to set
	 */
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

}
