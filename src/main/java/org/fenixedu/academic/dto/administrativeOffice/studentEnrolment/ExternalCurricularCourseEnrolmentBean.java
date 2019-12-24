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
package org.fenixedu.academic.dto.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExternalCurricularCourse;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.dto.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import org.joda.time.YearMonthDay;

public class ExternalCurricularCourseEnrolmentBean implements Serializable {

    private ExternalCurricularCourseResultBean externalCurricularCourseResultBean;
    private ExecutionInterval executionInterval;
    private YearMonthDay evaluationDate;
    private Grade grade;
    private Double ectsCredits;

    public ExternalCurricularCourseEnrolmentBean(final ExternalCurricularCourse externalCurricularCourse) {
        setExternalCurricularCourseResultBean(new ExternalCurricularCourseResultBean(externalCurricularCourse));
    }

    public ExternalCurricularCourseResultBean getExternalCurricularCourseResultBean() {
        return externalCurricularCourseResultBean;
    }

    public void setExternalCurricularCourseResultBean(ExternalCurricularCourseResultBean externalCurricularCourseResultBean) {
        this.externalCurricularCourseResultBean = externalCurricularCourseResultBean;
    }

    public ExecutionInterval getExecutionPeriod() {
        return this.executionInterval;
    }

    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public ExternalCurricularCourse getExternalCurricularCourse() {
        return getExternalCurricularCourseResultBean().getExternalCurricularCourse();
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
