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
package org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;
import java.util.Date;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class MarkSheetEnrolmentEvaluationBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String gradeValue;
    private Date evaluationDate;
    private Enrolment enrolment;
    private String bookReference;
    private String page;
    private String currentGrade;

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    // used to edit
    private EnrolmentEvaluation enrolmentEvaluation;

    private ExecutionSemester executionSemester;

    private Boolean enrolmentEvaluationSet;

    private EvaluationSeason evaluationSeason;

    private GradeScale gradeScale;

    private Double weight;

    public MarkSheetEnrolmentEvaluationBean() {
    }

    public MarkSheetEnrolmentEvaluationBean(Enrolment enrolment, Date evaluationDate, Grade grade) {
        setEnrolment(enrolment);
        setEvaluationDate(evaluationDate);
        setGradeValue(grade.getValue());
        setWeight(enrolment.getWeigth());
    }

    public MarkSheetEnrolmentEvaluationBean(Enrolment enrolment, ExecutionSemester executionSemester, EvaluationSeason season) {
        this.setExecutionSemester(executionSemester);
        this.setEnrolment(enrolment);
        this.setEvaluationSeason(season);
        setWeight(enrolment.getWeigth());

        if (this.getHasGrade()) {
            EnrolmentEvaluation enrolmentEvaluation = getLatestEnrolmentEvaluation();
            Grade grade = enrolmentEvaluation.getGrade();

            this.gradeValue = grade.getValue();
            this.evaluationDate = enrolmentEvaluation.getExamDate();
            this.bookReference = enrolmentEvaluation.getBookReference();
            this.page = enrolmentEvaluation.getPage();
            this.gradeScale = enrolmentEvaluation.getAssociatedGradeScale();
        }

        this.enrolmentEvaluationSet = null;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Enrolment getEnrolment() {
        return this.enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String grade) {
        this.gradeValue = grade;
    }

    public boolean hasAnyGradeValue() {
        return getGradeValue() != null && getGradeValue().length() != 0;
    }

    public EnrolmentEvaluation getEnrolmentEvaluation() {
        return this.enrolmentEvaluation;
    }

    public void setEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation) {
        this.enrolmentEvaluation = enrolmentEvaluation;
        if (this.enrolmentEvaluation != null) {
            setEnrolment(this.enrolmentEvaluation.getEnrolment());
        }
    }

    public String getBookReference() {
        return bookReference;
    }

    public void setBookReference(String bookReference) {
        this.bookReference = bookReference;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester != null ? this.executionSemester : null;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public Boolean getHasGrade() {
        return getLatestEnrolmentEvaluation() != null;
    }

    public Boolean getHasFinalGrade() {
        return getHasGrade() && getLatestEnrolmentEvaluation().isFinal();
    }

    public String getName() {
        return getEnrolment().getName().getContent();
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluation() {
        return getEnrolment().getLatestEnrolmentEvaluationBySeason(getEvaluationSeason());
    }

    public String getExecutionYearFullLabel() {
        return getEnrolment().getExecutionPeriod().getExecutionYear().getYear();
    }

    public String getExecutionSemesterFullLabel() {
        return String.format("%s %s", getEnrolment().getExecutionPeriod().getSemester().toString(),
                BundleUtil.getString(Bundle.ENUMERATION, "SEMESTER.ABBREVIATION"));
    }

    public String getEnrolmentState() {
        return this.getEnrolment().getEnrollmentState().getDescription();
    }

    public String getEnrolmentCondition() {
        return this.getEnrolment().getEnrolmentCondition().getDescription();
    }

    public void setEnrolmentEvaluationSet(Boolean value) {
        this.enrolmentEvaluationSet = value;
    }

    public Boolean getEnrolmentEvaluationSet() {
        return this.enrolmentEvaluationSet;
    }

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

    public GradeScale getGradeScale() {
        return this.gradeScale;
    }

    public void setGradeScale(final GradeScale value) {
        this.gradeScale = value;
    }

    public boolean isEnrolmentBeMarkedAsEnroled() {
        return this.getEnrolment().getEvaluationsSet().isEmpty();
    }

    public boolean isPossibleToUnEnrolEnrolment() {
        return this.getEnrolment().getEvaluationsSet().isEmpty()
                && this.getEnrolment().getEnrollmentState().equals(EnrollmentState.ENROLLED);
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
