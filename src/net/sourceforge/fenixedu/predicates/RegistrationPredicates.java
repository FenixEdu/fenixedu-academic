package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class RegistrationPredicates {

    public static final AccessControlPredicate<Registration> transitToBolonha = new AccessControlPredicate<Registration>() {
	public boolean evaluate(final Registration registration) {
	    return AccessControl.getPerson().hasRole(RoleType.MANAGER);
	};
    };

    public static final AccessControlPredicate<Registration> updateRegistration = new AccessControlPredicate<Registration>() {

	@Override
	public boolean evaluate(Registration registration) {
	    if (AccessControl.getPerson().hasRole(RoleType.MANAGER) || !registration.hasConcluded()) {
	    }
	    return true;

	    // AdministrativeOfficePermission permission =
	    // getPermissionByType(AccessControl.getPerson(),
	    // PermissionType.UPDATE_REGISTRATION_WITH_CONCLUSION);
	    //
	    // return permission != null
	    // && getPermissionByType(AccessControl.getPerson(),
	    // PermissionType.UPDATE_REGISTRATION_WITH_CONCLUSION)
	    // .getPermissionMembersGroup().isMember(AccessControl.getPerson());
	}
    };

    public static final AccessControlPredicate<Registration> MANAGE_CONCLUSION_PROCESS = new AccessControlPredicate<Registration>() {

	@Override
	public boolean evaluate(final Registration registration) {
	    if (AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    final AdministrativeOfficePermission permission = getPermissionByType(PermissionType.MANAGE_CONCLUSION);
	    if (permission == null || !permission.isAppliable(registration)) {
		return true;
	    }

	    return permission.isMember(AccessControl.getPerson());
	}
    };

    public static final AccessControlPredicate<Registration> EDIT_MISSING_CANDIDACY_INFORMATION = new AccessControlPredicate<Registration>() {
	public boolean evaluate(final Registration registration) {
	    if (AccessControl.getPerson().hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
		return true;
	    }

	    if (AccessControl.getPerson().hasStudent()) {
		return registration.getStudent() == AccessControl.getPerson().getStudent();
	    }

	    return false;

	};
    };

    public static final AccessControlPredicate<Registration> EDIT_CANDIDACY_INFORMATION = new AccessControlPredicate<Registration>() {
	public boolean evaluate(final Registration registration) {
	    if (AccessControl.getPerson().hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
		return true;
	    }

	    return false;

	};
    };

    static private final AdministrativeOfficePermission getPermissionByType(final PermissionType type) {
	final Person person = AccessControl.getPerson();
	return person.getEmployeeAdministrativeOffice().getPermission(type, person.getEmployeeCampus());
    }

}
