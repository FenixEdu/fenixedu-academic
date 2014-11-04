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
import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public interface ICurriculum {

    abstract public Collection<ICurriculumEntry> getCurriculumEntries();

    abstract public Set<ICurriculumEntry> getCurricularYearEntries();

    abstract public BigDecimal getSumPiCi();

    abstract public BigDecimal getSumPi();

    abstract public BigDecimal getAverage();

    abstract public Integer getRoundedAverage();

    abstract public BigDecimal getSumEctsCredits();

    abstract public Integer getCurricularYear();

    abstract public Integer getTotalCurricularYears();

    abstract public boolean isEmpty();

    abstract public BigDecimal getRemainingCredits();

    abstract public StudentCurricularPlan getStudentCurricularPlan();

    abstract public void setAverageType(AverageType averageType);

    abstract public boolean hasAnyExternalApprovedEnrolment();
}
