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
package org.fenixedu.academic.domain.accessControl;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.predicate.AccessControl;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

@GroupOperator("departmentPresident")
public class DepartmentPresidentStrategy extends FenixGroupStrategy {

    private static final long serialVersionUID = -3153992434314606564L;

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getDepartmentsSet().stream().map(d -> getCurrentDepartmentPresident(d).getUser())
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getEmployee() != null
                && user.getPerson().getEmployee().getCurrentDepartmentWorkingPlace() != null
                && getCurrentDepartmentPresident(user.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()).equals(
                        user.getPerson());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Person getCurrentDepartmentPresident(Department department) {
        final YearMonthDay today = new YearMonthDay();
        for (final Accountability accountability : department.getDepartmentUnit().getChildsSet()) {
            if (accountability.isPersonFunction() && accountability.isActive(today)) {
                final PersonFunction personFunction = (PersonFunction) accountability;
                final Function function = personFunction.getFunction();
                if (function != null && function.getFunctionType() == FunctionType.PRESIDENT) {
                    final Party childParty = accountability.getChildParty();
                    if (childParty != null && childParty.isPerson()) {
                        return (Person) childParty;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isCurrentUserCurrentDepartmentPresident(Department department) {
        final Person person = AccessControl.getPerson();
        return person == null ? false : getCurrentDepartmentPresident(department).equals(person);
    }
}
