package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class RegistrationPredicates {

    public static final AccessControlPredicate<Registration> TRANSIT_TO_BOLONHA = new AccessControlPredicate<Registration>() {
	@Override
	public boolean evaluate(final Registration registration) {
	    return AccessControl.getPerson().hasRole(RoleType.MANAGER);
	};
    };

    public static final AccessControlPredicate<Registration> MANAGE_CONCLUSION_PROCESS = new AccessControlPredicate<Registration>() {

	@Override
	public boolean evaluate(final Registration registration) {
	    if (AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    return AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
		    AcademicOperationType.MANAGE_CONCLUSION).contains(registration.getDegree());
	}
    };

}
