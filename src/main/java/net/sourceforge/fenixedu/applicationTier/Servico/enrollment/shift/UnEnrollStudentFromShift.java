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
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class UnEnrollStudentFromShift {

    protected void run(final Registration registration, final String shiftId) throws StudentNotFoundServiceException,
            ShiftNotFoundServiceException, ShiftEnrolmentNotFoundServiceException, FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), registration, shiftId);

        if (registration == null) {
            throw new StudentNotFoundServiceException();
        }
        if (registration.getPayedTuition() == null || registration.getPayedTuition().equals(Boolean.FALSE)) {
            throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
        }

        final Shift shift = FenixFramework.getDomainObject(shiftId);
        if (shift == null) {
            throw new ShiftNotFoundServiceException();
        }

        shift.removeStudents(registration);
    }

    public class StudentNotFoundServiceException extends FenixServiceException {
    }

    public class ShiftNotFoundServiceException extends FenixServiceException {
    }

    public class ShiftEnrolmentNotFoundServiceException extends FenixServiceException {
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

}