/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author jpvl
 */
public class Role extends Role_Base implements Comparable<Role> {

    static final protected Comparator<Role> COMPARATOR_BY_ROLE_TYPE = new Comparator<Role>() {
        @Override
        public int compare(Role r1, Role r2) {
            return r1.getRoleType().compareTo(r2.getRoleType());
        }
    };

    public static Role getRoleByRoleType(final RoleType roleType) {
        for (final Role role : Bennu.getInstance().getRolesSet()) {
            if (role.getRoleType() == roleType) {
                return role;
            }
        }
        return null;
    }

    public Role(final RoleType roleType) {
        setRootDomainObject(Bennu.getInstance());
        setRoleType(roleType);
    }

    @Override
    public RoleType getRoleType() {
        return super.getRoleType();
    }

    @Override
    protected void setRoleType(RoleType roleType) {
        if (roleType == null) {
            throw new DomainException("error.Role.empty.role.type");
        }
        super.setRoleType(roleType);
    }

    @Override
    public int compareTo(Role role) {
        return (role != null) ? getRoleType().compareTo(role.getRoleType()) : -1;
    }

    public ArrayList<RoleOperationLog> getRoleOperationLogArrayListOrderedByDate() {
        ArrayList<RoleOperationLog> logs = new ArrayList<>(this.getRoleOperationLogSet());
        Collections.sort(logs, new Comparator<RoleOperationLog>() {
            @Override
            public int compare(RoleOperationLog o1, RoleOperationLog o2) {
                return o1.getLogDate().compareTo(o2.getLogDate());
            }
        });
        return logs;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getAssociatedPersons() {
        return getAssociatedPersonsSet();
    }

}
