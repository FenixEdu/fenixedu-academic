package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class RegistrationPredicates {

    public static final AccessControlPredicate<Registration> transitToBolonha = new AccessControlPredicate<Registration>() {
	public boolean evaluate(final Registration registration) {
	    return AccessControl.getPerson().hasRole(RoleType.STUDENT);
	};
    };

}
