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
package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.util.Season;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

    static public final List<RegistrationAgreement> FREE_PAYMENT_AGREEMENTS = Arrays.asList(RegistrationAgreement.AFA,
            RegistrationAgreement.MA);

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
                    "error.net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExamDateCertificateRequest.executionPeriod.cannot.be.null");
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
        for (final Exam exam : getExams()) {
            if (exam.contains(enrolment.getCurricularCourse()) && exam.isForSeason(season)) {
                return exam;
            }
        }

        return null;
    }

    @Override
    public boolean isFree() {
        if (FREE_PAYMENT_AGREEMENTS.contains(getRegistration().getRegistrationAgreement())) {
            return true;
        }
        return super.isFree();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Exam> getExams() {
        return getExamsSet();
    }

    @Deprecated
    public boolean hasAnyExams() {
        return !getExamsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Enrolment> getEnrolments() {
        return getEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolments() {
        return !getEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

}
