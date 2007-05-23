package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class PersistentGroupMembersPredicates {

    public static final AccessControlPredicate<PersistentGroupMembers> checkPermissionsToManagePersistentGroups = new AccessControlPredicate<PersistentGroupMembers>() {
	public boolean evaluate(PersistentGroupMembers persistentGroupMembers) {	    
	    return AccessControl.getPerson().hasRole(RoleType.MANAGER);
	}
    };    
}
