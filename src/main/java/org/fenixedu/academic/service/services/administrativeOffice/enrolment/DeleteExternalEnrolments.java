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
package org.fenixedu.academic.service.services.administrativeOffice.enrolment;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class DeleteExternalEnrolments {

    @Atomic
    public static void run(final Registration registration, String[] externalEnrolmentIDs) throws FenixServiceException {
        for (final String externalEnrolmentID : externalEnrolmentIDs) {
            final ExternalEnrolment externalEnrolment = getExternalEnrolmentByID(registration, externalEnrolmentID);
            if (externalEnrolment == null) {
                throw new FenixServiceException("error.DeleteExternalEnrolments.externalEnrolmentID.doesnot.belong.to.student");
            }
            externalEnrolment.delete();
        }
    }

    private static ExternalEnrolment getExternalEnrolmentByID(final Registration registration, final String externalEnrolmentID) {
        for (final ExternalEnrolment externalEnrolment : registration.getExternalEnrolmentsSet()) {
            if (externalEnrolment.getExternalId().equals(externalEnrolmentID)) {
                return externalEnrolment;
            }
        }
        return null;
    }
}