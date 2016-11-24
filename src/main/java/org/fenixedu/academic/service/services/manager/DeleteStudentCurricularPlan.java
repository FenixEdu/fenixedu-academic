/**
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
/*
 * Created on Feb 18, 2005
 *
 */
package org.fenixedu.academic.service.services.manager;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.util.EnrolmentEvaluationState;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan {

    @Atomic
    public static void run(final String studentCurricularPlanId) throws DomainException, NonExistingServiceException {
        final StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanId);

        if (studentCurricularPlan != null) {

            for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                for (EnrolmentEvaluation evaluation : enrolment.getEvaluationsSet()) {
                    evaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
                }
            }

            studentCurricularPlan.delete();
        } else {
            throw new NonExistingServiceException();
        }
    }
}