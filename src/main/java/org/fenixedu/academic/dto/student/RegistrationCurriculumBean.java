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
package org.fenixedu.academic.dto.student;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.joda.time.YearMonthDay;

public class RegistrationCurriculumBean extends RegistrationSelectExecutionYearBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private ProgramConclusion programConclusion;

    public RegistrationCurriculumBean(Registration registration) {
        setRegistration(registration);
    }

    public ProgramConclusion getProgramConclusion() {
        return programConclusion;
    }

    public void setProgramConclusion(ProgramConclusion programConclusion) {
        this.programConclusion = programConclusion;
    }

    public CurriculumGroup getCurriculumGroup() {
        return getProgramConclusion() == null ? null : getProgramConclusion().groupFor(getRegistration()).orElse(null);
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        if (curriculumGroup.getDegreeModule().getProgramConclusion() != null) {
            setProgramConclusion(curriculumGroup.getDegreeModule().getProgramConclusion());
        } else {
            setProgramConclusion(null);
        }
    }

    public boolean hasCurriculumGroup() {
        return getCurriculumGroup() != null;
    }

    public Grade getFinalGrade() {
        if (hasCurriculumGroup() && getCurriculumGroup().isConclusionProcessed()) {
            return getCurriculumGroup().getFinalGrade();
        } else if (getRegistration().isRegistrationConclusionProcessed()) {
            return getRegistration().getFinalGrade();
        } else {
            return null;
        }
    }

    public Grade getRawGrade() {
        return hasCurriculumGroup() ? getCurriculumGroup().calculateRawGrade() : getRegistration().calculateRawGrade();
    }

    public YearMonthDay getConclusionDate() {
        if (hasCurriculumGroup() && getCurriculumGroup().isConclusionProcessed()) {
            return getCurriculumGroup().getConclusionDate();
        } else if (getRegistration().isRegistrationConclusionProcessed()) {
            return getRegistration().getConclusionDate();
        } else {
            return null;
        }
    }

    public double getEctsCredits() {
        return hasCurriculumGroup() ? getCurriculumGroup().getCreditsConcluded() : getRegistration().getEctsCredits();
    }

    public ICurriculum getCurriculum(final ExecutionYear executionYear) {
        return hasCurriculumGroup() ? getCurriculumGroup().getCurriculum(executionYear) : getRegistration().getCurriculum(
                executionYear);
    }

    public ICurriculum getCurriculum() {
        return hasCurriculumGroup() ? getCurriculumGroup().getCurriculum() : getRegistration().getCurriculum();
    }

    public boolean isConcluded() {
        return hasCurriculumGroup() ? getCurriculumGroup().isConcluded() : getRegistration().hasConcluded();
    }

    public boolean isConclusionProcessed() {
        return hasCurriculumGroup() ? getCurriculumGroup().isConclusionProcessed() : getRegistration()
                .isRegistrationConclusionProcessed();
    }

}
