package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;

public class AddPersonToAccessGroup extends FenixService {

    public void run(ResourceAllocationAccessGroupType accessGroupType, String expression, boolean toAdd,
	    ResourceAllocationRole role) {
	role.addOrRemovePersonFromAccessGroup(expression, accessGroupType, toAdd);
    }
}
