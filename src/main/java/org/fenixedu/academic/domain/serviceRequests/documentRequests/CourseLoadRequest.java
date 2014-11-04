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

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

public class CourseLoadRequest extends CourseLoadRequest_Base {

    protected CourseLoadRequest() {
        super();
        setNumberOfPages(0);
    }

    public CourseLoadRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.getEnrolmentsSet().addAll(bean.getEnrolments());
        super.setRequestedCycle(bean.getRequestedCycle());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getEnrolments().isEmpty()) {
            throw new DomainException("error.CourseLoadRequest.invalid.number.of.enrolments");
        }

        for (final Enrolment enrolment : bean.getEnrolments()) {
            if (!enrolment.isApproved()) {
                throw new DomainException("error.CourseLoadRequest.cannot.add.not.approved.enrolments");
            }
            if (!getStudent().hasEnrolments(enrolment)) {
                throw new DomainException("error.ProgramCertificateRequest.enrolment.doesnot.belong.to.student");
            }
        }
    }

    @Override
    public Integer getNumberOfUnits() {
        return getEnrolmentsSet().size();
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.COURSE_LOAD;
    }

    @Override
    public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    public EventType getEventType() {
        return EventType.COURSE_LOAD_REQUEST;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToCancelOrReject()) {
            for (; !getEnrolmentsSet().isEmpty();) {
                removeEnrolments(getEnrolmentsSet().iterator().next());
            }
        }
    }

    @Override
    protected void disconnect() {
        super.getEnrolmentsSet().clear();
        super.disconnect();
    }

    @Override
    public boolean isFree() {
        return getRegistration().getRegistrationProtocol().isMilitaryAgreement() || super.isFree();
    }

}
