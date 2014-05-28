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
package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

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
        setCurricularCourse(String.format("externalId: %s; code: %s; name %s", enrolmentEvaluation.getEnrolment()
                .getCurricularCourse().getExternalId(), enrolmentEvaluation.getEnrolment().getCurricularCourse().getCode(),
                enrolmentEvaluation.getEnrolment().getCurricularCourse().getName()));

        setGradeValue(enrolmentEvaluation.getGradeValue());
        setGradeScale(enrolmentEvaluation.getGradeScale() != null ? enrolmentEvaluation.getGradeScale().getName() : "");
        setEnrolmentEvaluationType(enrolmentEvaluation.getEnrolmentEvaluationType().getName());
        setEnrolmentEvaluationState(enrolmentEvaluation.getEnrolmentEvaluationState() != null ? enrolmentEvaluation
                .getEnrolmentEvaluationState().getState().toString() : "");
        setExecutionSemester(enrolmentEvaluation.getEnrolment().getExecutionPeriod().getName());
        setExamDate(enrolmentEvaluation.getExamDateYearMonthDay() != null ? enrolmentEvaluation.getExamDateYearMonthDay()
                .toString("dd/MM/yyyy") : "");
        setEnrolmentEvaluationResponsible(enrolmentEvaluation.getPerson() != null ? enrolmentEvaluation.getPerson().getUsername() : "");
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

    @Deprecated
    public boolean hasBook() {
        return getBook() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGradeScale() {
        return getGradeScale() != null;
    }

    @Deprecated
    public boolean hasExamDate() {
        return getExamDate() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationType() {
        return getEnrolmentEvaluationType() != null;
    }

    @Deprecated
    public boolean hasActionDate() {
        return getActionDate() != null;
    }

    @Deprecated
    public boolean hasGradeValue() {
        return getGradeValue() != null;
    }

    @Deprecated
    public boolean hasAction() {
        return getAction() != null;
    }

    @Deprecated
    public boolean hasWho() {
        return getWho() != null;
    }

    @Deprecated
    public boolean hasPage() {
        return getPage() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationState() {
        return getEnrolmentEvaluationState() != null;
    }

    @Deprecated
    public boolean hasEnrolmentEvaluationResponsible() {
        return getEnrolmentEvaluationResponsible() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
