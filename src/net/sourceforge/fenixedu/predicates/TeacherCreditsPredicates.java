package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class TeacherCreditsPredicates {

    public static final AccessControlPredicate<ExecutionPeriod> checkPermissionsToManageCreditsPeriods = new AccessControlPredicate<ExecutionPeriod>() {
	public boolean evaluate(ExecutionPeriod executionPeriod) {
	    Person loggedPerson = AccessControl.getPerson();
	    if(!loggedPerson.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
		throw new DomainException("error.logged.person.not.authorized.to.make.operation");
	    }
	    return true;
	}
    };   
    
}
