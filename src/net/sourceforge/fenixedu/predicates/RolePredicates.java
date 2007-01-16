package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class RolePredicates {

    public static final AccessControlPredicate<DomainObject> MANAGER_PREDICATE = new AccessControlPredicate<DomainObject>() {
	public boolean evaluate(DomainObject domainObject) {
	    return hasRole(RoleType.MANAGER);
	};
    };
    
    private static boolean hasRole(final RoleType roleType) {
	return AccessControl.getPerson().hasRole(roleType);
    }
}
