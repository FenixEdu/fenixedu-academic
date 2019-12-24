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
package org.fenixedu.academic.dto.administrativeOffice.externalUnits;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.Grade;
import org.joda.time.YearMonthDay;

public class CreateExternalEnrolmentBean implements Serializable {

    private ExternalCurricularCourse externalCurricularCourse;
    private ExecutionInterval executionSemester;

    private Integer studentNumber;
    private Grade grade;
    private YearMonthDay evaluationDate;
    private Double ectsCredits;

    public CreateExternalEnrolmentBean() {
    }

    public CreateExternalEnrolmentBean(final ExternalCurricularCourse externalCurricularCourse) {
        setExternalCurricularCourse(externalCurricularCourse);
    }

    public ExternalCurricularCourse getExternalCurricularCourse() {
        return this.externalCurricularCourse;
    }

    public void setExternalCurricularCourse(ExternalCurricularCourse externalCurricularCourse) {
        this.externalCurricularCourse = externalCurricularCourse;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public ExecutionInterval getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionInterval executionSemester) {
        this.executionSemester = executionSemester;
    }

    public YearMonthDay getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(YearMonthDay evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Double getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }
}
