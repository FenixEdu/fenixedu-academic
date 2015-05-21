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
package org.fenixedu.academic.domain.student.curriculum;

import java.io.Serializable;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.util.MultiLanguageString;

abstract public class CurriculumEntry implements Serializable, ICurriculumEntry {

    public boolean isNotInDegreeCurriculumEnrolmentEntry() {
        return false;
    }

    final public boolean getIsNotInDegreeCurriculumEnrolmentEntry() {
        return isNotInDegreeCurriculumEnrolmentEntry();
    }

    protected double ectsCredits(final CurricularCourse curricularCourse) {
        final double ectsCredits = curricularCourse.getEctsCredits().doubleValue();
        return ectsCredits == 0 ? 6.0 : ectsCredits;
    }

    @Override
    abstract public Grade getGrade();

    @Override
    public String getGradeValue() {
        return getGrade() == null ? null : getGrade().getValue();
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
        return null;
    }

    @Override
    final public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Override
    final public ExecutionYear getExecutionYear() {
        return getExecutionPeriod() == null ? null : getExecutionPeriod().getExecutionYear();
    }

    @Override
    final public String getCode() {
        return null;
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString();
    }

    @Override
    public MultiLanguageString getPresentationName() {
        return getName();
    }
}
