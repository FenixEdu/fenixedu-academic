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
