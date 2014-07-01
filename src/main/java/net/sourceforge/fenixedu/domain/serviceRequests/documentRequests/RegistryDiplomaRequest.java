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
package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.RegistryDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
            if (!isFree() && !hasEvent() && !isPayedUponCreation()) {
                RegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
            }
            if (getRegistration().isBolonha() && getDiplomaSupplement().isConcludedSituationAccepted()) {
                getDiplomaSupplement().conclude();
            }
        } else if (academicServiceRequestBean.isToCancelOrReject()) {
            if (hasEvent()) {
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
        StringBuilder result = new StringBuilder();

        CycleType cycleType = getRequestedCycle();
        Degree degree = getDegree();
        final DegreeType degreeType = getDegreeType();
        result.append(degreeType.getGraduateTitle(cycleType, getLanguage()));
        final String degreeFilteredName = degree.getFilteredName(getConclusionYear(), getLanguage());
        result.append(" ").append(BundleUtil.getString(Bundle.APPLICATION, getLanguage(), "label.in"));
        List<DegreeCurricularPlan> degreeCurricularPlansForYear =
                getDegree().getDegreeCurricularPlansForYear(getConclusionYear());
        if (degreeCurricularPlansForYear.size() == 1) {
            DegreeCurricularPlan dcp = degreeCurricularPlansForYear.iterator().next();
            CycleCourseGroup cycleCourseGroup = dcp.getCycleCourseGroup(cycleType);
            if (cycleCourseGroup != null) {
                final MultiLanguageString mls = cycleCourseGroup.getGraduateTitleSuffix();
                final String suffix = mls == null ? null : mls.getContent(getLanguage());
                if (!StringUtils.isEmpty(suffix) && !degreeFilteredName.contains(suffix.trim())) {
                    result.append(" ").append(suffix);
                    result.append(" ").append("-");
                }
            }
        }
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

    @Deprecated
    public boolean hasRequestedCycle() {
        return getRequestedCycle() != null;
    }

    @Deprecated
    public boolean hasDiplomaSupplement() {
        return getDiplomaSupplement() != null;
    }

}
