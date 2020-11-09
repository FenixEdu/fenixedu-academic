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
package org.fenixedu.academic.service.services.teacher;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.LessonPlanning;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class DeleteLessonPlanning {

    protected void run(String executionCourseID, LessonPlanning lessonPlanning, ExecutionCourse executionCourse,
            ShiftType shiftType) {
        if (lessonPlanning != null) {
            lessonPlanning.delete();
        } else if (executionCourse != null && shiftType != null) {
            deleteLessonPlanningsByLessonType(executionCourse, shiftType);
        }
    }

    private void deleteLessonPlanningsByLessonType(ExecutionCourse executionCourse, ShiftType shiftType) {
        List<LessonPlanning> lessonPlanningsOrderedByOrder = LessonPlanning.findOrdered(executionCourse, shiftType);
        for (LessonPlanning planning : lessonPlanningsOrderedByOrder) {
            planning.deleteWithoutReOrder();
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteLessonPlanning serviceInstance = new DeleteLessonPlanning();

    @Atomic
    public static void runDeleteLessonPlanning(String executionCourseID, LessonPlanning lessonPlanning,
            ExecutionCourse executionCourse, ShiftType shiftType) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, lessonPlanning, executionCourse, shiftType);
    }

}