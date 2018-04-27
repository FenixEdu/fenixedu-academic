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
import java.util.HashSet;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    protected EnrolmentCertificateRequest() {
        super();
    }

    public EnrolmentCertificateRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getDetailed() == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.detailed.cannot.be.null");
        }
        if (bean.getExecutionYear() == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");
        } else if (!bean.getRegistration().hasAnyEnrolmentsIn(bean.getExecutionYear())) {
            throw new DomainException("EnrolmentCertificateRequest.no.enrolments.for.registration.in.given.executionYear");
        }
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.ENROLMENT_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public void setDetailed(Boolean detailed) {
        throw new DomainException("error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.detailed");
    }

    @Override
    final public EventType getEventType() {
        return EventType.ENROLMENT_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
        return getEntriesToReport().size() + getExtraCurricularEntriesToReport().size() + getPropaedeuticEntriesToReport().size();
    }

    final public Collection<Enrolment> getEntriesToReport() {
        return getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear());
    }

    final public Collection<Enrolment> getExtraCurricularEntriesToReport() {
        final Collection<Enrolment> extraCurricular = new HashSet<Enrolment>();
        for (final Enrolment entry : getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear())) {
            if (entry.isExtraCurricular() && entry.getEnrolmentWrappersSet().isEmpty()) {
                extraCurricular.add(entry);
            }
        }
        return extraCurricular;
    }

    final public Collection<Enrolment> getPropaedeuticEntriesToReport() {
        final Collection<Enrolment> propaedeutic = new HashSet<Enrolment>();
        for (final Enrolment entry : getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear())) {
            if (!(entry.isExtraCurricular() && entry.getEnrolmentWrappersSet().isEmpty()) && entry.isPropaedeutic()) {
                propaedeutic.add(entry);
            }
        }
        return propaedeutic;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return true;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    public CertificateRequestEvent getEvent() {
        return (CertificateRequestEvent) super.getEvent();
    }

}
