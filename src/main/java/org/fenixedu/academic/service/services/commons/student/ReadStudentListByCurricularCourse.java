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
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.dto.InfoEnrolment;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentListByCurricularCourse {

    protected List run(final User userView, final String curricularCourseID, final String executionYear)
            throws FenixServiceException {

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseID);
        return (executionYear != null) ? cleanList(curricularCourse.getEnrolmentsByYear(executionYear)) : cleanList(curricularCourse
                .getEnrolments());
    }

    private List cleanList(final List<Enrolment> enrolmentList) throws FenixServiceException {

        if (enrolmentList.isEmpty()) {
            throw new NonExistingServiceException();
        }

        Integer studentNumber = null;
        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        for (final Enrolment enrolment : enrolmentList) {

            if (studentNumber == null
                    || studentNumber.intValue() != enrolment.getStudentCurricularPlan().getRegistration().getNumber().intValue()) {
                studentNumber = enrolment.getStudentCurricularPlan().getRegistration().getNumber();
                result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        Collections.sort(result, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentListByCurricularCourse serviceInstance = new ReadStudentListByCurricularCourse();

    @Atomic
    public static List runReadStudentListByCurricularCourse(User userView, String curricularCourseID, String executionYear)
            throws FenixServiceException, NotAuthorizedException {
        return serviceInstance.run(userView, curricularCourseID, executionYear);
    }

}