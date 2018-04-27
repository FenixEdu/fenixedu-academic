/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.util.email;

import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class DepartmentMemberBasedSender extends DepartmentMemberBasedSender_Base {

    public DepartmentMemberBasedSender() {
        super();
    }

    public DepartmentMemberBasedSender(final Unit unit, final String fromAddress, final Group members) {
        super();
        init(unit, fromAddress, members);
    }

    @Atomic
    public static DepartmentMemberBasedSender newInstance(final Unit unit) {
        return new DepartmentMemberBasedSender(unit, Bennu.getInstance().getSystemSender().getFromAddress(), UnitGroup.recursiveWorkers(unit));
    }

}
