package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class PermissionPredicates {

    public static final AccessControlPredicate<AdministrativeOffice> CREATE_PERMISSION_MEMBERS_GROUP = new AccessControlPredicate<AdministrativeOffice>() {

	@Override
	public boolean evaluate(final AdministrativeOffice administrativeOffice) {
	    final Person person = AccessControl.getPerson();
	    return person.hasRole(RoleType.MANAGER)
		    || (person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) && person.getEmployee().isUnitCoordinator() && person
			    .getEmployeeAdministrativeOffice().equals(administrativeOffice));
	}
    };

    public static final AccessControlPredicate<AdministrativeOfficePermission> MANAGE_PERMISSION_MEMBERS_GROUP = new AccessControlPredicate<AdministrativeOfficePermission>() {

	@Override
	public boolean evaluate(AdministrativeOfficePermission c) {
	    final Person person = AccessControl.getPerson();

	    return person.hasRole(RoleType.MANAGER)
		    || (person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) && person.getEmployee().isUnitCoordinator());
	}
    };

}
