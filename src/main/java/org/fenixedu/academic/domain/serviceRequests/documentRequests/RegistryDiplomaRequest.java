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

import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.RegistryDiplomaRequestEvent;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class RegistryDiplomaRequest extends RegistryDiplomaRequest_Base implements IRegistryDiplomaRequest,
        IRectorateSubmissionBatchDocumentEntry {

    public RegistryDiplomaRequest() {
        super();
    }

    public RegistryDiplomaRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);
        checkParameters(bean);
        if (isPayedUponCreation() && !isFree()) {
            RegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
        }
        if (bean.getRegistration().isBolonha()) {
            setDiplomaSupplement(new DiplomaSupplementRequest(bean));
        }
    }

    public String getFamilyNames() {
        return getRegistration().getPerson().getFamilyNames();
    }

    public String getGivenNames() {
        return getRegistration().getPerson().getGivenNames();
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {
        if (bean.getHasCycleTypeDependency()) {
            if (bean.getRequestedCycle() == null) {
                throw new DomainException("error.registryDiploma.requestedCycleMustBeGiven");
            } else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
                throw new DomainException("error.registryDiploma.requestedCycleTypeIsNotAllowedForGivenStudentCurricularPlan");
            }
            super.setRequestedCycle(bean.getRequestedCycle());
        } else {
            if (bean.getRegistration().getDegreeType().hasExactlyOneCycleType()) {
                super.setRequestedCycle(bean.getRegistration().getDegreeType().getCycleType());
            }
        }
        if (getRegistration().getDiplomaRequest(bean.getRequestedCycle()) != null) {
            throw new DomainException("error.registryDiploma.alreadyHasDiplomaRequest");
        }
        if (getRegistration().getRegistryDiplomaRequest(bean.getRequestedCycle()) != this) {
            throw new DomainException("error.registryDiploma.alreadyRequested");
        }
        if (hasPersonalInfo() && hasMissingPersonalInfo()) {
            throw new DomainException("AcademicServiceRequest.has.missing.personal.info");
        }
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
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    public EventType getEventType() {
        switch (getDegreeType()) {
        case DEGREE:
        case BOLONHA_DEGREE:
            return EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST;
        case MASTER_DEGREE:
        case BOLONHA_MASTER_DEGREE:
            return EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST;
        case BOLONHA_INTEGRATED_MASTER_DEGREE:
            return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST : EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST;
        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
            return EventType.BOLONHA_ADVANCED_FORMATION_REGISTRY_DIPLOMA_REQUEST;
        default:
            throw new DomainException("error.registryDiploma.notAvailableForGivenDegreeType");
        }
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
        if (academicServiceRequestBean.isToProcess()) {
            if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
                throw new DomainException("error.registryDiploma.registrationNotSubmitedToConclusionProcess");
            }
            if (!getFreeProcessed()) {
                assertPayedEvents();
            }
            if (isPayable() && !isPayed()) {
                throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
            }
            if (getRegistryCode() == null) {
                getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
                getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
            }
            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
            if (getRegistration().isBolonha()) {
                getDiplomaSupplement().process();
            }
        } else if (academicServiceRequestBean.isToConclude()) {
            if (!isFree() && getEvent() == null && !isPayedUponCreation()) {
                RegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
            }
            if (getRegistration().isBolonha() && getDiplomaSupplement().isConcludedSituationAccepted()) {
                getDiplomaSupplement().conclude();
            }
        } else if (academicServiceRequestBean.isToCancelOrReject()) {
            if (getEvent() != null) {
                getEvent().cancel(academicServiceRequestBean.getResponsible());
            }
            if (getRegistration().isBolonha() && academicServiceRequestBean.isToCancel()) {
                getDiplomaSupplement().cancel(academicServiceRequestBean.getJustification());
            }
            if (getRegistration().isBolonha() && academicServiceRequestBean.isToReject()) {
                getDiplomaSupplement().reject(academicServiceRequestBean.getJustification());
            }
        }
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPagedDocument() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
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
    public boolean isToPrint() {
        return false;
    }

    @Override
    public void setRequestedCycle(CycleType requestedCycle) {
        throw new DomainException("error.registryDiploma.cannotModifyRequestedCycle");
    }

    @Override
    public LocalDate getConclusionDate() {
        if (getRegistration().isBolonha()) {
            return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
                    .getConclusionDate();
        } else {
            return getRegistration().getConclusionProcess().getConclusionDate();
        }
    }

    @Override
    public ExecutionYear getConclusionYear() {
        if (getRegistration().isBolonha()) {
            return getRegistration().getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getConclusionProcess()
                    .getConclusionYear();
        } else {
            return getRegistration().getConclusionProcess().getConclusionYear();
        }
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        CycleType cycleType = getRequestedCycle();
        List<DegreeCurricularPlan> degreeCurricularPlansForYear =
                getDegree().getDegreeCurricularPlansForYear(getConclusionYear());
        if (degreeCurricularPlansForYear.size() == 1) {
            DegreeCurricularPlan dcp = degreeCurricularPlansForYear.iterator().next();
            CycleCourseGroup cycleCourseGroup = dcp.getCycleCourseGroup(cycleType);
            if (cycleCourseGroup != null) {
                return cycleCourseGroup.getGraduateTitle(getConclusionYear(), getLanguage());
            }
        }

        StringBuilder result = new StringBuilder();
        Degree degree = getDegree();
        final DegreeType degreeType = getDegreeType();
        result.append(degreeType.getGraduateTitle(cycleType, getLanguage()));
        final String degreeFilteredName = degree.getFilteredName(getConclusionYear(), getLanguage());
        result.append(" ").append(BundleUtil.getString(Bundle.APPLICATION, getLanguage(), "label.in"));
        result.append(" ").append(degreeFilteredName);

        return result.toString();
    }

    @Override
    public String getFinalAverage(final Locale locale) {
        return String.valueOf(getRegistration().getFinalAverage(getRequestedCycle()));
    }

    @Override
    public String getQualifiedAverageGrade(final Locale locale) {
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
    public String getProgrammeTypeDescription() {
        return getDegreeType().getLocalizedName();
    }

    @Override
    public String getViewStudentProgrammeLink() {
        return "/student.do?method=visualizeRegistration&amp;registrationID=" + getRegistration().getExternalId();
    }

    @Override
    public String getReceivedActionLink() {
        return "/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest&amp;academicServiceRequestId="
                + getExternalId();
    }

    @Override
    public boolean isProgrammeLinkVisible() {
        return getRegistration().isAllowedToManageRegistration();
    }

}
