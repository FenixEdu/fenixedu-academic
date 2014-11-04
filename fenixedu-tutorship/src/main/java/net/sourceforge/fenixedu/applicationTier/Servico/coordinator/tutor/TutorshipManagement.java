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

import java.util.List;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public abstract class TutorshipManagement {

    private boolean verifyIfBelongsToDegree(Registration registration, Degree degree) {
        if (registration == null) {
            return false;
        }

        StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return false;
        }

        return studentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(degree);
    }

    protected void validateTeacher(Teacher teacher, ExecutionDegree executionDegree) throws FenixServiceException {
        List<Teacher> possibleTutorsForExecutionDegree =
                Tutorship.getPossibleTutorsFromExecutionDegreeDepartments(executionDegree);

        if (!possibleTutorsForExecutionDegree.contains(teacher)) {
            throw new FenixServiceException("error.tutor.cannotBeTutorOfExecutionDegree");
        }
    }

    protected void validateStudentRegistration(Registration registration, ExecutionDegree executionDegree,
            DegreeCurricularPlan degreeCurricularPlan, Integer studentNumber) throws FenixServiceException {

        if (!verifyIfBelongsToDegree(registration, degreeCurricularPlan.getDegree())) {
            // student doesn't belong to this degree
            String degreeType = BundleUtil.getString(Bundle.ENUMERATION, executionDegree.getDegree().getDegreeType().getName());

            throw new FenixServiceException("error.tutor.studentNoDegree", new String[] { studentNumber.toString(), degreeType,
                    executionDegree.getDegree().getNameFor(registration.getStartExecutionYear()).getContent() });
        }
    }

    protected void validateTutorship(Registration registration) throws FenixServiceException {
        if (Tutorship.getActiveTutorship(registration.getLastStudentCurricularPlan()) != null) {
            // student already with tutor
            throw new FenixServiceException("error.tutor.studentAlreadyHasTutor", new String[] { registration.getNumber()
                    .toString() });
        }
    }
}
