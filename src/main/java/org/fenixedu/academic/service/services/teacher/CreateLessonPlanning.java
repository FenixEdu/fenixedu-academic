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

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.LessonPlanning;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.MultiLanguageString;

import pt.ist.fenixframework.Atomic;

public class CreateLessonPlanning {

    protected void run(String executionCourseId, MultiLanguageString title, MultiLanguageString planning, ShiftType lessonType,
            ExecutionCourse executionCourse) {

        new LessonPlanning(title, planning, lessonType, executionCourse);
    }

    // Service Invokers migrated from Berserk

    private static final CreateLessonPlanning serviceInstance = new CreateLessonPlanning();

    @Atomic
    public static void runCreateLessonPlanning(String executionCourseId, MultiLanguageString title, MultiLanguageString planning,
            ShiftType lessonType, ExecutionCourse executionCourse) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, title, planning, lessonType, executionCourse);
    }

}