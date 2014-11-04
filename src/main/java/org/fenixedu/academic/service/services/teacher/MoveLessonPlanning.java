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

import org.fenixedu.academic.domain.LessonPlanning;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class MoveLessonPlanning {

    protected void run(String executionCourseID, LessonPlanning lessonPlanning, Integer order) {
        lessonPlanning.moveTo(order);
    }

    // Service Invokers migrated from Berserk

    private static final MoveLessonPlanning serviceInstance = new MoveLessonPlanning();

    @Atomic
    public static void runMoveLessonPlanning(String executionCourseID, LessonPlanning lessonPlanning, Integer order)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, lessonPlanning, order);
    }

}