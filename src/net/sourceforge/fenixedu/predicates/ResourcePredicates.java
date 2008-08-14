package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.material.Material;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.resource.Vehicle;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResourcePredicates {

    public static final AccessControlPredicate<Material> checkPermissionsToManageMaterial = new AccessControlPredicate<Material>() {
	public boolean evaluate(Material material) {
	    Person loggedPerson = AccessControl.getPerson();
	    if (!loggedPerson.hasRole(RoleType.RESOURCE_MANAGER)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };

    public static final AccessControlPredicate<Vehicle> checkPermissionsToManageVehicle = new AccessControlPredicate<Vehicle>() {
	public boolean evaluate(Vehicle vehicle) {
	    Person loggedPerson = AccessControl.getPerson();
	    if (!loggedPerson.hasRole(RoleType.RESOURCE_MANAGER)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };
}
