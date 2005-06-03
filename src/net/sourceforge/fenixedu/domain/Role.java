/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.InfoRole;

/**
 * @author jpvl
 */
public class Role extends Role_Base implements Comparable {

    public boolean equals(Object obj) {
        return ((obj instanceof IRole) && (((IRole) obj).getRoleType().equals(getRoleType())));
    }

    public int hashCode() {
        return 0;
    }

    public int compareTo(Object o) {
        if (o != null && o instanceof InfoRole) {
            final InfoRole infoRole = (InfoRole) o;
            return getRoleType().compareTo(infoRole.getRoleType());
        }
        return 0;
    }

}
