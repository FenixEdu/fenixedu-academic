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

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.person.RoleType;

public class ResourceAllocationRolePredicates {

    // Lesson Predicates

    public static final AccessControlPredicate<Lesson> checkPermissionsToManageLessons = new AccessControlPredicate<Lesson>() {
        @Override
        public boolean evaluate(Lesson lesson) {
            return RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(AccessControl.getPerson().getUser());
        }
    };

    // Shift Predicates

    public static final AccessControlPredicate<Shift> checkPermissionsToManageShifts = new AccessControlPredicate<Shift>() {
        @Override
        public boolean evaluate(Shift shift) {
            return RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(AccessControl.getPerson().getUser());
        }
    };

    // SchoolClass Predicates

    public static final AccessControlPredicate<SchoolClass> checkPermissionsToManageSchoolClass =
            new AccessControlPredicate<SchoolClass>() {
                @Override
                public boolean evaluate(SchoolClass schoolClass) {
                    return RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(AccessControl.getPerson().getUser());
                }
            };

    // Lesson Instance Predicates

    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstances =
            new AccessControlPredicate<LessonInstance>() {
                @Override
                public boolean evaluate(LessonInstance lessonInstance) {
                    return RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(AccessControl.getPerson().getUser());
                }
            };

    public static final AccessControlPredicate<LessonInstance> checkPermissionsToManageLessonInstancesWithTeacherCheck =
            new AccessControlPredicate<LessonInstance>() {
                @Override
                public boolean evaluate(LessonInstance lessonInstance) {

                    Person loggedPerson = AccessControl.getPerson();

                    ExecutionCourse executionCourse = lessonInstance.getLesson().getExecutionCourse();
                    if (loggedPerson.getProfessorshipsSet().size() > 0
                            && loggedPerson.hasProfessorshipForExecutionCourse(executionCourse)) {
                        return true;
                    }

                    return RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(AccessControl.getPerson().getUser());
                }
            };

    // Punctual Rooms Occupation Management Predicates

}
