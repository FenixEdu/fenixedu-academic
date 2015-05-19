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
package org.fenixedu.academic.service.services.commons.student;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.dto.InfoEnrolment;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentCurriculum {

    protected List run(String executionDegreeCode, String studentCurricularPlanID) throws FenixServiceException {

        final StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanID);
        if (studentCurricularPlan == null) {
            throw new NonExistingServiceException("error.readStudentCurriculum.noStudentCurricularPlan");
        }

        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>(studentCurricularPlan.getEnrolmentsSet().size());
        for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
            result.add(InfoEnrolment.newInfoFromDomain(enrolment));
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentCurriculum serviceInstance = new ReadStudentCurriculum();

    @Atomic
    public static List runReadStudentCurriculum(String executionDegreeCode, String studentCurricularPlanID)
            throws FenixServiceException, NotAuthorizedException {
        return serviceInstance.run(executionDegreeCode, studentCurricularPlanID);
    }

}