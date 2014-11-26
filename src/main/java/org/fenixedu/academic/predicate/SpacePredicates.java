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
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonSpaceOccupation;
import org.fenixedu.academic.domain.space.SpaceOccupation;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;

public class SpacePredicates {

    private static final AccessControlPredicate<SpaceOccupation> checkPermissionsToManageOccupationsWithoutCheckSpaceManager =
            new AccessControlPredicate<SpaceOccupation>() {
                @Override
                public boolean evaluate(SpaceOccupation spaceOccupation) {
                    spaceOccupation.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManager();
                    return true;
                }
            };
    public static final AccessControlPredicate<WrittenEvaluationSpaceOccupation> checkPermissionsToManageWrittenEvaluationSpaceOccupations =
            new AccessControlPredicate<WrittenEvaluationSpaceOccupation>() {
                @Override
                public boolean evaluate(WrittenEvaluationSpaceOccupation spaceOccupation) {
//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupationsWithoutCheckSpaceManager.evaluate(spaceOccupation);
                }
            };
//
    public static final AccessControlPredicate<LessonSpaceOccupation> checkPermissionsToManageLessonSpaceOccupations =
            new AccessControlPredicate<LessonSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonSpaceOccupation spaceOccupation) {
//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return checkPermissionsToManageOccupationsWithoutCheckSpaceManager.evaluate(spaceOccupation);
                }
            };
//
    public static final AccessControlPredicate<LessonSpaceOccupation> checkPermissionsToDeleteLessonSpaceOccupations =
            new AccessControlPredicate<LessonSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonSpaceOccupation spaceOccupation) {

                    Person loggedPerson = AccessControl.getPerson();

                    ExecutionCourse executionCourse = spaceOccupation.getLesson().getExecutionCourse();
                    if (loggedPerson.hasProfessorshipForExecutionCourse(executionCourse)) {
                        return true;
                    }

                    return checkPermissionsToManageLessonSpaceOccupations.evaluate(spaceOccupation);
                }
            };
//
    public static final AccessControlPredicate<LessonInstanceSpaceOccupation> checkPermissionsToManageLessonInstanceSpaceOccupations =
            new AccessControlPredicate<LessonInstanceSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonInstanceSpaceOccupation lessonInstanceSpaceOccupation) {
//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(AccessControl.getPerson());
                    return true;
                }
            };
//
    public static final AccessControlPredicate<LessonInstanceSpaceOccupation> checkPermissionsToManageLessonInstanceSpaceOccupationsWithTeacherCheck =
            new AccessControlPredicate<LessonInstanceSpaceOccupation>() {
                @Override
                public boolean evaluate(LessonInstanceSpaceOccupation lessonInstanceSpaceOccupation) {

                    Person loggedPerson = AccessControl.getPerson();

                    if (loggedPerson.getProfessorshipsSet().size() > 0) {
                        return true;
                    }

//                    ResourceAllocationRole.checkIfPersonHasPermissionToManageSchedulesAllocation(loggedPerson);
                    return true;
                }
            };

}
