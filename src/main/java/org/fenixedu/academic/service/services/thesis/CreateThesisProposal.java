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
package org.fenixedu.academic.service.services.thesis;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.MultiLanguageString;

import pt.ist.fenixframework.Atomic;

public class CreateThesisProposal {

    @Atomic
    public static Thesis run(DegreeCurricularPlan degreeCurricularPlan, Student student, MultiLanguageString title, String comment)
            throws NotAuthorizedException {
        final Degree degree = degreeCurricularPlan.getDegree();
        final Enrolment enrolment = student.getDissertationEnrolment(degreeCurricularPlan);

        final Thesis thesis = new Thesis(degree, enrolment, title);
        thesis.setComment(comment);
        return thesis;
    }

}