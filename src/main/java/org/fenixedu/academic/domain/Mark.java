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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.InvalidMarkDomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;

public class Mark extends Mark_Base {

    public Mark() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Mark(final Attends attends, final Evaluation evaluation, final String mark) {
        this();
        setAttend(attends);
        setEvaluation(evaluation);
        setMark(mark);
        setPublishedMark(null);
    }

    @Override
    public void setMark(String mark) {
        if (validateMark(mark)) {
            super.setMark(mark);
        } else {
            throw new InvalidMarkDomainException("errors.invalidMark", mark, getAttend().getRegistration().getNumber().toString());
        }
    }

    public void delete() {
        setAttend(null);
        setEvaluation(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private boolean validateMark(String mark) {
        GradeScale gradeScale;
        if (getEvaluation() instanceof Project || getEvaluation() instanceof FinalEvaluation) {
            if (getAttend().getEnrolment() == null) {
                final Registration registration = getAttend().getRegistration();
                final StudentCurricularPlan studentCurricularPlan =
                        registration.getStudentCurricularPlan(getAttend().getExecutionPeriod());
                final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
                final DegreeType degreeType = degreeCurricularPlan.getDegreeType();
                if (degreeType.isEmpty()) {
                    gradeScale = GradeScale.TYPE20;
                } else {
                    gradeScale = degreeCurricularPlan.getGradeScaleChain();
                }
            } else {
                gradeScale = getAttend().getEnrolment().getCurricularCourse().getGradeScaleChain();
            }
        } else {
            gradeScale = getEvaluation().getGradeScale();
        }
        return gradeScale.isValid(mark, getEvaluation().getEvaluationType());
    }

}
