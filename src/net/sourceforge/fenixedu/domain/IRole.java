package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.RoleType;

/**
 * 11/Mar/2003
 * 
 * @author jpvl
 */
public interface IRole extends IDomainObject {
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