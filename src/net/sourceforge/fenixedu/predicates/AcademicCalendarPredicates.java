package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class AcademicCalendarPredicates {

    public static final AccessControlPredicate<AcademicCalendar> checkPermissionsToManageAcademicCalendar = new AccessControlPredicate<AcademicCalendar>() {
	public boolean evaluate(AcademicCalendar academicCalendar) {
	    if(!AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };
    
    public static final AccessControlPredicate<AcademicCalendarEntry> checkPermissionsToManageAcademicCalendarEntry = new AccessControlPredicate<AcademicCalendarEntry>() {
	public boolean evaluate(AcademicCalendarEntry academicCalendarEntry) {
	    if(!AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };
}
