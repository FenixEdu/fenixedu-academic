package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResourceAllocationPredicates {

    public static final AccessControlPredicate<VehicleAllocation> checkPermissionsToManageVehicleAllocations = new AccessControlPredicate<VehicleAllocation>() {
	public boolean evaluate(VehicleAllocation allocation) {
	    Person loggedPerson = AccessControl.getPerson();
	    if (!loggedPerson.hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };

}
