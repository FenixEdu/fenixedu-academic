/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public class Role extends Role_Base implements Comparable {

    /**
     * This map is a temporary solution until DML provides indexed relations.
     * 
     */
    private static final Map<RoleType, SoftReference<Role>> roleMap = new HashMap<RoleType, SoftReference<Role>>();

    public static Role getRoleByRoleType(final RoleType roleType) {
        // Temporary solution until DML provides indexed relations.
        final SoftReference<Role> roleReference = roleMap.get(roleType);
        if (roleReference != null) {
            final Role role = roleReference.get();
            if (role != null && role.getRootDomainObject() == RootDomainObject.getInstance()
                    && role.getRoleType() == roleType) {
                return role;
            } else {
                roleMap.remove(roleType);
            }
        }
        // *** end of hack

        for (final Role role : RootDomainObject.getInstance().getRoles()) {
            // Temporary solution until DML provides indexed relations.
            roleMap.put(role.getRoleType(), new SoftReference<Role>(role));
            // *** end of hack
            if (role.getRoleType() == roleType) {
                return role;
            }
        }
        return null;
    }

    public Role(final RoleType roleType, final String portalSubApplication, final String page, final String pageNameProperty) {
    	super();
        setRootDomainObject(RootDomainObject.getInstance());
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
