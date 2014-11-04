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
package org.fenixedu.academic.service.services.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.service.filter.TutorshipAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.coordinator.tutor.TutorshipErrorBean;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteTutorship extends TutorshipManagement {

    public List<TutorshipErrorBean> run(String executionDegreeID, List<Tutorship> tutorsToDelete) {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

        for (Tutorship tutorship : tutorsToDelete) {
            Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
            Integer studentNumber = registration.getNumber();

            try {
                validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

                tutorship.delete();

            } catch (FenixServiceException ex) {
                studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
            }
        }

        return studentsWithErrors;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTutorship serviceInstance = new DeleteTutorship();

    @Atomic
    public static List<TutorshipErrorBean> runDeleteTutorship(String executionDegreeID, List<Tutorship> tutorsToDelete)
            throws NotAuthorizedException {
        TutorshipAuthorizationFilter.instance.execute();
        return serviceInstance.run(executionDegreeID, tutorsToDelete);
    }

}