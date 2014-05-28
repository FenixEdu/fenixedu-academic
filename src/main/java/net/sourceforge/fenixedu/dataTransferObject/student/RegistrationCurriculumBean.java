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
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.joda.time.YearMonthDay;

public class RegistrationCurriculumBean extends RegistrationSelectExecutionYearBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private CycleCurriculumGroup cycleCurriculumGroup;

    public RegistrationCurriculumBean(Registration registration) {
        setRegistration(registration);

        if (registration.isBolonha()) {
            final List<CycleCurriculumGroup> internalCycleCurriculumGrops =
                    registration.getLastStudentCurricularPlan().getInternalCycleCurriculumGrops();
            if (internalCycleCurriculumGrops.size() == 1) {
                setCycleCurriculumGroup(internalCycleCurriculumGrops.iterator().next());
            }
        }
    }

    public CycleCurriculumGroup getCycleCurriculumGroup() {
        return this.cycleCurriculumGroup;
    }

    public void setCycleCurriculumGroup(CycleCurriculumGroup cycleCurriculumGroup) {
        this.cycleCurriculumGroup = cycleCurriculumGroup;
    }

    public boolean hasCycleCurriculumGroup() {
        return cycleCurriculumGroup != null;
    }

    public Integer getFinalAverage() {
        if (hasCycleCurriculumGroup() && getCycleCurriculumGroup().isConclusionProcessed()) {
            return getCycleCurriculumGroup().getFinalAverage();
        } else if (getRegistration().isRegistrationConclusionProcessed()) {
            return getRegistration().getFinalAverage();
        } else {
            return null;
        }
    }

    public BigDecimal getAverage() {
        return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateAverage() : getRegistration().calculateAverage();
    }

    public YearMonthDay getConclusionDate() {
        if (hasCycleCurriculumGroup() && getCycleCurriculumGroup().isConclusionProcessed()) {
            return getCycleCurriculumGroup().getConclusionDate();
        } else if (getRegistration().isRegistrationConclusionProcessed()) {
            return getRegistration().getConclusionDate();
        } else {
            return null;
        }
    }

    public double getEctsCredits() {
        return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCreditsConcluded() : getRegistration().getEctsCredits();
    }

    public ICurriculum getCurriculum(final ExecutionYear executionYear) {
        return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCurriculum(executionYear) : getRegistration()
                .getCurriculum(executionYear);
    }

    public ICurriculum getCurriculum() {
        return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCurriculum() : getRegistration().getCurriculum();
    }

    public boolean isConcluded() {
        return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().isConcluded() : getRegistration().hasConcluded();
    }

    public boolean isConclusionProcessed() {
        return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().isConclusionProcessed() : getRegistration()
                .isRegistrationConclusionProcessed();
    }

}
