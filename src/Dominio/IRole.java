package Dominio;

import Util.RoleType;

/**
 * 11/Mar/2003
 * 
 * @author jpvl
 */
public interface IRole {
    String getPortalSubApplication();

    String getPageNameProperty();

    String getPage();

    //Collection getRolePersons();
    RoleType getRoleType();

    void setPortalSubApplication(String portalSubApplication);

    void setPageNameProperty(String pageNameProperty);

    void setPage(String page);

    //void setRolePersons(Collection rolePersons);
    void setRoleType(RoleType roleName);
}