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
package org.fenixedu.academic.predicate;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Student;

public class StudentPredicates {

    @Deprecated
    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsStudentOwnerOrManager =
            new AccessControlPredicate<Student>() {
                @Override
                public boolean evaluate(Student student) {
                    final Person person = AccessControl.getPerson();
                    return person.getStudent() == student || RoleType.MANAGER.isMember(person.getUser());
                }
            };

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsCoordinator = new AccessControlPredicate<Student>() {
        @Override
        public boolean evaluate(Student student) {
            return RoleType.COORDINATOR.isMember(AccessControl.getPerson().getUser());
        }
    };

}
