package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class CreditsPredicates {

    static public final AccessControlPredicate<Credits> DELETE = new AccessControlPredicate<Credits>() {

	public boolean evaluate(final Credits credits) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (!person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
		return false;
	    }

	    final AdministrativeOfficePermission permission = getUpdateRegistrationAfterConclusionProcessPermission(person);

	    if (permission != null) {
		for (final Dismissal dismissal : credits.getDismissalsSet()) {
		    if (permission.isAppliable(dismissal.getParentCycleCurriculumGroup()) && !permission.isMember(person)) {
			return false;
		    }
		}
	    }

	    return true;
	}
    };

    static private AdministrativeOfficePermission getUpdateRegistrationAfterConclusionProcessPermission(final Person person) {
	return person.getEmployeeAdministrativeOffice().getPermission(PermissionType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
		person.getEmployeeCampus());
    }

}
