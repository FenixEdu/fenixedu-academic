package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class PersistentGroupMembersPredicates {

	public static final AccessControlPredicate<PersistentGroupMembers> checkPermissionsToManagePersistentGroups = new AccessControlPredicate<PersistentGroupMembers>() {
		public boolean evaluate(PersistentGroupMembers persistentGroupMembers) {
			Person person = AccessControl.getPerson();

			return person.hasRole(RoleType.MANAGER)
					|| (person.hasRole(RoleType.RESEARCHER) && person.hasRole(RoleType.WEBSITE_MANAGER));
		}
	};
}
