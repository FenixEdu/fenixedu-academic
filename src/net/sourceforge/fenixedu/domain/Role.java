/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public class Role extends DomainObject implements IRole {
    private String portalSubApplication;

    private String page;

    private String pageNameProperty;

    private RoleType roleType;

    public Role() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return ((obj instanceof IRole) && (((IRole) obj).getRoleType().equals(getRoleType())));
    }

    public int hashCode() {
        return 0;
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
     * @return Collection
     */
    //	public Collection getRolePersons() {
    //		return rolePersons;
    //	}
    /**
     * @return RoleType
     */
    public RoleType getRoleType() {
        return roleType;
    }

    /**
     * Sets the page.
     * 
     * @param page
     *            The page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Sets the pageNameProperty.
     * 
     * @param pageNameProperty
     *            The pageNameProperty to set
     */
    public void setPageNameProperty(String pageNameProperty) {
        this.pageNameProperty = pageNameProperty;
    }

    /**
     * Sets the portalSubApplication.
     * 
     * @param portalSubApplication
     *            The portalSubApplication to set
     */
    public void setPortalSubApplication(String portalSubApplication) {
        this.portalSubApplication = portalSubApplication;
    }

    /**
     * Sets the rolePersons.
     * 
     * @param rolePersons
     *            The rolePersons to set
     */
    //	public void setRolePersons(Collection rolePersons) {
    //		this.rolePersons = rolePersons;
    //	}
    /**
     * Sets the roleType.
     * 
     * @param roleType
     *            The roleType to set
     */
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

}