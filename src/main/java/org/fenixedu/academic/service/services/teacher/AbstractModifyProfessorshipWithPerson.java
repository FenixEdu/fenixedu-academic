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
package org.fenixedu.academic.service.services.teacher;

import java.util.Collection;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public abstract class AbstractModifyProfessorshipWithPerson {
    @Atomic
    public static Boolean run(Person person) throws NotAuthorizedException {
        /* start chain */
        Person requester = AccessControl.getPerson();
        if ((requester == null) || !requester.hasRole(RoleType.DEPARTMENT_CREDITS_MANAGER)) {
            throw new NotAuthorizedException();
        }
        if (person.getTeacher() != null) {
            final Person requesterPerson = requester;
            Department teacherDepartment = person.getTeacher().getDepartment();
            Collection departmentsWithAccessGranted = requesterPerson.getManageableDepartmentCreditsSet();
            if (!departmentsWithAccessGranted.contains(teacherDepartment)) {
                throw new NotAuthorizedException();
            }
        }
        /* end chain */
        return true;
    }

}
