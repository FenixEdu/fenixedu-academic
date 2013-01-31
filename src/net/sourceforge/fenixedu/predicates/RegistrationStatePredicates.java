/**
 * 
 */
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationStatePredicates {

	public static final AccessControlPredicate<RegistrationState> deletePredicate =
			new AccessControlPredicate<RegistrationState>() {
				@Override
				public boolean evaluate(RegistrationState c) {
					final Person person = AccessControl.getPerson();
					return AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.MANAGE_REGISTRATIONS)
							.contains(c.getRegistration().getDegree()) || person.hasRole(RoleType.MANAGER);
				}
			};

}
