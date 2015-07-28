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
package org.fenixedu.academic.service.services.enrollment.shift;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

public class WriteStudentAttendingCourse {

    protected void run(Registration registration, String executionCourseId) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, executionCourseId);

        if (registration == null) {
            throw new FenixServiceException("error.registration.not.exist");
        }
        registration.addAttendsTo(readExecutionCourse(executionCourseId));
    }

    private ExecutionCourse readExecutionCourse(String executionCourseId) throws FenixServiceException {
        if (Strings.isNullOrEmpty(executionCourseId)) {
            throw new FenixServiceException("errors.notSelected.executionCourse");
        }
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("error.executionCourse.not.exist");
        }
        return executionCourse;
    }

    // Service Invokers migrated from Berserk

    private static final WriteStudentAttendingCourse serviceInstance = new WriteStudentAttendingCourse();

    @Atomic
    public static void runWriteStudentAttendingCourse(Registration registration, String executionCourseId)
            throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        serviceInstance.run(registration, executionCourseId);
    }

    @Atomic
    public static void runWriteStudentAttendingCourse(Registration registration, String executionCourseId,
            ExecutionSemester executionSemester) throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration, executionSemester);
        serviceInstance.run(registration, executionCourseId);
    }

}