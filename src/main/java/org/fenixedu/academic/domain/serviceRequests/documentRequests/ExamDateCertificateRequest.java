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
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Collection;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.util.Season;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

    protected ExamDateCertificateRequest() {
        super();
    }

    public ExamDateCertificateRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        checkRulesToCreate(bean);
        super.getEnrolmentsSet().addAll(bean.getEnrolments());
        super.getExamsSet().addAll(bean.getExams());
        super.setExecutionPeriod(bean.getExecutionPeriod());
    }

    private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
        for (final Exam exam : bean.getExams()) {
            if (exam.isForSeason(Season.SPECIAL_SEASON_OBJ)
                    && !getEnrolmentFor(bean.getEnrolments(), exam).isSpecialSeasonEnroled(
                            bean.getExecutionPeriod().getExecutionYear())) {

                throw new DomainExceptionWithLabelFormatter(
                        "error.serviceRequests.documentRequests.ExamDateCertificateRequest.special.season.exam.requires.student.to.be.enroled",
                        exam.getSeason().getDescription());
            }
        }
    }

    private Enrolment getEnrolmentFor(final Collection<Enrolment> enrolments, final Exam exam) {
        for (final Enrolment enrolment : enrolments) {
            if (exam.contains(enrolment.getCurricularCourse())) {
                return enrolment;
            }
        }

        throw new DomainException(
                "error.serviceRequests.documentRequests.ExamDateCertificateRequest.each.exam.must.belong.to.at.least.one.enrolment");

    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getExecutionYear() == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.executionYear.cannot.be.null");
        }

        if (bean.getEnrolments() == null || bean.getEnrolments().isEmpty()) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.enrolments.cannot.be.null.and.must.have.size.greater.than.zero");
        }

        if (bean.getExecutionPeriod() == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.serviceRequests.documentRequests.ExamDateCertificateRequest.executionPeriod.cannot.be.null");
        }

    }

    @Override
    public Integer getNumberOfUnits() {
        return 0;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.EXAM_DATE_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    public EventType getEventType() {
        return EventType.EXAM_DATE_CERTIFICATE_REQUEST;
    }

    public Exam getExamFor(final Enrolment enrolment, final Season season) {
        for (final Exam exam : getExamsSet()) {
            if (exam.contains(enrolment.getCurricularCourse()) && exam.isForSeason(season)) {
                return exam;
            }
        }

        return null;
    }

    @Override
    public boolean isFree() {
        return getRegistration().getRegistrationProtocol().isMilitaryAgreement() || super.isFree();
    }

}
