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
package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
    public MultiLanguageString getName() {
        return enrolmentDomainReference.getName();
    }

}
