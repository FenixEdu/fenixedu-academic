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
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.curriculum.EnrollmentState;

public class InfoEnrolment extends InfoObject {

    private Enrolment enrolment;

    protected InfoEnrolment() {
    }

    protected InfoEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        return (enrolment != null) ? new InfoEnrolment(enrolment) : null;
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    @Override
    public String getExternalId() {
        return enrolment.getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public String getEnrollmentTypeResourceKey() {
        return enrolment.getEnrolmentTypeName();
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return InfoCurricularCourse.newInfoFromDomain(enrolment.getCurricularCourse());
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return InfoExecutionPeriod.newInfoFromDomain(enrolment.getExecutionInterval());
    }

    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return InfoStudentCurricularPlan.newInfoFromDomain(enrolment.getStudentCurricularPlan());
    }

    public EnrollmentState getEnrollmentState() {
        return enrolment.getEnrollmentState();
    }

    public List<InfoEnrolmentEvaluation> getInfoEvaluations() {
        final List<InfoEnrolmentEvaluation> result = new ArrayList<InfoEnrolmentEvaluation>(enrolment.getEvaluationsSet().size());
        for (final EnrolmentEvaluation enrolmentEvaluation : enrolment.getEvaluationsSet()) {
            result.add(InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolmentEvaluation));
        }
        return result;
    }

    public InfoEnrolmentEvaluation getInfoEnrolmentEvaluation() {
        return InfoEnrolmentEvaluationWithResponsibleForGrade.newInfoFromDomain(enrolment.getFinalEnrolmentEvaluation());
    }

    public Date getCreationDate() {
        return enrolment.getCreationDateDateTime().toDate();
    }

    public EnrollmentCondition getCondition() {
        return enrolment.getEnrolmentCondition();
    }

    public String getGradeValue() {
        return enrolment.getGradeValue();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof InfoEnrolment) ? this.enrolment == ((InfoEnrolment) obj).getEnrolment() : false;
    }

    @Override
    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "infoStudentCurricularPlan = " + getInfoStudentCurricularPlan() + "; ";
        result += "infoExecutionPeriod = " + getInfoExecutionPeriod() + "; ";
        result += "state = " + getEnrollmentState() + "; ";
        result += "infoCurricularCourse = " + getInfoCurricularCourse() + "; ";
        result += "season = " + enrolment.getEvaluationSeason().getName().getContent() + "; ";
        result += "infoEvaluations = " + getInfoEvaluations() + "]\n";
        return result;
    }
}
