package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class AddPersonToAccessGroup {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(ResourceAllocationAccessGroupType accessGroupType, String expression, boolean toAdd,
            ResourceAllocationRole role) {
        role.addOrRemovePersonFromAccessGroup(expression, accessGroupType, toAdd);
    }
}