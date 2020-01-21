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
import org.fenixedu.academic.domain.accounting.events.serviceRequests.PhdRegistryDiplomaRequestEvent;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.documents.DocumentRequestGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisFinalGrade;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.AdministrativeOfficeDocument;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhdRegistryDiplomaRequest extends PhdRegistryDiplomaRequest_Base implements IRegistryDiplomaRequest,
        IRectorateSubmissionBatchDocumentEntry {

    private static final Logger logger = LoggerFactory.getLogger(PhdRegistryDiplomaRequest.class);

    protected PhdRegistryDiplomaRequest() {
        super();
    }

    protected PhdRegistryDiplomaRequest(final PhdDocumentRequestCreateBean bean) {
        this();
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
            PhdRegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getPhdIndividualProgramProcess().getPerson(), this);
        }

        setDiplomaSupplement(PhdDiplomaSupplementRequest.create(bean));
    }

    private void checkParameters(final PhdDocumentRequestCreateBean bean) {
        PhdIndividualProgramProcess process = bean.getPhdIndividualProgramProcess();
        if (process.hasRegistryDiplomaRequest()) {
            throw new PhdDomainOperationException("error.registryDiploma.alreadyRequested");
        }

        if (!process.isBolonha()) {
            return;
        }

        if (process.hasDiplomaRequest()) {
            throw new PhdDomainOperationException("error.registryDiploma.alreadyHasDiplomaRequest");
        }

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
        return true;
    }

    @Override
    public EventType getEventType() {
        return EventType.BOLONHA_PHD_REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    public CycleType getRequestedCycle() {
        return CycleType.THIRD_CYCLE;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
        return RegistryDiplomaRequest.class.getName();
    }

    @Override
    public String getFinalAverage(final Locale locale) {
        PhdThesisFinalGrade finalGrade = getPhdIndividualProgramProcess().getFinalGrade();
        return finalGrade.getLocalizedName(locale);
    }

    private LocalizedString getLocalizedThesisTitle() {
        return new LocalizedString(LocaleUtils.PT, getPhdIndividualProgramProcess().getThesisTitle())
                .with(LocaleUtils.EN, getPhdIndividualProgramProcess().getThesisTitleEn());
    }

    @Override
    public String getThesisTitle(Locale locale) {
        return getLocalizedThesisTitle().getContent(locale);
    }

    @Override
    public String getQualifiedAverageGrade(final Locale locale) {
        String qualifiedAverageGrade;

        PhdThesisFinalGrade grade = getPhdIndividualProgramProcess().getFinalGrade();

        switch (grade) {
        case APPROVED:
        case PRE_BOLONHA_APPROVED:
            qualifiedAverageGrade = "sufficient";
            break;
        case APPROVED_WITH_PLUS:
        case PRE_BOLONHA_APPROVED_WITH_PLUS:
            qualifiedAverageGrade = "good";
            break;
        case APPROVED_WITH_PLUS_PLUS:
        case PRE_BOLONHA_APPROVED_WITH_PLUS_PLUS:
            qualifiedAverageGrade = "verygood";
            break;
        default:
            throw new DomainException("docs.academicAdministrativeOffice.RegistryDiploma.unknown.grade");
        }

        return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
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
    public String getGraduateTitle(Locale locale) {
        return getPhdIndividualProgramProcess().getGraduateTitle(locale);
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

            if (getRegistryCode() == null) {

                PhdDiplomaRequest diplomaRequest = getPhdIndividualProgramProcess().getDiplomaRequest();

                if (diplomaRequest != null && diplomaRequest.hasRegistryCode()) {
                    diplomaRequest.getRegistryCode().addDocumentRequest(this);
                } else {
                    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
                }

                getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
            }

            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }

            getDiplomaSupplement().process();
        } else if (academicServiceRequestBean.isToConclude()) {
            if (getDiplomaSupplement().isConcludedSituationAccepted()) {
                getDiplomaSupplement().conclude();
            }
        } else if (academicServiceRequestBean.isToCancelOrReject()) {
            if (getEvent() != null) {
                getEvent().cancel(academicServiceRequestBean.getResponsible());
            }

            if (academicServiceRequestBean.isToCancel()) {
                getDiplomaSupplement().cancel(academicServiceRequestBean.getJustification());
            }

            if (academicServiceRequestBean.isToReject()) {
                getDiplomaSupplement().reject(academicServiceRequestBean.getJustification());
            }
        }
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType(), "DocumentRequestType.REGISTRY_DIPLOMA_REQUEST.THIRD_CYCLE");
    }

    public static PhdRegistryDiplomaRequest create(final PhdDocumentRequestCreateBean bean) {
        return new PhdRegistryDiplomaRequest(bean);
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

    @Override
    public String getDegreeName(ExecutionYear year) {
        return getDegree().getNameI18N(year).getContent(getLanguage());
    }
}
