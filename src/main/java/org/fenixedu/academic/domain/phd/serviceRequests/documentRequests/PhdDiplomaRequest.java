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
package org.fenixedu.academic.domain.phd.serviceRequests.documentRequests;

import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.PhdDiplomaRequestEvent;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.documents.DocumentRequestGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisFinalGrade;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
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
        return DiplomaRequest.class.getName();
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
            if (getEvent() != null) {
                getEvent().cancel(academicServiceRequestBean.getResponsible());
            }
        }
    }

    @Override
    public CycleType getRequestedCycle() {
        return CycleType.THIRD_CYCLE;
    }

    @Override
    public LocalDate getConclusionDate() {
        return getPhdIndividualProgramProcess().getConclusionDate();
    }

    @Override
    public ExecutionYear getConclusionYear() {
        return getPhdIndividualProgramProcess().getConclusionYear();
    }

    @Override
    public Integer getFinalAverage() {
        return null;
    }

    @Override
    public String getDissertationThesisTitle() {
        return getPhdIndividualProgramProcess().getThesisTitle();
    }

    private LocalizedString getLocalizedThesisTitle() {
        return new LocalizedString(LocaleUtils.PT, getDissertationThesisTitle())
                .with(LocaleUtils.EN, getPhdIndividualProgramProcess().getThesisTitleEn());
    }

    public String getThesisTitle(final Locale locale) {
        return getLocalizedThesisTitle().getContent(locale);
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        return getPhdIndividualProgramProcess().getGraduateTitle(locale);
    }

    @Override
    public Degree getDegree() {
        /**
         * TODO: phd-refactor
         * all individual processes must have a registration therefore degree comes always from registration
         */

        if (getPhdIndividualProgramProcess().getRegistration() != null) {
            return getPhdIndividualProgramProcess().getRegistration().getDegree();
        }

        return getPhdIndividualProgramProcess().getPhdProgram().getDegree();
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
            byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(documents);
            DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
            return data;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DomainException("error.phdDiplomaRequest.errorGeneratingDocument");
        }
    }



}
