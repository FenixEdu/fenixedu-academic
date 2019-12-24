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

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.dto.enrollment.shift.ShiftEnrollmentErrorReport;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EnrollStudentInShifts {

    public static class StudentNotFoundServiceException extends FenixServiceException {
    }

    public ShiftEnrollmentErrorReport run(final Registration registration, final String shiftId) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, shiftId);

        final ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

        final Shift selectedShift = FenixFramework.getDomainObject(shiftId);

        if (selectedShift == null) {
            errorReport.getUnExistingShifts().add(shiftId);
            return errorReport;
        }

        if (registration == null || selectedShift.getExecutionCourse().getAttendsByStudent(registration.getStudent()) == null) {
            throw new StudentNotFoundServiceException();
        }

        if (TreasuryBridgeAPIFactory.implementation().isAcademicalActsBlocked(registration.getPerson(), new LocalDate())) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        final Shift shiftFromStudent = findShiftOfSameTypeForSameExecutionCourse(registration, selectedShift);

        if (selectedShift != shiftFromStudent) {
            // Registration is not yet enroled, so let's reserve the shift...
            if (selectedShift.reserveForStudent(registration)) {
                if (shiftFromStudent != null) {
                    shiftFromStudent.removeStudents(registration);
                }
            } else {
                errorReport.getUnAvailableShifts().add(selectedShift);
            }
        }

        return errorReport;
    }

    private Shift findShiftOfSameTypeForSameExecutionCourse(final Registration registration, final Shift shift) {
        for (final Shift shiftFromStudent : registration.getShiftsSet()) {
            if (shiftFromStudent.getTypes().containsAll(shift.getTypes())
                    && shiftFromStudent.getExecutionCourse() == shift.getExecutionCourse()) {
                return shiftFromStudent;
            }
        }
        return null;
    }

    // Service Invokers migrated from Berserk

    private static final EnrollStudentInShifts serviceInstance = new EnrollStudentInShifts();

    @Atomic
    public static ShiftEnrollmentErrorReport runEnrollStudentInShifts(Registration registration, String shiftId)
            throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration);
        return serviceInstance.run(registration, shiftId);
    }

    @Atomic
    public static ShiftEnrollmentErrorReport runEnrollStudentInShifts(Registration registration, String shiftId,
            ExecutionInterval executionInterval) throws FenixServiceException, NotAuthorizedException {
        ClassEnrollmentAuthorizationFilter.instance.execute(registration, executionInterval);
        return serviceInstance.run(registration, shiftId);
    }

}