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

import java.util.Locale;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.EctsGraduationGradeConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsTableIndex;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaSupplementRequest;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.report.academicAdministrativeOffice.DiplomaSupplement;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;

public class DiplomaSupplementRequest extends DiplomaSupplementRequest_Base implements IDiplomaSupplementRequest {

    public DiplomaSupplementRequest() {
        super();
    }

    public DiplomaSupplementRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);
        checkParameters(bean);
        setLanguage(org.fenixedu.academic.util.LocaleUtils.PT);
        setProgramConclusion(bean.getProgramConclusion());
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {

        if (bean.getProgramConclusion() == null) {
            throw new DomainException("error.program.conclusion.empty");
        }

        final String fullName = getRegistration().getStudent().getPerson().getName();
        final String familyName = bean.getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? bean.getGivenNames() : bean.getGivenNames() + " " + familyName;

        if (!fullName.equals(composedName)) {
            throw new DomainException("error.diplomaSupplementRequest.splittedNamesDoNotMatch");
        }
        getRegistration().getPerson().getProfile().changeName(bean.getGivenNames(), bean.getFamilyNames(), null);
        RegistryDiplomaRequest registry = getRegistration().getRegistryDiplomaRequest(bean.getProgramConclusion());
        DiplomaRequest diploma = getRegistration().getDiplomaRequest(bean.getProgramConclusion());
        if (registry == null && diploma == null) {
            throw new DomainException(
                    "error.diplomaSupplementRequest.cannotAskForSupplementWithoutEitherRegistryDiplomaOrDiplomaRequest");
        }
        final DiplomaSupplementRequest supplement = getRegistration().getDiplomaSupplementRequest(bean.getProgramConclusion());
        if (supplement != null) {
            throw new DomainException("error.diplomaSupplementRequest.alreadyRequested");
        }
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    final public String getDescription() {
        return Joiner.on(" : ").join(super.getDescription(getAcademicServiceRequestType()),
                getProgramConclusion().getName().getContent());
    }

    @Override
    public String getDocumentTemplateKey() {
        return DiplomaSupplement.class.getName();
    }

    public String getGivenNames() {
        return getRegistration().getPerson().getGivenNames();
    }

    public String getFamilyNames() {
        return getRegistration().getPerson().getFamilyNames();
    }

    @Override
    public boolean isPagedDocument() {
        return true;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
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
    public boolean isPayedUponCreation() {
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
    public boolean isToPrint() {
        return true;
    }

    @Override
    public boolean isPiggyBackedOnRegistry() {
        return getRegistryDiplomaRequest() != null;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
        if (academicServiceRequestBean.isToProcess()) {
            if (!getProgramConclusion().isConclusionProcessed(getRegistration())) {
                throw new DomainException("error.diplomaSupplement.registration.not.submited.to.conclusion.process");
            }
            if (getRegistryCode() == null) {
                RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getProgramConclusion());
                DiplomaRequest diploma = getRegistration().getDiplomaRequest(getProgramConclusion());
                if (registryRequest != null) {
                    registryRequest.getRegistryCode().addDocumentRequest(this);
                } else if (diploma != null && diploma.hasRegistryCode()) {
                    diploma.getRegistryCode().addDocumentRequest(this);
                } else {
                    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
                }
            }
            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        }
    }

    @Override
    public String getGraduateTitle(final Locale locale) {
        return getProgramConclusion().groupFor(getRegistration())
                .map(cg -> cg.getDegreeModule().getGraduateTitle(getConclusionYear(), locale)).orElse(null);
    }

    @Override
    public Integer getRegistrationNumber() {
        return getRegistration().getNumber();
    }

    @Override
    public String getPrevailingScientificArea(final Locale locale) {
        Degree degree = getDegree();
        ExecutionYear conclusion = getConclusionYear();
        return degree.getFilteredName(conclusion, locale);
    }

    @Override
    public double getEctsCredits() {
        ExecutionYear conclusion = getConclusionYear();
        final Supplier<? extends DomainException> noDefaultCredits =
                () -> new DomainException("error.CycleCourseGroup.cannot.calculate.default.ects.credits");
        return getProgramConclusion().groupFor(getRegistration()).map(cg -> cg.getDegreeModule().getDefaultEcts(conclusion))
                .orElseThrow(noDefaultCredits);
    }

    @Override
    public DegreeOfficialPublication getDegreeOfficialPublication() {
        ConclusionProcess conclusionProcess =
                getProgramConclusion().groupFor(getRegistration()).map(cg -> cg.getConclusionProcess()).orElse(null);

        DegreeOfficialPublication dr =
                getRegistration().getDegree().getOfficialPublication(
                        conclusionProcess.getConclusionDate().toDateTimeAtStartOfDay());

        return dr;
    }

    @Override
    public Integer getFinalAverage() {
        return getRegistration().getFinalGrade(getProgramConclusion()).getIntegerValue();
    }

    @Override
    public String getFinalAverageQualified(final Locale locale) {
        Integer finalGrade = getFinalAverage();
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
    public ExecutionYear getConclusionYear() {
        return getProgramConclusion().groupFor(getRegistration()).map(CurriculumGroup::getConclusionProcess)
                .map(ConclusionProcess::getConclusionYear).orElse(null);
    }

    @Override
    public ExecutionYear getStartYear() {
        return getRegistration().getStartExecutionYear();
    }

    @Override
    public CycleType getRequestedCycle() {
        return getProgramConclusion().groupFor(getRegistration()).filter(CurriculumGroup::isCycleCurriculumGroup)
                .map(cg -> ((CycleCurriculumGroup) cg).getCycleType()).orElse(null);
    }

    @Override
    public EctsGraduationGradeConversionTable getGraduationConversionTable() {

        return EctsTableIndex.getGraduationGradeConversionTable(getRegistration().getDegree(), getRequestedCycle(),
                getConclusionYear().getAcademicInterval(), getProcessingDate() != null ? getProcessingDate() : new DateTime());
    }

    @Override
    public Integer getNumberOfCurricularYears() {
        return getRegistration().getLastStudentCurricularPlan().getDegreeCurricularPlan().getDurationInYears(getRequestedCycle());
    }

    @Override
    public Integer getNumberOfCurricularSemesters() {
        return getRegistration().getLastStudentCurricularPlan().getDegreeCurricularPlan()
                .getDurationInSemesters(getRequestedCycle());
    }

    @Override
    public Boolean isExemptedFromStudy() {
        return false;
    }

}
