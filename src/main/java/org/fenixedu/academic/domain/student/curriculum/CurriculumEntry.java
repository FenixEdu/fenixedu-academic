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
package org.fenixedu.academic.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
    public BigDecimal getWeigthTimesGrade() {
        final String grade = getGradeValue();
        return StringUtils.isNumeric(grade) ? getWeigthForCurriculum().multiply(BigDecimal.valueOf(Double.valueOf(grade))) : null;
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
