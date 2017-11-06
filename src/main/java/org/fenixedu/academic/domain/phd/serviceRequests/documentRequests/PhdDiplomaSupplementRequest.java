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

import java.util.Locale;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.EctsGraduationGradeConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsTableIndex;
import org.fenixedu.academic.domain.degreeStructure.NoEctsComparabilityTableFound;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramInformation;
import org.fenixedu.academic.domain.phd.conclusion.PhdConclusionProcess;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisFinalGrade;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IRectorateSubmissionBatchDocumentEntry;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.DiplomaSupplement;
import org.fenixedu.academic.util.Bundle;
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

        getPhdIndividualProgramProcess().getPerson().getProfile().changeName(bean.getGivenNames(), bean.getFamilyNames(), null);
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
        return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return false;
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
        return getDescription(getAcademicServiceRequestType(), "DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST.THIRD_CYCLE");
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
        final PhdIndividualProgramProcess individualProgramProcess = getPhdIndividualProgramProcess();
        final ExecutionYear executionYear = individualProgramProcess.getExecutionYear();
        return individualProgramProcess.getPhdProgram().getName(executionYear).getContent(locale);
    }

    @Override
    public double getEctsCredits() {
        PhdProgramInformation information = getPhdInformationForConclusionDate();

        if (information == null) {
            return 0;
        }

        return information.getMaxStudyPlanEctsCredits().add(information.getMaxThesisEctsCredits()).doubleValue();
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
    public DegreeOfficialPublication getDegreeOfficialPublication() {
        PhdConclusionProcess lastConclusionProcess = getPhdIndividualProgramProcess().getLastConclusionProcess();

        DateTime conclusionDate = null;
        if (!getPhdIndividualProgramProcess().getStudyPlan().isExempted()) {
            Registration registration = getPhdIndividualProgramProcess().getRegistration();
            //TODO: phd-refactor this should be change to terminal program conclusion
            conclusionDate =
                    registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE).getConclusionDate()
                            .toDateMidnight().toDateTime();
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

        return getPhdIndividualProgramProcess().getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle())
                .getFinalGrade().getIntegerValue();
    }

    @Override
    public String getFinalAverageQualified(final Locale locale) {
        Integer finalGrade = getFinalAverage();
        if (finalGrade == null) {
            return null;
        }
        String qualifiedAverageGrade;
        if (finalGrade <= 13) {
            qualifiedAverageGrade = "sufficient";
        } else if (finalGrade <= 15) {
            qualifiedAverageGrade = "good";
        } else if (finalGrade <= 17) {
            qualifiedAverageGrade = "verygood";
        } else {
            qualifiedAverageGrade = "excelent";
        }

        return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

    @Override
    public boolean isPiggyBackedOnRegistry() {
        return getRegistryDiplomaRequest() != null;
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
            //TODO: phd-refactor this should be change to terminal program conclusion
            conclusionDate =
                    registration.getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE).getConclusionDate()
                            .toDateMidnight().toLocalDate();
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

}
