package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;

public class AddPersonToAccessGroup extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(ResourceAllocationAccessGroupType accessGroupType, String expression, boolean toAdd,
	    ResourceAllocationRole role) {
	role.addOrRemovePersonFromAccessGroup(expression, accessGroupType, toAdd);
    }
}