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
package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests;

import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PhdDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.documents.DocumentRequestGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.AdministrativeOfficeDocument;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhdDiplomaRequest extends PhdDiplomaRequest_Base implements IDiplomaRequest, IRectorateSubmissionBatchDocumentEntry {

    private static final Logger logger = LoggerFactory.getLogger(PhdDiplomaRequest.class);

    protected PhdDiplomaRequest() {
        super();
    }

    protected PhdDiplomaRequest(final PhdDocumentRequestCreateBean bean) {
        init(bean);
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
        throw new DomainException("invoke init(PhdDocumentRequestCreateBean)");
    }

    @Override
    protected void init(final PhdDocumentRequestCreateBean bean) {
        checkParameters(bean);
        super.init(bean);

        if (!isFree()) {
            PhdDiplomaRequestEvent.create(getAdministrativeOffice(), getPhdIndividualProgramProcess().getPerson(), this);
        }

        applyRegistryCode();
    }

    private void applyRegistryCode() {
        RegistryCode code = getRegistryCode();
        if (code != null) {
            if (!code.getDocumentRequestSet().contains(this)) {
                code.addDocumentRequest(this);
            }
        } else {
            getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
        }
    }

    private void checkParameters(final PhdDocumentRequestCreateBean bean) {
        PhdIndividualProgramProcess process = bean.getPhdIndividualProgramProcess();

        if (process.hasDiplomaRequest()) {
            throw new PhdDomainOperationException("error.phdDiploma.alreadyHasDiplomaRequest");
        }

        checkRegistryDiplomaRequest(process);
    }

    private void checkRegistryDiplomaRequest(PhdIndividualProgramProcess process) {

        if (!process.isBolonha()) {
            return;
        }

        if (!process.hasRegistryDiplomaRequest()) {
            throw new PhdDomainOperationException("error.phdDiploma.registryDiploma.must.be.requested");
        }

        PhdRegistryDiplomaRequest phdRegistryDiploma = process.getRegistryDiplomaRequest();

        if (phdRegistryDiploma.isPayedUponCreation() && !phdRegistryDiploma.getEvent().isPayed()) {
            throw new PhdDomainOperationException("error.phdDiploma.registryDiploma.must.be.payed");
        }
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DIPLOMA_REQUEST;
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName() + "." + "PHD");
    }

    @Override
    public String getDocumentTemplateKey() {
        return PhdDiplomaRequest.class.getName();
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
        return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        return true;
    }

    @Override
    public EventType getEventType() {
        return EventType.BOLONHA_PHD_DIPLOMA_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    public RegistryCode getRegistryCode() {
        PhdIndividualProgramProcess phdIndividualProgramProcess = getPhdIndividualProgramProcess();
        RegistryCode registryCode = null;

        if (phdIndividualProgramProcess.hasRegistryDiplomaRequest()) {
            registryCode = phdIndividualProgramProcess.getRegistryDiplomaRequest().getRegistryCode();
        }

        return registryCode != null ? registryCode : super.getRegistryCode();
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
            if (!getPhdIndividualProgramProcess().isConclusionProcessed()) {
                throw new PhdDomainOperationException("error.registryDiploma.registrationNotSubmitedToConclusionProcess");
            }

            if (isPayable() && !isPayed()) {
                throw new PhdDomainOperationException("AcademicServiceRequest.hasnt.been.payed");
            }

            getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);

            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        } else if (academicServiceRequestBean.isToCancelOrReject()) {
            if (hasEvent()) {
                getEvent().cancel(academicServiceRequestBean.getResponsible());
            }
        }
    }

    @Override
    public CycleType getWhatShouldBeRequestedCycle() {
        return CycleType.THIRD_CYCLE;
    }

    @Override
    public LocalDate getConclusionDate() {
        return getPhdIndividualProgramProcess().getConclusionDate();
    }

    @Override
    public Integer getFinalAverage() {
        return null;
    }

    @Override
    public String getFinalAverageQualified() {
        return null;
    }

    @Override
    public String getDissertationThesisTitle() {
        return getPhdIndividualProgramProcess().getThesisTitle();
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        return getPhdIndividualProgramProcess().getGraduateTitle(locale);
    }

    public PhdThesisFinalGrade getThesisFinalGrade() {
        return getPhdIndividualProgramProcess().getFinalGrade();
    }

    public Boolean isBolonha() {
        return getPhdIndividualProgramProcess().isBolonha();
    }

    public static PhdDiplomaRequest create(final PhdDocumentRequestCreateBean bean) {
        return new PhdDiplomaRequest(bean);
    }

    @Override
    public CycleType getRequestedCycle() {
        return getWhatShouldBeRequestedCycle();
    }

    @Override
    public String getProgrammeTypeDescription() {
        return BundleUtil.getString(Bundle.PHD, "label.php.program");
    }

    @Override
    public String getViewStudentProgrammeLink() {
        return "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId="
                + getPhdIndividualProgramProcess().getExternalId();
    }

    @Override
    public String getReceivedActionLink() {
        return String
                .format("/phdAcademicServiceRequestManagement.do?method=prepareReceiveOnRectorate&amp;phdAcademicServiceRequestId=%s&amp;batchOid=%s",
                        getExternalId(), getRectorateSubmissionBatch().getExternalId());
    }

    @Override
    public boolean isProgrammeLinkVisible() {
        return getPhdIndividualProgramProcess().isCurrentUserAllowedToManageProcess();
    }

    @Override
    public byte[] generateDocument() {
        try {
            final List<AdministrativeOfficeDocument> documents =
                    AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator.create(this);

            final AdministrativeOfficeDocument[] array = {};
            byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(documents.toArray(array));

            DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
            return data;
        } catch (JRException e) {
            logger.error(e.getMessage(), e);
            throw new DomainException("error.phdDiplomaRequest.errorGeneratingDocument");
        }
    }
}
