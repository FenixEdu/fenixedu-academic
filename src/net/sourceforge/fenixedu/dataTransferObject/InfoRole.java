/*
 * Created on 12/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public class InfoRole extends InfoObject implements Comparable {

    private final Role role;

    public InfoRole(final Role role) {
        this.role = role;
    }

    public String getPage() {
        return role.getPage();
    }

    public String getPageNameProperty() {
        return role.getPageNameProperty();
    }

    public String getPortalSubApplication() {
        return role.getPortalSubApplication();
    }

    public RoleType getRoleType() {
        return role.getRoleType();
    }

    public String toString() {
        return role.toString();
    }

    public boolean equals(final Object o) {
        return o instanceof InfoRole && this.getRoleType() == ((InfoRole) o).getRoleType();
    }

    public int hashCode() {
        return this.getRoleType().hashCode();
    }

    public int compareTo(final Object o) {
        if (o != null && o instanceof InfoRole) {
            final InfoRole infoRole = (InfoRole) o;
            return getRoleType().compareTo(infoRole.getRoleType());
        }
        return 0;
    }

    public static InfoRole newInfoFromDomain(final Role role) {
        return new InfoRole(role);
    }

}