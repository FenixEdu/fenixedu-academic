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

import java.util.Date;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;

import pt.ist.fenixframework.Atomic;

public class MarkSheetRectifyBean extends MarkSheetManagementBaseBean {

    private MarkSheet markSheet;
    private EnrolmentEvaluation enrolmentEvaluation;
    private EvaluationSeason evaluationSeason;

    private Integer studentNumber;
    private String newGrade;
    private Date evaluationDate;
    private String reason;

    public MarkSheet getMarkSheet() {
        return this.markSheet;
    }

    public void setMarkSheet(MarkSheet markSheet) {
        this.markSheet = markSheet;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Grade getRectifiedGrade() {
        return Grade.createGrade(getNewGrade(), getEnrolmentEvaluation().getGradeScale());
    }

    public String getNewGrade() {
        return newGrade;
    }

    public void setNewGrade(String newGrade) {
        this.newGrade = newGrade;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public EnrolmentEvaluation getEnrolmentEvaluation() {
        return this.enrolmentEvaluation;
    }

    public void setEnrolmentEvaluation(EnrolmentEvaluation enrolmentEvaluation) {
        this.enrolmentEvaluation = enrolmentEvaluation;
    }

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

    @Atomic
    public MarkSheet createRectificationOldMarkSheet(Person person) throws InvalidArgumentsServiceException {
        if (getEnrolmentEvaluation() == null) {
            throw new InvalidArgumentsServiceException();
        }
        return getEnrolmentEvaluation()
                .getEnrolment()
                .getCurricularCourse()
                .rectifyOldEnrolmentEvaluation(getEnrolmentEvaluation(), getEvaluationSeason(), getEvaluationDate(),
                        getRectifiedGrade(), getReason(), person);
    }
}
