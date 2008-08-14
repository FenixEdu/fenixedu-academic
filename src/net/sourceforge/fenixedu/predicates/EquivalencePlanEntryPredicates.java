package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class EquivalencePlanEntryPredicates {

    public static final AccessControlPredicate<EquivalencePlanEntry> checkPermissionsToCreate = new AccessControlPredicate<EquivalencePlanEntry>() {
	public boolean evaluate(EquivalencePlanEntry equivalencePlanEntry) {
	    return hasRoleType(RoleType.SCIENTIFIC_COUNCIL) || hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		    || hasRoleType(RoleType.MANAGER);
	}
    };

    private static boolean hasRoleType(final RoleType roleType) {
	return AccessControl.getPerson().hasRole(roleType);
    }

}
