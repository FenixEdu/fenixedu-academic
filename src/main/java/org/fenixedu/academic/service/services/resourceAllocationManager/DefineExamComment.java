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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DefineExamComment {

    @Atomic
    public static void run(String executionCourseInitials, String executionPeriodId, String comment) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        final ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseInitials);

        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        executionCourse.setComment(comment);
    }

}