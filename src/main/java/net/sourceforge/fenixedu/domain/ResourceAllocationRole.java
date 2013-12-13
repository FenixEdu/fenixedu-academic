package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.predicates.ResourceAllocationRolePredicates;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.Bennu;

public class ResourceAllocationRole extends ResourceAllocationRole_Base {

    private static RoleType ROLE_TYPE = RoleType.RESOURCE_ALLOCATION_MANAGER;

    private ResourceAllocationRole() {
        super();
    }

    public ResourceAllocationRole(final String portalSubApplication, final String page, final String pageNameProperty) {
        this();
        checkIfAlreadyExistsRole();
        setRoleType(ROLE_TYPE);
        setPortalSubApplication(portalSubApplication);
        setPage(page);
        setPageNameProperty(pageNameProperty);
        setRootDomainObject(Bennu.getInstance());
    }

    private void checkIfAlreadyExistsRole() {
        Set<Role> rolesSet = Bennu.getInstance().getRolesSet();
        for (Role role : rolesSet) {
            if (role.isResourceAllocationRole() && !role.equals(this)) {
                throw new DomainException("error.ResourceAllocationRole.already.exists");
            }
        }
    }

    @Override
    public void setRoleType(RoleType roleType) {
        if (roleType == null || !roleType.equals(ROLE_TYPE)) {
            throw new DomainException("error.ResourceAllocationRole.invalid.roleType");
        }
        super.setRoleTypeWithoutCheckType(roleType);
    }

    @Override
    public boolean isResourceAllocationRole() {
        return true;
    }

    public static boolean personIsResourceAllocationSuperUser(Person person) {
        return person.hasRole(RoleType.MANAGER) && person.hasRole(ROLE_TYPE);
    }

    public static boolean personHasPermissionToManageSchedulesAllocation(Person person) {
        ResourceAllocationRole role = (ResourceAllocationRole) getRoleByRoleType(ROLE_TYPE);
        Group accessGroup = role.getSchedulesAccessGroup();
        return personIsResourceAllocationSuperUser(person) || (accessGroup != null && accessGroup.isMember(person));
    }

    public static boolean personHasPermissionToManageMaterialsAllocation(Person person) {
        ResourceAllocationRole role = (ResourceAllocationRole) getRoleByRoleType(ROLE_TYPE);
        Group accessGroup = role.getMaterialsAccessGroup();
        return personIsResourceAllocationSuperUser(person) || (accessGroup != null && accessGroup.isMember(person));
    }

    public static boolean personHasPermissionToManageVehiclesAllocation(Person person) {
        ResourceAllocationRole role = (ResourceAllocationRole) getRoleByRoleType(ROLE_TYPE);
        Group accessGroup = role.getVehiclesAccessGroup();
        return personIsResourceAllocationSuperUser(person) || (accessGroup != null && accessGroup.isMember(person));
    }

    public static boolean personHasPermissionToManageSpacesAllocation(Person person) {
        ResourceAllocationRole role = (ResourceAllocationRole) getRoleByRoleType(ROLE_TYPE);
        Group accessGroup = role.getSpacesAccessGroup();
        return personIsResourceAllocationSuperUser(person) || (accessGroup != null && accessGroup.isMember(person));
    }

    public static void checkIfPersonIsResourceAllocationSuperUser(Person person) {
        if (!personIsResourceAllocationSuperUser(person)) {
            throw new DomainException("error.logged.person.not.authorized.to.make.operation");
        }
    }

    public static void checkIfPersonHasPermissionToManageSpacesAllocation(Person person) {
        if (!personHasPermissionToManageSpacesAllocation(person)) {
            throw new DomainException("error.logged.person.not.authorized.to.make.operation");
        }
    }

    public static void checkIfPersonHasPermissionToManageSchedulesAllocation(Person person) {
        if (!personHasPermissionToManageSchedulesAllocation(person)) {
            throw new DomainException("error.logged.person.not.authorized.to.make.operation");
        }
    }

    public static enum ResourceAllocationAccessGroupType {

        SCHEDULES_ACCESS_GROUP("schedulesAccessGroup"), SPACES_ACCESS_GROUP("spacesAccessGroup"), MATERIALS_ACCESS_GROUP(
                "materialsAccessGroup"), VEHICLES_ACCESS_GROUP("vehiclesAccessGroup");

        private String accessGroupSlotName;

        private ResourceAllocationAccessGroupType(String accessGroupSlotName) {
            this.accessGroupSlotName = accessGroupSlotName;
        }

        public String getName() {
            return name();
        }

        public String getAccessGroupSlotName() {
            return accessGroupSlotName;
        }
    }

