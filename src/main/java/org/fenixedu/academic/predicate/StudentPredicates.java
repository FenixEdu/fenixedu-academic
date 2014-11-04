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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentPredicates {

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsStudentOwnerOrManager =
            new AccessControlPredicate<Student>() {
                @Override
                public boolean evaluate(Student student) {
                    final Person person = AccessControl.getPerson();
                    return person.getStudent() == student || person.hasRole(RoleType.MANAGER);
                }
            };

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsCoordinator = new AccessControlPredicate<Student>() {
        @Override
        public boolean evaluate(Student student) {
            return AccessControl.getPerson().hasRole(RoleType.COORDINATOR);
        }
    };

}
