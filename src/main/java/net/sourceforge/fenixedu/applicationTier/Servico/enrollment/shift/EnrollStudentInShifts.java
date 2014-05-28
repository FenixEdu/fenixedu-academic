/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentErrorReport;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EnrollStudentInShifts {

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    public ShiftEnrollmentErrorReport run(final Registration registration, final String shiftId) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, shiftId);

        final ShiftEnrollmentErrorReport errorReport = new ShiftEnrollmentErrorReport();

        final Shift selectedShift = FenixFramework.getDomainObject(shiftId);

        if (selectedShift == null) {
            errorReport.getUnExistingShifts().add(shiftId);
            return errorReport;
        }

        if (registration == null || !selectedShift.getExecutionCourse().hasAttendsFor(registration.getStudent())) {
            throw new StudentNotFoundServiceException();
        }

        if (registration.getPayedTuition() == null || registration.getPayedTuition().equals(Boolean.FALSE)) {
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
        for (final Shift shiftFromStudent : registration.getShifts()) {
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

}