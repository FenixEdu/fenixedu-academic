package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PhotocopyRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhotocopyRequest extends PhotocopyRequest_Base {

    protected PhotocopyRequest() {
        super();
        setNumberOfPages(0);
    }

    public PhotocopyRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
        this();
        super.init(bean);
    }

    @Override
    public EventType getEventType() {
        return EventType.PHOTOCOPY_REQUEST;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.PHOTOCOPY;
    }

    @Override
    public String getDocumentTemplateKey() {
        // this request does not need a document template key, because no
        // document will be printed
        return null;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {

        if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
            getEvent().cancel(academicServiceRequestBean.getResponsible());

        } else if (academicServiceRequestBean.isToConclude()) {
            if (!hasNumberOfPages()) {
                throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
            }

            if (!isFree()) {
                new PhotocopyRequestEvent(getAdministrativeOffice(), getPerson(), this);
            }
        } else if (academicServiceRequestBean.isToDeliver()) {
            if (isPayable() && !isPayed()) {
                throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
            }
        }
    }

    @Override
    public boolean isPagedDocument() {
        return true;
    }

    @Override
    public boolean isToPrint() {
        return false;
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
    public boolean isPayedUponCreation() {
        return false;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasPurpose() {
        return getPurpose() != null;
    }

}
