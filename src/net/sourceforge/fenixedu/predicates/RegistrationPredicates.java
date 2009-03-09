package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
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
//	    if (AccessControl.getPerson().hasRole(RoleType.MANAGER) || !registration.hasConcluded()) {
//		return true;
//	    }
//
//	    return getPermissionByType(AccessControl.getPerson(), PermissionType.UPDATE_REGISTRATION_WITH_CONCLUSION)
//		    .getPermissionMembersGroup().isMember(AccessControl.getPerson());
	    
	    return true;
	}
    };

    private static final AdministrativeOfficePermission getPermissionByType(Person person, PermissionType type) {
	Campus campus = person.getEmployee().getCurrentWorkingPlace().getCampus();
	return campus.getAdministrativeOfficePermissionByType(type);
    }
}
