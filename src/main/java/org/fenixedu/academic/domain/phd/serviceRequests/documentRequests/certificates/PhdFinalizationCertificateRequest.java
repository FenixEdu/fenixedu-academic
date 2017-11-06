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
package org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.certificates;

import java.util.List;

import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.PhdFinalizationCertificateRequestEvent;
import org.fenixedu.academic.domain.documents.DocumentRequestGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.RectorateSubmissionBatch;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhdFinalizationCertificateRequest extends PhdFinalizationCertificateRequest_Base {

    private static final Logger logger = LoggerFactory.getLogger(PhdFinalizationCertificateRequest.class);

    protected PhdFinalizationCertificateRequest() {
        super();
    }

    protected PhdFinalizationCertificateRequest(PhdDocumentRequestCreateBean bean) {
        this();

        this.init(bean);

        PhdRegistryDiplomaRequest registryDiplomaRequest = getPhdIndividualProgramProcess().getRegistryDiplomaRequest();

        if (registryDiplomaRequest == null) {
            throw new PhdDomainOperationException("error.PhdFinalizationCertificateRequest.registry.diploma.request.none");
        }

        RectorateSubmissionBatch rectorateSubmissionBatch = registryDiplomaRequest.getRectorateSubmissionBatch();

        if (rectorateSubmissionBatch == null) {
            throw new PhdDomainOperationException(
                    "error.PhdFinalizationCertificateRequest.registry.diploma.submission.batch.not.sent");
        }

    }

    @Override
    protected void init(PhdDocumentRequestCreateBean bean) {
        super.init(bean);

        if (!isFree()) {
            PhdFinalizationCertificateRequestEvent.create(getAdministrativeOffice(), getPerson(), this);
        }

        if (!bean.getPhdIndividualProgramProcess().isBolonha()) {
            return;
        }

        if (getPhdIndividualProgramProcess().getRegistryDiplomaRequest() == null) {
            throw new PhdDomainOperationException("error.PhdFinalizationCertificateRequest.registry.diploma.not.requested");
        }

    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        try {
            verifyIsToProcessAndHasPersonalInfo(academicServiceRequestBean);
            verifyIsToDeliveredAndIsPayed(academicServiceRequestBean);
        } catch (DomainException e) {
            throw new PhdDomainOperationException(e.getKey(), e, e.getArgs());
        }

        super.internalChangeState(academicServiceRequestBean);
        if (academicServiceRequestBean.isToProcess()) {
            PhdRegistryDiplomaRequest registryDiplomaRequest = getPhdIndividualProgramProcess().getRegistryDiplomaRequest();

            if (registryDiplomaRequest == null) {
                throw new PhdDomainOperationException("error.PhdFinalizationCertificateRequest.registry.diploma.request.none");
            }

            RectorateSubmissionBatch rectorateSubmissionBatch = registryDiplomaRequest.getRectorateSubmissionBatch();

            if (rectorateSubmissionBatch == null) {
                throw new PhdDomainOperationException(
                        "error.PhdFinalizationCertificateRequest.registry.diploma.submission.batch.not.sent");
            }

            if (!rectorateSubmissionBatch.isSent() && !rectorateSubmissionBatch.isReceived()) {
                throw new PhdDomainOperationException(
                        "error.PhdFinalizationCertificateRequest.registry.diploma.submission.batch.not.sent");
            }

            if (!getPhdIndividualProgramProcess().isConcluded()) {
                throw new PhdDomainOperationException(
                        "error.PhdFinalizationCertificateRequest.phd.process.not.submited.to.conclusion.process");
            }

            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        }
    }

    @Override
    public byte[] generateDocument() {
        try {
            final List<AdministrativeOfficeDocument> documents =
                    AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this);

            byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(documents);

            DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
            return data;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DomainException("error.phdDiplomaRequest.errorGeneratingDocument");
        }
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.PHD_FINALIZATION_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
        return PhdFinalizationCertificateRequest.class.getName();
    }

    @Override
    public boolean isPayedUponCreation() {
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
    public EventType getEventType() {
        return EventType.PHD_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    public static PhdFinalizationCertificateRequest create(final PhdDocumentRequestCreateBean bean) {
        return new PhdFinalizationCertificateRequest(bean);
    }

}
