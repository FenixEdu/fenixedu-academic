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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

@GroupOperator("departmentPresident")
public class DepartmentPresidentStrategy extends GroupStrategy {

    private static final long serialVersionUID = -3153992434314606564L;

    @Override
    public Set<User> getMembers() {
        return FluentIterable.from(Bennu.getInstance().getDepartmentsSet()).transform(new Function<Department, User>() {
            @Override
            public User apply(Department input) {
                return input.getCurrentDepartmentPresident().getUser();
            }
        }).filter(Predicates.notNull()).toSet();
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getEmployee() != null
                && user.getPerson().getEmployee().getCurrentDepartmentWorkingPlace() != null
                && user.getPerson().getEmployee().getCurrentDepartmentWorkingPlace()
                        .isCurrentDepartmentPresident(user.getPerson());
    }

}
