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

import java.math.BigDecimal;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Sets;

public class NotInDegreeCurriculumCurriculumEntry extends CurriculumEntry {

    private final Enrolment enrolmentDomainReference;

    public NotInDegreeCurriculumCurriculumEntry(final Enrolment enrolment) {
        super();
        this.enrolmentDomainReference = enrolment;
    }

    @Override
    public boolean isNotInDegreeCurriculumEnrolmentEntry() {
        return true;
    }

    public Enrolment getEnrolment() {
        return enrolmentDomainReference;
    }

    @Override
    public BigDecimal getEctsCreditsForCurriculum() {
        return BigDecimal.valueOf(getEnrolment().getEctsCredits());
    }

    @Override
    public Set<CurriculumLine> getCurriculumLinesForCurriculum() {
        return Sets.newHashSet(getEnrolment());
    }

    @Override
    public BigDecimal getWeigthForCurriculum() {
        return BigDecimal.valueOf(getEnrolment().getWeigth());
    }

    @Override
    public Grade getGrade() {
        return getEnrolment().getGrade();
    }

    @Override
    final public ExecutionSemester getExecutionPeriod() {
        return getEnrolment().getExecutionPeriod();
    }

    @Override
    public String getExternalId() {
        return getEnrolment().getExternalId();
    }

    @Override
    public DateTime getCreationDateDateTime() {
        return getEnrolment().getCreationDateDateTime();
    }

    @Override
    public YearMonthDay getApprovementDate() {
        return getEnrolment().getApprovementDate();
    }

    @Override
    public LocalizedString getName() {
        return enrolmentDomainReference.getName();
    }

}
