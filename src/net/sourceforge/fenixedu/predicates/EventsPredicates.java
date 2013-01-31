package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class EventsPredicates {

	public static final AccessControlPredicate<ResidenceEvent> MANAGER_OR_RESIDENCE_UNIT_EMPLOYEE =
			new AccessControlPredicate<ResidenceEvent>() {
				@Override
				public boolean evaluate(ResidenceEvent residenceEvent) {
					Employee employee = AccessControl.getPerson().getEmployee();
					return RolePredicates.MANAGER_PREDICATE.evaluate(residenceEvent)
							|| employee.getPerson().hasRole(RoleType.RESIDENCE_MANAGER)
							|| isManagementUnitOrParent(residenceEvent.getManagementUnit(), employee.getCurrentWorkingPlace());
				}

				private boolean isManagementUnitOrParent(Unit managementUnit, Unit currentWorkingPlace) {
					if (managementUnit == currentWorkingPlace) {
						return true;
					}
					for (Unit unit : currentWorkingPlace.getAllSubUnits()) {
						if (managementUnit == unit) {
							return true;
						}
					}
					return false;
				}
			};

}
