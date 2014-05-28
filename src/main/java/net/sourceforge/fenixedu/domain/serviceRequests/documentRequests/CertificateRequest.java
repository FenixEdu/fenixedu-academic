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

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

abstract public class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
        super();
        super.setNumberOfPages(0);
    }

    final protected void init(final DocumentRequestCreateBean bean) {
        super.init(bean);

        super.checkParameters(bean);
        super.setDocumentPurposeType(bean.getChosenDocumentPurposeType());
        super.setOtherDocumentPurposeTypeDescription(bean.getOtherPurpose());
    }

    static final public CertificateRequest create(final DocumentRequestCreateBean bean) {
        switch (bean.getChosenDocumentRequestType()) {
        case SCHOOL_REGISTRATION_CERTIFICATE:
            return new SchoolRegistrationCertificateRequest(bean);

        case ENROLMENT_CERTIFICATE:
            return new EnrolmentCertificateRequest(bean);

        case APPROVEMENT_CERTIFICATE:
            return new ApprovementCertificateRequest(bean);

        case APPROVEMENT_MOBILITY_CERTIFICATE:
            return new ApprovementMobilityCertificateRequest(bean);

        case DEGREE_FINALIZATION_CERTIFICATE:
            return new DegreeFinalizationCertificateRequest(bean);

        case EXAM_DATE_CERTIFICATE:
            return new ExamDateCertificateRequest(bean);
        case COURSE_LOAD:
            return new CourseLoadRequest(bean);

        case EXTERNAL_COURSE_LOAD:
            return new ExternalCourseLoadRequest(bean);

        case PROGRAM_CERTIFICATE:
            return new ProgramCertificateRequest(bean);

        case EXTERNAL_PROGRAM_CERTIFICATE:
            return new ExternalProgramCertificateRequest(bean);

        case EXTRA_CURRICULAR_CERTIFICATE:
            return new ExtraCurricularCertificateRequest(bean);

        case STANDALONE_ENROLMENT_CERTIFICATE:
            return new StandaloneEnrolmentCertificateRequest(bean);

        }

        throw new DomainException("error.CertificateRequest.unexpected.document.type");
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

    abstract public Integer getNumberOfUnits();

    final public void edit(final DocumentRequestBean certificateRequestBean) {

        if (isPayable() && isPayed() && !getNumberOfPages().equals(certificateRequestBean.getNumberOfPages())) {
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

        if (!isFree()) {
            if (!hasEvent()) {
                createCertificateRequestEvent();
            } else {
                getEvent().recalculateState(academicServiceRequestBean.getFinalSituationDate());
            }
        }
    }

    protected void createCertificateRequestEvent() {
        new CertificateRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    public boolean isFree() {
        if (getDocumentRequestType() == DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE
                && RegistrationAgreement.MOBILITY_AGREEMENTS.contains(getRegistration().getRegistrationAgreement())) {
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

    @Deprecated
    public boolean hasOtherDocumentPurposeTypeDescription() {
        return getOtherDocumentPurposeTypeDescription() != null;
    }

    @Deprecated
    public boolean hasDocumentPurposeType() {
        return getDocumentPurposeType() != null;
    }

}
