/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public class Role extends Role_Base {

    private RoleType roleType;

    public boolean equals(Object obj) {
        return ((obj instanceof IRole) && (((IRole) obj).getRoleType().equals(getRoleType())));
    }

    public int hashCode() {
        return 0;
    }

    /**
     * @return RoleType
     */
    public RoleType getRoleType() {
        return roleType;
    }

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