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
import java.util.Collection;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.joda.time.YearMonthDay;

public interface ICurriculum {

    abstract public Collection<ICurriculumEntry> getCurriculumEntries();

    abstract public Set<ICurriculumEntry> getCurricularYearEntries();

    abstract public ExecutionYear getLastExecutionYear();

    abstract public YearMonthDay getLastApprovementDate();

    abstract public Grade getRawGrade();

    abstract public Grade getFinalGrade();

    abstract public Grade getUnroundedGrade();

    abstract public BigDecimal getSumEctsCredits();

    abstract public Integer getCurricularYear();

    abstract public Integer getTotalCurricularYears();

    abstract public boolean isEmpty();

    abstract public BigDecimal getRemainingCredits();

    abstract public StudentCurricularPlan getStudentCurricularPlan();

    abstract public boolean hasAnyExternalApprovedEnrolment();
}
