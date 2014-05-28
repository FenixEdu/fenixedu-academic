/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
