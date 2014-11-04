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
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Person;
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
