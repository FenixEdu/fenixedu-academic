package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class AddPersonToAccessGroup {

    @Atomic
    public static void run(ResourceAllocationAccessGroupType accessGroupType, String expression, boolean toAdd,
            ResourceAllocationRole role) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        role.addOrRemovePersonFromAccessGroup(expression, accessGroupType, toAdd);
    }
}