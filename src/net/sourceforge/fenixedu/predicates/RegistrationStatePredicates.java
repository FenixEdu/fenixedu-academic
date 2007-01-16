/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationStatePredicates {

    public static final AccessControlPredicate<RegistrationState> deletePredicate = new AccessControlPredicate<RegistrationState>() {
	public boolean evaluate(RegistrationState c) {
	    final Person person = AccessControl.getPerson();
	    return person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		    || person.hasRole(RoleType.MANAGER);
	}
    };

}
