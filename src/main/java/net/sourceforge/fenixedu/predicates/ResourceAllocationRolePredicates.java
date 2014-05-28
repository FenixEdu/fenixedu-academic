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

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ResourceAllocationRolePredicates {

    // Lesson Predicates

    public static final AccessControlPredicate<Lesson> checkPermissionsToManageLessons = new AccessControlPredicate<Lesson>() {
        @Override
        public boolean evaluate(Lesson lesson) {
            return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
        }
    };

    // Shift Predicates

    public static final AccessControlPredicate<Shift> checkPermissionsToManageShifts = new AccessControlPredicate<Shift>() {
        @Override
        public boolean evaluate(Shift shift) {
            return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
        }
    };

    // SchoolClass Predicates

    public static final AccessControlPredicate<SchoolClass> checkPermissionsToManageSchoolClass =
            new AccessControlPredicate<SchoolClass>() {
                @Override
                public boolean evaluate(SchoolClass schoolClass) {
                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    // Lesson Instance Predicates

    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstances =
            new AccessControlPredicate<LessonInstance>() {
                @Override
                public boolean evaluate(LessonInstance lessonInstance) {
                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstancesWithTeacherCheck =
            new AccessControlPredicate<LessonInstance>() {
                @Override
                public boolean evaluate(LessonInstance lessonInstance) {

                    Person loggedPerson = AccessControl.getPerson();

                    if (loggedPerson.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                        return true;
                    }

                    ExecutionCourse executionCourse = lessonInstance.getLesson().getExecutionCourse();
                    if (loggedPerson.getProfessorships().size() > 0
                            && loggedPerson.hasProfessorshipForExecutionCourse(executionCourse)) {
                        return true;
                    }

                    return AccessControl.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                }
            };

    // Punctual Rooms Occupation Management Predicates

}
