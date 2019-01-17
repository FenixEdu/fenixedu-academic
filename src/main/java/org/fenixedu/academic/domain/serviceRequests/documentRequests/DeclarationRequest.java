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

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.treasury.IAcademicTreasuryEvent;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

abstract public class DeclarationRequest extends DeclarationRequest_Base {

    protected DeclarationRequest() {
        super();
        super.setNumberOfPages(0);
    }

    final protected void init(final DocumentRequestCreateBean bean) {
        super.init(bean);

        super.checkParameters(bean);
        super.setDocumentPurposeTypeInstance(bean.getChosenDocumentPurposeType());
        super.setDocumentPurposeType(
                getDocumentPurposeTypeInstance() != null ? getDocumentPurposeTypeInstance().getDocumentPurposeType() : null);
        super.setOtherDocumentPurposeTypeDescription(bean.getOtherPurpose());
    }

    static final public DeclarationRequest create(final DocumentRequestCreateBean bean) {
        final DocumentRequestType requestType = bean.getChosenServiceRequestType().getDocumentRequestType();
        switch (requestType) {
        case SCHOOL_REGISTRATION_DECLARATION:
            return new SchoolRegistrationDeclarationRequest(bean);
        case ENROLMENT_DECLARATION:
            return new EnrolmentDeclarationRequest(bean);
        case IRS_DECLARATION:
            return new IRSDeclarationRequest(bean);
        case GENERIC_DECLARATION:
            return new GenericDeclarationRequest(bean);
        }

        return null;
    }

    @Override
    final public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
        throw new DomainException("error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.documentPurposeType");
    }

    @Override
    final public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.otherDocumentTypeDescription");
    }

    @Override
    final public Boolean getUrgentRequest() {
        return Boolean.FALSE;
    }

    final public void edit(final DocumentRequestBean documentRequestBean) {

        final IAcademicTreasuryEvent event =
                TreasuryBridgeAPIFactory.implementation().academicTreasuryEventForAcademicServiceRequest(this);

        if (isPayable() && event != null && !getNumberOfPages().equals(documentRequestBean.getNumberOfPages())) {
            throw new DomainException("error.serviceRequests.documentRequests.cannot.change.numberOfPages.on.payed.documents");
        }

        super.edit(documentRequestBean);
        super.setNumberOfPages(documentRequestBean.getNumberOfPages());
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToConclude() && getServiceRequestType().isPayable()) {
            if (getNumberOfPages() == null || getNumberOfPages().intValue() == 0) {
                throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
            }
        }
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    final public boolean isFree() {
        if (getDocumentPurposeType() == DocumentPurposeType.PPRE) {
            return false;
        }
        return super.isFree() || hasFreeDeclarationRequests();
    }

    abstract protected boolean hasFreeDeclarationRequests();

    @Override
    public boolean isPayedUponCreation() {
        return false;
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
