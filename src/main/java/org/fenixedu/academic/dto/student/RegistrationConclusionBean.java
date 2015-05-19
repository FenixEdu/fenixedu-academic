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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public class RegistrationConclusionBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private Registration registration;

    private ProgramConclusion programConclusion;

    private Boolean hasAccessToRegistrationConclusionProcess = Boolean.TRUE;

    private LocalDate enteredConclusionDate;

    private Integer enteredFinalAverageGrade;

    private Double enteredAverageGrade;

    private String observations;

    public RegistrationConclusionBean(final Registration registration) {
        setRegistration(registration);
    }

    @Deprecated
    public RegistrationConclusionBean(final Registration registration, final CycleType cycleType) {
        this(registration, registration.getLastStudentCurricularPlan().getCycle(cycleType));
    }

    @Deprecated
    public RegistrationConclusionBean(final Registration registration, final CurriculumGroup curriculumGroup) {
        setRegistration(registration);
        setCurriculumGroup(curriculumGroup);
    }

    public RegistrationConclusionBean(final Registration registration, ProgramConclusion programConclusion) {
        setRegistration(registration);
        setProgramConclusion(programConclusion);
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

    public ProgramConclusion getProgramConclusion() {
        return programConclusion;
    }

    public void setProgramConclusion(ProgramConclusion programConclusion) {
        this.programConclusion = programConclusion;
    }

    public boolean hasCurriculumGroup() {
        return getCurriculumGroup() != null;
    }

    @Override
    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public ExecutionYear getStartExecutionYear() {
        return getRegistration().getStartExecutionYear();
    }

    public Integer getFinalAverage() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getFinalAverage();
        }

        return calculateFinalAverage();
    }

    public Integer calculateFinalAverage() {
        return getCurriculumGroup().calculateRoundedAverage();
    }

    public Integer getCalculatedFinalAverage() {
        return calculateFinalAverage();
    }

    public BigDecimal getAverage() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getAverage();
        }

        return calculateAverage();
    }

    public BigDecimal calculateAverage() {
        return getCurriculumGroup().calculateAverage();
    }

    public BigDecimal getCalculatedAverage() {
        return calculateAverage();
    }

    public YearMonthDay getConclusionDate() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getConclusionDate();
        }

        return calculateConclusionDate();
    }

    public YearMonthDay calculateConclusionDate() {
        return getCurriculumGroup().calculateConclusionDate();
    }

    public YearMonthDay getCalculatedConclusionDate() {
        return calculateConclusionDate();
    }

    public ExecutionYear getIngressionYear() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getIngressionYear();
        }

        return calculateIngressionYear();
    }

    public ExecutionYear calculateIngressionYear() {
        return getRegistration().calculateIngressionYear();
    }

    public ExecutionYear getConclusionYear() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getConclusionYear();
        }
        return calculateConclusionYear();
    }

    public ExecutionYear calculateConclusionYear() {
        return getRegistration().calculateConclusionYear();
    }

    public ExecutionYear getCalculatedConclusionYear() {
        return calculateConclusionYear();
    }

    public boolean hasDissertationThesis() {
        return getRegistration().hasDissertationThesis();
    }

    public String getDissertationThesisTitle() {
        return hasDissertationThesis() ? getRegistration().getDissertationThesisTitle() : null;
    }

    public LocalDate getDissertationThesisDiscussedDate() {
        return hasDissertationThesis() ? getRegistration().getDissertationThesisDiscussedDate() : null;
    }

    public double getEctsCredits() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getCreditsConcluded();
        }

        return calculateCredits();
    }

    public double calculateCredits() {
        return getCurriculumGroup().calculateCreditsConcluded();
    }

    public double getCalculatedEctsCredits() {
        return calculateCredits();
    }

    public ICurriculum getCurriculumForConclusion() {
        return getCurriculumGroup().getCurriculum();
    }

    public int getCurriculumEntriesSize() {
        return getCurriculumForConclusion().getCurriculumEntries().size();
    }

    public String getConclusionDegreeDescription() {
        return getRegistration().getDegreeDescription(getConclusionYear(), getProgramConclusion(), I18N.getLocale());
    }

    public boolean isConcluded() {
        return getCurriculumGroup().isConcluded();
    }

    public Collection<CurriculumModule> getCurriculumModulesWithNoConlusionDate() {
        final Collection<CurriculumModule> result = new HashSet<CurriculumModule>();
        if (isByGroup()) {
            getCurriculumGroup().assertConclusionDate(result);
        } else {
            getRegistration().assertConclusionDate(result);
        }
        return result;
    }

    public Collection<CurriculumGroup> getCurriculumGroupsNotVerifyingStructure() {
        if (isByGroup()) {
            final Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
            if (!getCurriculumGroup().isSkipConcluded()) {
                getCurriculumGroup().assertCorrectStructure(result, getConclusionYear());
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public boolean isConclusionProcessed() {
        return getCurriculumGroup().isConclusionProcessed();
    }

    public boolean getCanBeConclusionProcessed() {
        return (!isConclusionProcessed() || (isConclusionProcessed() && getRegistration().canRepeatConclusionProcess(
                AccessControl.getPerson())))
                && isConcluded();
    }

    public boolean getCanRepeatConclusionProcess() {
        return getRegistration().canRepeatConclusionProcess(AccessControl.getPerson());
    }

    public ConclusionProcess getConclusionProcess() {
        return getCurriculumGroup().getConclusionProcess();
    }

    public boolean isByGroup() {
        return hasCurriculumGroup();
    }

    public String getConclusionProcessNotes() {
        return getCurriculumGroup().getConclusionProcessNotes();
    }

    public Person getConclusionProcessResponsible() {
        return getCurriculumGroup().getConclusionProcessResponsible();
    }

    public Person getConclusionProcessLastResponsible() {
        return getCurriculumGroup().getConclusionProcessLastResponsible();
    }

    public DateTime getConclusionProcessCreationDateTime() {
        return getCurriculumGroup().getConclusionProcessCreationDateTime();
    }

    public DateTime getConclusionProcessLastModificationDateTime() {
        return getCurriculumGroup().getConclusionProcessLastModificationDateTime();
    }

    public boolean isSkipValidation() {
        return getProgramConclusion() != null && getProgramConclusion().isSkipValidation();
    }

    public Boolean getHasAccessToRegistrationConclusionProcess() {
        return hasAccessToRegistrationConclusionProcess;
    }

    public void setHasAccessToRegistrationConclusionProcess(Boolean hasAccessToRegistrationConclusionProcess) {
        this.hasAccessToRegistrationConclusionProcess = hasAccessToRegistrationConclusionProcess;
    }

    public LocalDate getEnteredConclusionDate() {
        return enteredConclusionDate;
    }

    public boolean hasEnteredConclusionDate() {
        return getEnteredConclusionDate() != null;
    }

    public void setEnteredConclusionDate(LocalDate enteredConclusionDate) {
        this.enteredConclusionDate = enteredConclusionDate;
    }

    public Integer getEnteredFinalAverageGrade() {
        return this.enteredFinalAverageGrade;
    }

    public void setEnteredFinalAverageGrade(final Integer value) {
        this.enteredFinalAverageGrade = value;
    }

    public boolean hasEnteredFinalAverageGrade() {
        return this.enteredFinalAverageGrade != null;
    }

    public Double getEnteredAverageGrade() {
        return this.enteredAverageGrade;
    }

    public void setEnteredAverageGrade(final Double averageGrade) {
        this.enteredAverageGrade = averageGrade;
    }

    public boolean hasEnteredAverageGrade() {
        return this.enteredAverageGrade != null;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

}
