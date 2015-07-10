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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.treasury.IAcademicTreasuryEvent;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

abstract public class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
        super();
        super.setNumberOfPages(0);
    }

    final protected void init(final DocumentRequestCreateBean bean) {
        super.init(bean);

        super.checkParameters(bean);
        super.setDocumentPurposeTypeInstance(bean.getChosenDocumentPurposeType());
        super.setDocumentPurposeType(getDocumentPurposeTypeInstance() != null ? getDocumentPurposeTypeInstance()
                .getDocumentPurposeType() : null);
        super.setOtherDocumentPurposeTypeDescription(bean.getOtherPurpose());
    }

    static final public CertificateRequest create(final DocumentRequestCreateBean bean) {

        CertificateRequest certificateRequest = null;
        final DocumentRequestType requestType = bean.getChosenServiceRequestType().getDocumentRequestType();

        switch (requestType) {
        case SCHOOL_REGISTRATION_CERTIFICATE:
            certificateRequest = new SchoolRegistrationCertificateRequest(bean);
            break;

        case ENROLMENT_CERTIFICATE:
            certificateRequest = new EnrolmentCertificateRequest(bean);
            break;

        case APPROVEMENT_CERTIFICATE:
            certificateRequest = new ApprovementCertificateRequest(bean);
            break;

        case APPROVEMENT_MOBILITY_CERTIFICATE:
            certificateRequest = new ApprovementMobilityCertificateRequest(bean);
            break;

        case DEGREE_FINALIZATION_CERTIFICATE:
            certificateRequest = new DegreeFinalizationCertificateRequest(bean);
            break;

        case EXAM_DATE_CERTIFICATE:
            certificateRequest = new ExamDateCertificateRequest(bean);
            break;

        case COURSE_LOAD:
            certificateRequest = new CourseLoadRequest(bean);
            break;

        case EXTERNAL_COURSE_LOAD:
            certificateRequest = new ExternalCourseLoadRequest(bean);
            break;

        case PROGRAM_CERTIFICATE:
            certificateRequest = new ProgramCertificateRequest(bean);
            break;

        case EXTERNAL_PROGRAM_CERTIFICATE:
            certificateRequest = new ExternalProgramCertificateRequest(bean);
            break;

        case EXTRA_CURRICULAR_CERTIFICATE:
            certificateRequest = new ExtraCurricularCertificateRequest(bean);
            break;

        case STANDALONE_ENROLMENT_CERTIFICATE:
            certificateRequest = new StandaloneEnrolmentCertificateRequest(bean);
            break;

        }

        if (certificateRequest == null) {
            throw new DomainException("error.CertificateRequest.unexpected.document.type");
        }

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(certificateRequest));

        return certificateRequest;

    }

    @Override
    final public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
        throw new DomainException("error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.documentPurposeType");
    }

    @Override
    final public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.otherDocumentTypeDescription");
    }

    final public void edit(final DocumentRequestBean certificateRequestBean) {

        final IAcademicTreasuryEvent event =
                TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(this);

        if (isPayable() && event != null && getNumberOfPages() == certificateRequestBean.getNumberOfPages()) {
            throw new DomainException("error.serviceRequests.documentRequests.cannot.change.numberOfPages.on.payed.documents");
        }

        super.edit(certificateRequestBean);
        super.setNumberOfPages(certificateRequestBean.getNumberOfPages());
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {

        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude()) {
            tryConcludeServiceRequest(academicServiceRequestBean);
        }
    }

    protected void tryConcludeServiceRequest(final AcademicServiceRequestBean academicServiceRequestBean) {
        if (!hasNumberOfPages()) {
            throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
        }
    }

    protected void createCertificateRequestEvent() {
        // ANIL
        // new CertificateRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    public boolean isFree() {
        if (getDocumentRequestType() == DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE
                && getRegistration().getRegistrationProtocol().isMobilityAgreement()) {
            return true;
        }
        if (getDocumentRequestType() == DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE
                || getDocumentRequestType() == DocumentRequestType.ENROLMENT_CERTIFICATE) {
            return super.isFree() || (!isRequestForPreviousExecutionYear() && isFirstRequestOfCurrentExecutionYear());
        }

        return super.isFree();
    }

    @Override
    public boolean isPayedUponCreation() {
        return false;
    }

    private boolean isRequestForPreviousExecutionYear() {
        return getExecutionYear() != ExecutionYear.readCurrentExecutionYear();
    }

    private boolean isFirstRequestOfCurrentExecutionYear() {
        return getRegistration().getSucessfullyFinishedDocumentRequestsBy(ExecutionYear.readCurrentExecutionYear(),
                getDocumentRequestType(), false).isEmpty();
    }

    @Override
    public boolean isPagedDocument() {
        return true;
    }

    @Override
    public boolean isToPrint() {
        return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return false;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

}
