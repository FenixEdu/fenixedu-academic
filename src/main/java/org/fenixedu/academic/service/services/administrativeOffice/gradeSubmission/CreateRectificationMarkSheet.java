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
package org.fenixedu.academic.service.services.administrativeOffice.gradeSubmission;

import java.util.Date;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;

import pt.ist.fenixframework.Atomic;

public class CreateRectificationMarkSheet {

    @Atomic
    public static MarkSheet run(MarkSheet markSheet, EnrolmentEvaluation enrolmentEvaluation, Grade newGrade,
            Date evaluationDate, String reason, Person person) throws InvalidArgumentsServiceException {
        if (markSheet == null) {
            throw new InvalidArgumentsServiceException();
        }
        CurricularCourse curricularCourse = markSheet.getCurricularCourse();
        return curricularCourse.rectifyEnrolmentEvaluation(markSheet, enrolmentEvaluation, evaluationDate, newGrade, reason,
                person);
    }

}
