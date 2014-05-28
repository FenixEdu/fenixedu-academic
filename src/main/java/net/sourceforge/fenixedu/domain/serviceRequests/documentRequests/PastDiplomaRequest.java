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
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PastDiplomaRequest extends PastDiplomaRequest_Base {

    public PastDiplomaRequest() {
        super();
    }

    public PastDiplomaRequest(final DocumentRequestCreateBean bean) {
        this();
        init(bean);

        checkParameters(bean);
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.PAST_DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
        return getClass().getName() + "." + getDegreeType().getName();
    }

    @Override
    public boolean isPagedDocument() {
        return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return true;
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return false;
    }

    @Override
    public EventType getEventType() {
        return EventType.PAST_DEGREE_DIPLOMA_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToProcess()) {
            checkForDuplicate();

            if (!getRegistration().isRegistrationConclusionProcessed()) {
                throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
            }
        }
    }

    private void checkForDuplicate() {
        final PastDiplomaRequest diplomaRequest = getRegistration().getPastDiplomaRequest();
        if (diplomaRequest != null && diplomaRequest != this) {
            throw new DomainException("DiplomaRequest.diploma.already.requested");
        }
    }

}
