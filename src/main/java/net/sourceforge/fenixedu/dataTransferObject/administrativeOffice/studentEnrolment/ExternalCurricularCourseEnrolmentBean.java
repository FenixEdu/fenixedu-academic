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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.ExternalCurricularCourseResultBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;
import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.YearMonthDay;

public class ExternalCurricularCourseEnrolmentBean implements Serializable {

    private ExternalCurricularCourseResultBean externalCurricularCourseResultBean;
    private ExecutionSemester executionSemester;
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

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
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
