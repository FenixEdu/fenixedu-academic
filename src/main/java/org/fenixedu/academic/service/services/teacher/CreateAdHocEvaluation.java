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

import org.fenixedu.academic.domain.AdHocEvaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.service.filter.ExecutionCourseCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateAdHocEvaluation {

    protected void run(String executionCourseID, String name, String description, GradeScale gradeScale)
            throws FenixServiceException {

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        new AdHocEvaluation(executionCourse, name, description, gradeScale);
    }

    // Service Invokers migrated from Berserk

    private static final CreateAdHocEvaluation serviceInstance = new CreateAdHocEvaluation();

    @Atomic
    public static void runCreateAdHocEvaluation(String executionCourseID, String name, String description, GradeScale gradeScale)
            throws FenixServiceException, NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, name, description, gradeScale);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, name, description, gradeScale);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}