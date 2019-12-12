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
package org.fenixedu.academic.domain.log;

import java.util.Locale;

import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class EnrolmentEvaluationLog extends EnrolmentEvaluationLog_Base {

    EnrolmentEvaluationLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    EnrolmentEvaluationLog(final EnrolmentEvaluation enrolmentEvaluation) {
        this();

        init(enrolmentEvaluation);
        setWho(getCurrentUser());
    }

    void init(final EnrolmentEvaluation enrolmentEvaluation) {
        setCurricularCourse(String.format("externalId: %s; code: %s; name %s",
                enrolmentEvaluation.getEnrolment().getCurricularCourse().getExternalId(),
                enrolmentEvaluation.getEnrolment().getCurricularCourse().getCode(),
                enrolmentEvaluation.getEnrolment().getCurricularCourse().getName()));

        setGradeValue(enrolmentEvaluation.getGradeValue());

        final Grade grade = enrolmentEvaluation.getGrade();
        setGradeScale(grade != null && grade.getGradeScale() != null ? grade.getGradeScale().getCode() : "");
        setEvaluationSeason(enrolmentEvaluation.getEvaluationSeason().getName().getContent(Locale.getDefault()));
        setEnrolmentEvaluationState(enrolmentEvaluation.getEnrolmentEvaluationState() != null ? enrolmentEvaluation
                .getEnrolmentEvaluationState().getState().toString() : "");
        setExecutionSemester(enrolmentEvaluation.getEnrolment().getExecutionInterval().getName());
        setExamDate(enrolmentEvaluation.getExamDateYearMonthDay() != null ? enrolmentEvaluation.getExamDateYearMonthDay()
                .toString("dd/MM/yyyy") : "");
        setEnrolmentEvaluationResponsible(
                enrolmentEvaluation.getPerson() != null ? enrolmentEvaluation.getPerson().getUsername() : "");
        setBook(enrolmentEvaluation.getBookReference());
        setPage(enrolmentEvaluation.getPage());
        setActionDate(new DateTime().toString());
    }

    public static void logEnrolmentEvaluationCreation(final EnrolmentEvaluation enrolmentEvaluation) {
        EnrolmentEvaluationLog log = new EnrolmentEvaluationLog(enrolmentEvaluation);
        log.setAction("create");
    }

    public static void logEnrolmentEvaluationDeletion(final EnrolmentEvaluation enrolmentEvaluation) {
        EnrolmentEvaluationLog log = new EnrolmentEvaluationLog(enrolmentEvaluation);
        log.setAction("delete");
    }

    protected String getCurrentUser() {
        return Authenticate.getUser() != null ? Authenticate.getUser().getUsername() : null;
    }

}
