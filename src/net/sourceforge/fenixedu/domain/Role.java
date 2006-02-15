/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public class Role extends Role_Base implements Comparable {

    public static Role getRoleByRoleType(final RoleType roleType) {
        for (final Role role : RootDomainObject.instance.getRoles()) {
            if (role.getRoleType() == roleType) {
                return role;
            }
        }
        return null;
    }

    public Role(final RoleType roleType, final String portalSubApplication, final String page, final String pageNameProperty) {
        setRootDomainObject(RootDomainObject.instance);
        setRoleType(roleType);
        setPortalSubApplication(portalSubApplication);
        setPage(page);
        setPageNameProperty(pageNameProperty);
    }

    public int compareTo(Object o) {
    	return (o instanceof Role) ? compareTo((Role) o) : -1;
    }

    public int compareTo(Role role) {
        return (role != null) ? getRoleType().compareTo(role.getRoleType()) : -1;
    }

}
