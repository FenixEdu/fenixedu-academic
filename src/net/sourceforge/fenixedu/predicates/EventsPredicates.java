package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class EventsPredicates {

    public static final AccessControlPredicate<ResidenceEvent> MANAGER_OR_RESIDENCE_UNIT_EMPLOYEE = new AccessControlPredicate<ResidenceEvent>() {
	public boolean evaluate(ResidenceEvent residenceEvent) {
	    return RolePredicates.MANAGER_PREDICATE.evaluate(residenceEvent)
		    || residenceEvent.getManagementUnit()
			    .equals(AccessControl.getPerson().getEmployee().getCurrentWorkingPlace());
	};
    };

}
