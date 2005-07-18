/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public class Role extends Role_Base implements Comparable {

    public Role() {
        super();
    }

    public Role(final RoleType roleType, final String portalSubApplication, final String page,
            final String pageNameProperty) {
        super();
        setRoleType(roleType);
        setPortalSubApplication(portalSubApplication);
        setPage(page);
        setPageNameProperty(pageNameProperty);
    }

    public int compareTo(Object o) {
        if (o != null && o instanceof InfoRole) {
            final InfoRole infoRole = (InfoRole) o;
            return getRoleType().compareTo(infoRole.getRoleType());
        }
        return -1;
    }

}
