package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType;

public class AddPersonToAccessGroup extends Service {
    
    public void run(ResourceAllocationAccessGroupType accessGroupType, String expression, boolean toAdd, ResourceAllocationRole role) {	
	role.addOrRemovePersonFromAccessGroup(expression, accessGroupType, toAdd);		
    }
}
