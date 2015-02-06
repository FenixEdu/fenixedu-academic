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

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.EctsGraduationGradeConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EctsTableIndex;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaSupplementRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.CycleConclusionProcess;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DiplomaSupplementRequest extends DiplomaSupplementRequest_Base implements IDiplomaSupplementRequest {

    public DiplomaSupplementRequest() {
        super();
    }

    public DiplomaSupplementRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);
        checkParameters(bean);
        setLanguage(MultiLanguageString.pt);
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {
        if (bean.getHasCycleTypeDependency()) {
            if (bean.getRequestedCycle() == null) {
                throw new DomainException("error.diplomaSupplementRequest.requestedCycleMustBeGiven");
            } else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
                throw new DomainException(
                        "error.diplomaSupplementRequest.requestedDegreeTypeIsNotAllowedForGivenStudentCurricularPlan");
            }
            super.setRequestedCycle(bean.getRequestedCycle());
        } else {
            if (bean.getRegistration().getDegreeType().hasExactlyOneCycleType()) {
                super.setRequestedCycle(bean.getRegistration().getDegreeType().getCycleType());
            }
        }

        final String fullName = getRegistration().getStudent().getPerson().getName();
        final String familyName = bean.getFamilyNames();
        final String composedName =
                familyName == null || familyName.isEmpty() ? bean.getGivenNames() : bean.getGivenNames() + " " + familyName;

        if (!fullName.equals(composedName)) {
            throw new DomainException("error.diplomaSupplementRequest.splittedNamesDoNotMatch");
        }
        getRegistration().getPerson().getProfile().changeName(bean.getGivenNames(), bean.getFamilyNames(), null);
        RegistryDiplomaRequest registry = getRegistration().getRegistryDiplomaRequest(bean.getRequestedCycle());
        DiplomaRequest diploma = getRegistration().getDiplomaRequest(bean.getRequestedCycle());
        if (registry == null && diploma == null) {
            throw new DomainException(
                    "error.diplomaSupplementRequest.cannotAskForSupplementWithoutEitherRegistryDiplomaOrDiplomaRequest");
        }
        final DiplomaSupplementRequest supplement = getRegistration().getDiplomaSupplementRequest(bean.getRequestedCycle());
        if (supplement != null && supplement != this) {
            throw new DomainException("error.diplomaSupplementRequest.alreadyRequested");
        }
    }

    @Override
    public void setRequestedCycle(CycleType requestedCycle) {
        throw new DomainException("error.diplomaSupplementRequest.cannotModifyRequestedCycle");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    final public String getDescription() {
        final DegreeType degreeType = getDegreeType();
        final CycleType requestedCycle = getRequestedCycle();

        return getDescription(getAcademicServiceRequestType(),
                getDocumentRequestType().getQualifiedName() + "." + degreeType.name()
                        + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
    }

    @Override
    public String getDocumentTemplateKey() {
        String result = getClass().getName() + "." + getDegreeType().getName();
        if (getRequestedCycle() != null) {
            result += "." + getRequestedCycle().name();
        }
        return result;
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
            if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
                throw new DomainException("error.diplomaSupplement.registration.not.submited.to.conclusion.process");
            }
            if (getRegistryCode() == null) {
                RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getRequestedCycle());
                DiplomaRequest diploma = getRegistration().getDiplomaRequest(getRequestedCycle());
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
        return getRegistration().getGraduateTitle(getRequestedCycle(), locale);
    }

    @Override
    public Integer getRegistrationNumber() {
        return getRegistration().getNumber();
    }

    @Override
    public String getPrevailingScientificArea(final Locale locale) {
        Degree degree = getDegree();
        ExecutionYear conclusion =
                getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
                        .getConclusionYear();
        return degree.getFilteredName(conclusion, locale);
    }

    @Override
    public double getEctsCredits() {
        ExecutionYear conclusion = getConclusionYear();

        return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getDefaultEcts(conclusion);
    }

    @Override
    public DegreeOfficialPublication getDegreeOfficialPublication() {
        CycleConclusionProcess conclusionProcess =
                getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess();

        DegreeOfficialPublication dr =
                getRegistration().getDegree().getOfficialPublication(
                        conclusionProcess.getConclusionDate().toDateTimeAtStartOfDay());

        return dr;
    }

    @Override
    public Integer getFinalAverage() {
        return getRegistration().getFinalAverage(getRequestedCycle());
    }

    @Override
    public String getFinalAverageQualified(final Locale locale) {
        Integer finalAverage = getRegistration().getFinalAverage(getRequestedCycle());
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
    public ExecutionYear getConclusionYear() {
        return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
                .getConclusionYear();
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
