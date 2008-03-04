package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class AcademicCalendarPredicates {
    
    public static final AccessControlPredicate<AcademicCalendarEntry> checkPermissionsToManageAcademicCalendarEntry = new AccessControlPredicate<AcademicCalendarEntry>() {
	public boolean evaluate(AcademicCalendarEntry academicCalendarEntry) {
	    Person person = AccessControl.getPerson();
	    if(!person.hasRole(RoleType.MANAGER) && !person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };
}
