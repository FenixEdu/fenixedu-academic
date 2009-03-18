package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class PermissionPredicates {

    public static final AccessControlPredicate<Campus> createPermissionMembersGroup = new AccessControlPredicate<Campus>() {

	@Override
	public boolean evaluate(Campus campus) {
	    Person person = AccessControl.getPerson();
	    Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();

	    return (person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) && person.isAdministrativeOfficeEmployee()
		    && currentWorkingPlace.getActiveUnitCoordinator() == person && currentWorkingPlace.getCampus() == campus)
		    || person.hasRole(RoleType.MANAGER);
	}

    };

    public static final AccessControlPredicate<AdministrativeOfficePermission> managePermissionMembersGroup = new AccessControlPredicate<AdministrativeOfficePermission>() {

	@Override
	public boolean evaluate(AdministrativeOfficePermission c) {
	    Person person = AccessControl.getPerson();

	    return (person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) && person.isAdministrativeOfficeEmployee() && person
		    .getEmployee().isUnitCoordinator())
		    || person.hasRole(RoleType.MANAGER);
	}

    };

}
