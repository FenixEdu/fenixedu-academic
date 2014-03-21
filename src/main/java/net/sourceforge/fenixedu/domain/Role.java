/*
 * Created on 11/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
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
            if (role != null && role.getRootDomainObject() == Bennu.getInstance() && role.getRoleType() == roleType) {
                return role;
            } else {
                roleMap.remove(roleType);
            }
        }
        // *** end of hack

        for (final Role role : Bennu.getInstance().getRolesSet()) {
            // Temporary solution until DML provides indexed relations.
            roleMap.put(role.getRoleType(), new SoftReference<Role>(role));
            // *** end of hack
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
        return orderRoleOperationLogSetByValue(this.getRoleOperationLogSet(), "logDate");
    }

    private ArrayList<RoleOperationLog> orderRoleOperationLogSetByValue(Set<RoleOperationLog> roleOperationLogSet, String value) {
        ArrayList<RoleOperationLog> roleOperationLogList = new ArrayList<RoleOperationLog>(roleOperationLogSet);
        Collections.sort(roleOperationLogList, new ReverseComparator(new BeanComparator(value)));
        return roleOperationLogList;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getAssociatedPersons() {
        return getAssociatedPersonsSet();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.RoleOperationLog> getRoleOperationLog() {
        return getRoleOperationLogSet();
    }

    @Deprecated
    public boolean hasAnyRoleOperationLog() {
        return !getRoleOperationLogSet().isEmpty();
    }

}
