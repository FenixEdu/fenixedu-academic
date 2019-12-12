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

import org.fenixedu.academic.domain.curriculum.grade.GradeScale;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.EvaluationType;
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
            throw new DomainException("errors.invalidMark", mark, getAttend().getRegistration().getNumber().toString());
        }
    }

    public void delete() {
        setAttend(null);
        setEvaluation(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    private boolean validateMark(String mark) {
        try {
            final Grade grade = Grade.createGrade(mark, getEvaluation().getGradeScale());
            if (grade.isEmpty()) {
                return false;
            }
            
            return getEvaluation().getGradeScale().belongsTo(mark);
        } catch (DomainException de) {
            return false;
        }
    }

}
