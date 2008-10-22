package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class RolePredicates {

    public static final AccessControlPredicate<Object> MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return hasRole(RoleType.MANAGER);
	};
    };

    public static final AccessControlPredicate<Object> ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	};
    };

    public static final AccessControlPredicate<Object> GRANT_OWNER_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return hasRole(RoleType.GRANT_OWNER_MANAGER);
	};
    };

    public static final AccessControlPredicate<Object> MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return MANAGER_PREDICATE.evaluate(domainObject) || ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE.evaluate(domainObject);
	};
    };

    public static final AccessControlPredicate<Object> SCIENTIFIC_COUNCIL_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return hasRole(RoleType.SCIENTIFIC_COUNCIL);
	};
    };

    public static final AccessControlPredicate<Object> TEACHER_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return hasRole(RoleType.TEACHER);
	};
    };

    public static final AccessControlPredicate<Object> MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_OR_GRANT_OWNER_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
	public boolean evaluate(Object domainObject) {
	    return MANAGER_PREDICATE.evaluate(domainObject) || ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE.evaluate(domainObject)
		    || GRANT_OWNER_MANAGER_PREDICATE.evaluate(domainObject);
	};
    };

    private static boolean hasRole(final RoleType roleType) {
	return AccessControl.getPerson().hasRole(roleType);
    }
}
