/*
 * Created on 12/Mar/2003 by jpvl
 *
 */
package DataBeans;

import Util.RoleType;

/**
 * @author jpvl
 */
public class InfoRole extends InfoObject {
	private String portalSubApplication;
	private String page;
	private String pageNameProperty;
	private RoleType roleType;

	public InfoRole(){
	}
	
	public InfoRole(RoleType roleType){
		setRoleType(roleType);	
	}
	/**
	 * @return String
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @return String
	 */
	public String getPageNameProperty() {
		return pageNameProperty;
	}

	/**
	 * @return String
	 */
	public String getPortalSubApplication() {
		return portalSubApplication;
	}

	/**
	 * @return RoleType
	 */
	public RoleType getRoleType() {
		return roleType;
	}

	/**
	 * Sets the page.
	 * @param page The page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * Sets the pageNameProperty.
	 * @param pageNameProperty The pageNameProperty to set
	 */
	public void setPageNameProperty(String pageNameProperty) {
		this.pageNameProperty = pageNameProperty;
	}

	/**
	 * Sets the portalSubApplication.
	 * @param portalSubApplication The portalSubApplication to set
	 */
	public void setPortalSubApplication(String portalSubApplication) {
		this.portalSubApplication = portalSubApplication;
	}

	/**
	 * Sets the roleType.
	 * @param roleType The roleType to set
	 */
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	public String toString(){
		String result = "Info Role :\n";
		result += "\n  - Page : " + page;
		result += "\n  - PageNameProperty : " + pageNameProperty;
		result += "\n  - PortalSubApplication : " + portalSubApplication;
		result += "\n  - RoleType : " + roleType;
	
		return result;		
	}
	
	public boolean equals(Object o) {
			return
			((o instanceof InfoRole) &&
		
			this.roleType.equals(((InfoRole)o).getRoleType()));
	}
	
	public int hashCode(){
		return this.roleType.getValue(); 
	}
}