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
/*
 * Created on Nov 8, 2005
 *  by jdnf
 */
package org.fenixedu.academic.service.services.teacher;

import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.service.filter.ExecutionCourseCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateProject {

    protected void run(String executionCourseID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, String groupingID, GradeScale gradeScale,
            List<Department> departments) throws FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        final Grouping grouping = (groupingID != null) ? FenixFramework.<Grouping> getDomainObject(groupingID) : null;

        final Project project =
                new Project(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping,
                        executionCourse, gradeScale);
        project.getDeparmentsSet().addAll(departments);
    }

    // Service Invokers migrated from Berserk

    private static final CreateProject serviceInstance = new CreateProject();

    @Atomic
    public static void runCreateProject(String executionCourseID, String name, Date begin, Date end, String description,
            Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, String groupingID, GradeScale gradeScale,
            List<Department> departments) throws FenixServiceException, NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep,
                    groupingID, gradeScale, departments);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, name, begin, end, description, onlineSubmissionsAllowed,
                        maxSubmissionsToKeep, groupingID, gradeScale, departments);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}