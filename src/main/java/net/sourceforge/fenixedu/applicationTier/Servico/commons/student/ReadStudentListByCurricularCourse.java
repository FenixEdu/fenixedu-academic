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
package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.StudentListByCurricularCourseAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.beanutils.BeanComparator;
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
        StudentListByCurricularCourseAuthorizationFilter.instance.execute(curricularCourseID);
        return serviceInstance.run(userView, curricularCourseID, executionYear);
    }

}