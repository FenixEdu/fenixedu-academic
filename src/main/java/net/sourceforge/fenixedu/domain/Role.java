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
import org.apache.commons.lang.StringUtils;

/**
 * @author jpvl
 */
public class Role extends Role_Base implements Comparable {

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
            if (role != null && role.getRootDomainObject() == RootDomainObject.getInstance() && role.getRoleType() == roleType) {
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

    protected Role() {
        super();
    }

    public Role(final RoleType roleType, final String portalSubApplication, final String page, final String pageNameProperty) {
        this();
        setRootDomainObject(RootDomainObject.getInstance());
        setRoleType(roleType);
        setPortalSubApplication(portalSubApplication);
        setPage(page);
        setPageNameProperty(pageNameProperty);
    }

    @Override
    public void setRoleType(RoleType roleType) {
        if (roleType == null || roleType.equals(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            throw new DomainException("error.Role.empty.role.type");
        }
        super.setRoleType(roleType);
    }

    protected void setRoleTypeWithoutCheckType(RoleType roleType) {
        super.setRoleType(roleType);
    }

    @Override
    public void setPortalSubApplication(String portalSubApplication) {
        if (StringUtils.isEmpty(portalSubApplication)) {
            throw new DomainException("error.Role.empty.portal.sub.application");
        }
        super.setPortalSubApplication(portalSubApplication);
    }

    @Override
    public void setPage(String page) {
        if (StringUtils.isEmpty(page)) {
            throw new DomainException("error.Role.empty.page");
        }
        super.setPage(page);
    }

    @Override
    public void setPageNameProperty(String pageNameProperty) {
        if (StringUtils.isEmpty(pageNameProperty)) {
            throw new DomainException("error.Role.empty.pageNameProperty");
        }
        super.setPageNameProperty(pageNameProperty);
    }

    @Override
    public int compareTo(Object o) {
        return (o instanceof Role) ? compareTo((Role) o) : -1;
    }

    public int compareTo(Role role) {
        return (role != null) ? getRoleType().compareTo(role.getRoleType()) : -1;
    }

    public boolean isResourceAllocationRole() {
        return false;
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
    public boolean hasAnyAssociatedPersons() {
        return !getAssociatedPersonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.RoleOperationLog> getRoleOperationLog() {
        return getRoleOperationLogSet();
    }

    @Deprecated
    public boolean hasAnyRoleOperationLog() {
        return !getRoleOperationLogSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPage() {
        return getPage() != null;
    }

    @Deprecated
    public boolean hasRoleType() {
        return getRoleType() != null;
    }

    @Deprecated
    public boolean hasPortalSubApplication() {
        return getPortalSubApplication() != null;
    }

    @Deprecated
    public boolean hasPageNameProperty() {
        return getPageNameProperty() != null;
    }

}
