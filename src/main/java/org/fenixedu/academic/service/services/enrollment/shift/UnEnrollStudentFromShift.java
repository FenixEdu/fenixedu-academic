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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class UnEnrollStudentFromShift {

    protected void run(final Registration registration, final String shiftId) throws StudentNotFoundServiceException,
            ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException, FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, shiftId);

        if (registration == null) {
            throw new StudentNotFoundServiceException();
        }

        final Shift shift = FenixFramework.getDomainObject(shiftId);
        if (shift == null) {
            throw new ShiftNotFoundServiceException();
        }

        shift.removeStudents(registration);
    }

    public static class StudentNotFoundServiceException extends FenixServiceException {
    }

    public static class ShiftNotFoundServiceException extends FenixServiceException {
    }

    public static class ShiftEnrolmentNotFoundServiceException extends FenixServiceException {
    }

    // Service Invokers migrated from Berserk

    private static final UnEnrollStudentFromShift serviceInstance = new UnEnrollStudentFromShift();

    @Atomic
    public static void runUnEnrollStudentFromShift(Registration registration, String shiftId)
            throws StudentNotFoundServiceException, ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException,
            FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        serviceInstance.run(registration, shiftId);
    }

    @Atomic
    public static void runUnEnrollStudentFromShift(Registration registration, String shiftId, ExecutionSemester executionSemester)
            throws StudentNotFoundServiceException, ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException,
            FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration, executionSemester);
        serviceInstance.run(registration, shiftId);
    }

}