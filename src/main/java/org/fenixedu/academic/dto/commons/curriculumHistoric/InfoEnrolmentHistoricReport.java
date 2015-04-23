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
package org.fenixedu.academic.dto.commons.curriculumHistoric;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class InfoEnrolmentHistoricReport implements Serializable {

    private Enrolment enrolment;

    public Enrolment getEnrolment() {
        return this.enrolment;
    }

    private void setEnrolment(final Enrolment enrolment) {
        this.enrolment = enrolment;

    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return getEnrolment().getStudentCurricularPlan();
    }

    public InfoEnrolmentHistoricReport(final Enrolment enrolment) {
        setEnrolment(enrolment);
    }

    public List<String> getEvaluationGrades() {
        return EvaluationSeason.all().sorted().map(s -> enrolment.getLatestEnrolmentEvaluationBySeason(s))
                .map(InfoEnrolmentHistoricReport::printGrade).collect(Collectors.toList());
    }

    private static String printGrade(final EnrolmentEvaluation evaluation) {
        if (evaluation == null) {
            return "--";
        }

        final Grade grade = evaluation.getGrade();
        if (!evaluation.isFinal()) {
            return BundleUtil.getString(Bundle.ENUMERATION, "msg.enrolled");
        } else if (grade.isEmpty() || grade.isNotEvaluated()) {
            return BundleUtil.getString(Bundle.ENUMERATION, "msg.notEvaluated");
        } else if (grade.isNotApproved()) {
            return BundleUtil.getString(Bundle.ENUMERATION, "msg.notApproved");
        } else if (!grade.isNumeric() && grade.isApproved()) {
            return BundleUtil.getString(Bundle.ENUMERATION, "msg.approved");
        } else {
            return grade.getValue();
        }
    }

    public String getLatestEnrolmentEvaluationInformation() {
        if (getEnrolment().isApproved()) {
            final Grade grade = getEnrolment().getGrade();

            if (grade.getGradeScale() == GradeScale.TYPEAP) {
                return BundleUtil.getString(Bundle.ENUMERATION, "msg.approved");
            } else {
                return grade.getValue();
            }
        } else {
            return BundleUtil.getString(Bundle.ENUMERATION, getEnrolment().getEnrollmentState().name());
        }
    }

}
