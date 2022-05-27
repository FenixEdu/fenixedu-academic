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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
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

import com.google.common.base.Strings;

public class RegistrationConclusionBean implements Serializable, IRegistrationBean {

    private static final long serialVersionUID = 5825221957160251388L;

    private Registration registration;

    private ProgramConclusion programConclusion;

    private Boolean hasAccessToRegistrationConclusionProcess = Boolean.TRUE;

    private LocalDate enteredConclusionDate;

    private String enteredFinalAverageGrade;

    private String enteredAverageGrade;

    private String enteredDescriptiveGrade;

    private String observations;

    private StudentCurricularPlan studentCurricularPlan;

    protected RegistrationConclusionBean() {
        super();
    }

    public RegistrationConclusionBean(final StudentCurricularPlan studentCurricularPlan,
            final ProgramConclusion programConclusion) {
        setStudentCurricularPlan(studentCurricularPlan);
        setRegistration(studentCurricularPlan.getRegistration());
        setProgramConclusion(programConclusion);
    }

    public CurriculumGroup getCurriculumGroup() {
        if (getProgramConclusion() == null) {
            return null;
        }

        if (getStudentCurricularPlan() != null && getProgramConclusion().groupFor(studentCurricularPlan).isPresent()) {
            return getProgramConclusion().groupFor(studentCurricularPlan).get();
        }

        return getProgramConclusion() == null ? null : getProgramConclusion().groupFor(getRegistration()).orElse(null);
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

    public Grade getFinalGrade() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getFinalGrade();
        }

        return getCalculatedFinalGrade();
    }

    public Grade getCalculatedFinalGrade() {
        return getCurriculumGroup().calculateFinalGrade();
    }

    public Grade getRawGrade() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getRawGrade();
        }

        return getCalculatedRawGrade();
    }

    public Grade getCalculatedRawGrade() {
        return getCurriculumGroup().calculateRawGrade();
    }

    public YearMonthDay getConclusionDate() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getConclusionDate();
        }

        return calculateConclusionDate();
    }

    public YearMonthDay calculateConclusionDate() {
        return getCurriculumForConclusion().getLastApprovementDate();
    }

    public YearMonthDay getCalculatedConclusionDate() {
        return calculateConclusionDate();
    }

    public Grade getDescriptiveGrade() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getDescriptiveGrade();
        }

        return null;
    }

    public String getDescriptiveGradeExtendedValue() {
        return getDescriptiveGrade() == null ? null : getDescriptiveGrade().getExtendedValue().getContent();
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

        return getCalculatedConclusionYear();
    }

    public ExecutionYear calculateConclusionYear() {
        return getCurriculumForConclusion().getLastExecutionYear();
    }

    public ExecutionYear getCalculatedConclusionYear() {
        return calculateConclusionYear();
    }

    public double getEctsCredits() {
        if (isConclusionProcessed()) {
            return getCurriculumGroup().getCreditsConcluded();
        }

        return calculateCredits();
    }

    public double calculateCredits() {
        return getCurriculumForConclusion().getSumEctsCredits().doubleValue();
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
        return (!isConclusionProcessed()
                || (isConclusionProcessed() && getRegistration().canRepeatConclusionProcess(AccessControl.getPerson())))
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

    public String getEnteredFinalAverageGrade() {
        return this.enteredFinalAverageGrade;
    }

    public void setEnteredFinalAverageGrade(final String value) {
        this.enteredFinalAverageGrade = value;
    }

    public boolean hasEnteredFinalAverageGrade() {
        return !Strings.isNullOrEmpty(this.enteredFinalAverageGrade);
    }

    public String getEnteredAverageGrade() {
        return this.enteredAverageGrade;
    }

    public void setEnteredAverageGrade(final String averageGrade) {
        this.enteredAverageGrade = averageGrade;
    }

    public boolean hasEnteredAverageGrade() {
        return !Strings.isNullOrEmpty(this.enteredAverageGrade);
    }

    public String getEnteredDescriptiveGrade() {
        return enteredDescriptiveGrade;
    }

    public void setEnteredDescriptiveGrade(String enteredDescriptiveGrade) {
        this.enteredDescriptiveGrade = enteredDescriptiveGrade;
    }

    public boolean hasEnteredDescriptiveGrade() {
        return !Strings.isNullOrEmpty(this.enteredDescriptiveGrade);
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }
}
