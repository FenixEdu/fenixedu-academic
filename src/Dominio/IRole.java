package Dominio;
import java.util.Collection;

import Util.RoleType;
/**
 *   11/Mar/2003
 *   @author     jpvl
 */
public interface IRole {
	String getPortalAction();
	String getPortalActionNameProperty();
	Collection getRolePersons();
	RoleType getRoleType();
	void setPortalAction(String portalAction);
	void setPortalActionNameProperty(String portalNameProperty);
	void setRolePersons(Collection rolePersons);
	void setRoleType(RoleType roleName);
}