    public void addOrRemovePersonFromAccessGroup(String expression, ResourceAllocationAccessGroupType accessGroupType,
            boolean toAdd) {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageAccessGroups);

        if (StringUtils.isEmpty(expression)) {
            throw new DomainException("error.ResourceAllocation.access.groups.management.no.person");
        }

        if (!toAdd) {
            byte[] encodeHex;
            try {
                encodeHex = Hex.decodeHex(expression.toCharArray());
            } catch (DecoderException e) {
                throw new DomainException("error.ResourceAllocation.access.groups.invalid.expression");
            }
            expression = new String(encodeHex);
        }

        Group groupToAddOrRemove = Group.fromString(expression);

        Set<Person> elementsToAddOrRemove = groupToAddOrRemove.getElements();
        Group existentGroup = null, newGroupUnion = null;

        switch (accessGroupType) {

        case MATERIALS_ACCESS_GROUP:

            existentGroup = getMaterialsAccessGroup();
            newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
            setMaterialsAccessGroup(newGroupUnion);
            resourceAllocationRoleManagement(newGroupUnion, toAdd, elementsToAddOrRemove);
            break;

        case SCHEDULES_ACCESS_GROUP:

            existentGroup = getSchedulesAccessGroup();
            newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
            setSchedulesAccessGroup(newGroupUnion);
            resourceAllocationRoleManagement(newGroupUnion, toAdd, elementsToAddOrRemove);
            break;

        case SPACES_ACCESS_GROUP:

            existentGroup = getSpacesAccessGroup();
            newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
            setSpacesAccessGroup(newGroupUnion);
            resourceAllocationRoleManagement(newGroupUnion, toAdd, elementsToAddOrRemove);
            break;

        case VEHICLES_ACCESS_GROUP:

            existentGroup = getVehiclesAccessGroup();
            newGroupUnion = manageGroups(toAdd, groupToAddOrRemove, existentGroup);
            setVehiclesAccessGroup(newGroupUnion);
            resourceAllocationRoleManagement(newGroupUnion, toAdd, elementsToAddOrRemove);
            break;

        default:
            break;
        }
    }

    private Group manageGroups(boolean toAdd, Group groupToAddOrRemove, Group existentGroup) {

        List<IGroup> existentGroups = new ArrayList<IGroup>();
        if (existentGroup != null) {
            if (existentGroup instanceof GroupUnion) {
                existentGroups.addAll(((GroupUnion) existentGroup).getChildren());
            } else {
                existentGroups.add(existentGroup);
            }
        }

        if (toAdd) {
            for (IGroup iGroup : existentGroups) {
                IGroup existentGroup_ = iGroup;
                if (existentGroup_.getElements().containsAll(groupToAddOrRemove.getElements())) {
                    toAdd = false;
                    break;
                }
            }
            if (toAdd) {
                existentGroups.add(groupToAddOrRemove);
            }

        } else {
            for (Iterator<IGroup> iter = existentGroups.iterator(); iter.hasNext();) {
                IGroup existentGroup_ = iter.next();
                if (existentGroup_.getElementsCount() == groupToAddOrRemove.getElementsCount()
                        && existentGroup_.getElements().containsAll(groupToAddOrRemove.getElements())) {
                    iter.remove();
                    existentGroups.remove(existentGroup_);
                }
            }
        }

        return (Group) (existentGroups.isEmpty() ? null : existentGroups.size() == 1 ? existentGroups.iterator().next() : new GroupUnion(
                existentGroups));
    }

    private void resourceAllocationRoleManagement(Group newGroupUnion, boolean toAdd, Set<Person> elementsToAddOrRemove) {
        if (toAdd) {
            for (Person person : newGroupUnion.getElements()) {
                person.addPersonRoleByRoleType(ROLE_TYPE);
            }
        } else {
            for (Person person : elementsToAddOrRemove) {
                if (!personHasPermissionToManageMaterialsAllocation(person)
                        && !personHasPermissionToManageSchedulesAllocation(person)
                        && !personHasPermissionToManageSpacesAllocation(person)
                        && !personHasPermissionToManageVehiclesAllocation(person)) {
                    person.removeRoleByType(ROLE_TYPE);
                }
            }
        }
    }

    @Deprecated
    public boolean hasSpacesAccessGroup() {
        return getSpacesAccessGroup() != null;
    }

    @Deprecated
    public boolean hasVehiclesAccessGroup() {
        return getVehiclesAccessGroup() != null;
    }

    @Deprecated
    public boolean hasSchedulesAccessGroup() {
        return getSchedulesAccessGroup() != null;
    }

    @Deprecated
    public boolean hasMaterialsAccessGroup() {
        return getMaterialsAccessGroup() != null;
    }

}
