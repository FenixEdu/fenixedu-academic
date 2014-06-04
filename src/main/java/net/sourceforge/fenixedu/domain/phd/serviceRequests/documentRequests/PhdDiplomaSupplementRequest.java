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

import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsTableIndex;
import net.sourceforge.fenixedu.domain.degreeStructure.NoEctsComparabilityTableFound;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramInformation;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.DiplomaSupplement;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdDiplomaSupplementRequest extends PhdDiplomaSupplementRequest_Base implements IDiplomaSupplementRequest,
        IRectorateSubmissionBatchDocumentEntry {

    protected PhdDiplomaSupplementRequest() {
        super();
    }

    protected PhdDiplomaSupplementRequest(final PhdDocumentRequestCreateBean bean) {
        this();
        init(bean);
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
        throw new DomainException("invoke init(PhdDocumentRequestCreateBean)");
    }

    @Override
    protected void init(final PhdDocumentRequestCreateBean bean) {
        super.init(bean);
        checkParameters(bean);

        getPhdIndividualProgramProcess().getPerson().setGivenNames(bean.getGivenNames());
        getPhdIndividualProgramProcess().getPerson().setFamilyNames(bean.getFamilyNames());
    }

    private void checkParameters(final PhdDocumentRequestCreateBean bean) {
        final String fullName = getPhdIndividualProgramProcess().getStudent().getPerson().getName();
        final String familyName = bean.getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? bean.getGivenNames() : bean.getGivenNames() + " " + familyName;

        if (!fullName.equals(composedName)) {
            throw new DomainException("error.diplomaSupplementRequest.splittedNamesDoNotMatch");
        }

        PhdIndividualProgramProcess process = getPhdIndividualProgramProcess();
        if (!process.hasRegistryDiplomaRequest() && process.hasDiplomaRequest()) {
            throw new DomainException(
                    "error.diplomaSupplementRequest.cannotAskForSupplementWithoutEitherRegistryDiplomaOrDiplomaRequest");
        }

        final PhdDiplomaSupplementRequest supplement = process.getDiplomaSupplementRequest();
        if (supplement != null && supplement != this) {
            throw new DomainException("error.diplomaSupplementRequest.alreadyRequested");
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
        return null;
    }

    @Override
    public boolean hasPersonalInfo() {
        return false;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
        return DiplomaSupplement.class.getName();
    }

    @Override
    public CycleType getRequestedCycle() {
        return CycleType.THIRD_CYCLE;
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
            if (!getPhdIndividualProgramProcess().isConcluded()) {
                throw new PhdDomainOperationException(
                        "error.phdDiplomaSupplement.registration.not.submited.to.conclusion.process");
            }

            if (getRegistryCode() == null) {
                getRegistryDiplomaRequest().getRegistryCode().addDocumentRequest(this);
                getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
            }

            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        }
    }

    public static PhdDiplomaSupplementRequest create(PhdDocumentRequestCreateBean bean) {
        return new PhdDiplomaSupplementRequest(bean);
    }

    @Override
    public String getDescription() {
        return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName() + "."
                + DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA.name());
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        return getPhdIndividualProgramProcess().getGraduateTitle(locale);
    }

    @Override
    public Integer getRegistrationNumber() {
        return getStudent().getNumber();
    }

    @Override
    public String getPrevailingScientificArea(final Locale locale) {
        return getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(locale);
    }

    @Override
    public long getEctsCredits() {
        PhdProgramInformation information = getPhdInformationForConclusionDate();

        if (information == null) {
            return 0;
        }

        return information.getMaxStudyPlanEctsCredits().add(information.getMaxThesisEctsCredits()).longValue();
    }

    @Override
    public DegreeOfficialPublication getDegreeOfficialPublication() {
        PhdConclusionProcess lastConclusionProcess = getPhdIndividualProgramProcess().getLastConclusionProcess();

        DateTime conclusionDate = null;
        if (!getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
            Registration registration = getPhdIndividualProgramProcess().getRegistration();
            conclusionDate = registration.getConclusionDateForBolonha().toDateMidnight().toDateTime();
        } else {
            conclusionDate = lastConclusionProcess.getConclusionDate().toDateMidnight().toDateTime();
        }

        return getPhdIndividualProgramProcess().getPhdProgram().getDegree().getOfficialPublication(conclusionDate);
    }

    @Override
    public Integer getFinalAverage() {
        if (getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
            return null;
        }

        return getPhdIndividualProgramProcess().getRegistration().getFinalAverage(getRequestedCycle());
    }

    @Override
    public String getFinalAverageQualified(final Locale locale) {
        if (getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
            return null;
        }

        Integer finalAverage = getPhdIndividualProgramProcess().getRegistration().getFinalAverage(getRequestedCycle());
        String qualifiedAverageGrade;
        if (finalAverage <= 13) {
            qualifiedAverageGrade = "sufficient";
        } else if (finalAverage <= 15) {
            qualifiedAverageGrade = "good";
        } else if (finalAverage <= 17) {
            qualifiedAverageGrade = "verygood";
        } else {
            qualifiedAverageGrade = "excelent";
        }

        return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

    @Override
    public boolean isPiggyBackedOnRegistry() {
        return hasRegistryDiplomaRequest();
    }

    public String getThesisFinalGrade(final Locale locale) {
        PhdThesisFinalGrade finalGrade = getPhdIndividualProgramProcess().getFinalGrade();

        return finalGrade.getLocalizedName(locale);
    }

    @Override
    public ExecutionYear getConclusionYear() {
        return getPhdIndividualProgramProcess().getConclusionYear();
    }

    @Override
    public EctsGraduationGradeConversionTable getGraduationConversionTable() {
        try {
            return EctsTableIndex.getGraduationGradeConversionTable(getPhdIndividualProgramProcess().getPhdProgram().getDegree(),
                    getRequestedCycle(), getConclusionYear().getAcademicInterval(), getProcessingDate());
        } catch (NoEctsComparabilityTableFound e) {
            throw new PhdDomainOperationException("error.no.ects.comparability.found");
        }
    }

    @Override
    public Integer getNumberOfCurricularYears() {
        PhdProgramInformation information = getPhdInformationForConclusionDate();

        if (information == null) {
            return null;
        }

        return information.getNumberOfYears();
    }

    private PhdProgramInformation getPhdInformationForConclusionDate() {
        LocalDate conclusionDate = null;

        if (!getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
            Registration registration = getPhdIndividualProgramProcess().getRegistration();
            conclusionDate = registration.getConclusionDateForBolonha().toDateMidnight().toLocalDate();
        } else {
            PhdConclusionProcess conclusionProcess = getPhdIndividualProgramProcess().getLastConclusionProcess();
            conclusionDate = conclusionProcess.getConclusionDate();
        }

        PhdProgram phdProgram = getPhdIndividualProgramProcess().getPhdProgram();
        PhdProgramInformation information = phdProgram.getPhdProgramInformationByDate(conclusionDate);
        return information;
    }

    @Override
    public Integer getNumberOfCurricularSemesters() {
        PhdProgramInformation information = getPhdInformationForConclusionDate();

        if (information == null) {
            return null;
        }

        return information.getNumberOfSemesters();
    }

    @Override
    public Boolean isExemptedFromStudy() {
        return getPhdIndividualProgramProcess().getStudyPlan().isExempted();
    }

    @Override
    public Registration getRegistration() {
        if (isExemptedFromStudy()) {
            return null;
        }

        return getPhdIndividualProgramProcess().getRegistration();
    }

    @Override
    public boolean hasRegistration() {
        return getRegistration() != null;
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

    @Deprecated
    public boolean hasRegistryDiplomaRequest() {
        return getRegistryDiplomaRequest() != null;
    }

}
