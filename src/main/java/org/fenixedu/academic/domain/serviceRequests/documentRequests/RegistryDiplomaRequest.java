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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.RegistryDiplomaRequestEvent;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.joda.time.LocalDate;

import java.util.Locale;
import java.util.Set;

public class RegistryDiplomaRequest extends RegistryDiplomaRequest_Base implements IRegistryDiplomaRequest,
        IRectorateSubmissionBatchDocumentEntry {

    public RegistryDiplomaRequest() {
        super();
    }

    public RegistryDiplomaRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);
        checkParameters(bean);
        setProgramConclusion(bean.getProgramConclusion());

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

        if (bean.getProgramConclusion() == null) {
            throw new DomainException("error.program.conclusion.empty");
        }

        if (getRegistration().getDiplomaRequest(bean.getProgramConclusion()) != null) {
            throw new DomainException("error.registryDiploma.alreadyHasDiplomaRequest");
        }
        if (getRegistration().getRegistryDiplomaRequest(bean.getProgramConclusion()) != null) {
            throw new DomainException("error.registryDiploma.alreadyRequested");
        }
        if (hasPersonalInfo() && hasMissingPersonalInfo()) {
            throw new DomainException("AcademicServiceRequest.has.missing.personal.info");
        }
    }

    @Override
    final public String getDescription() {
        return Joiner.on(" : ").join(super.getDescription(getAcademicServiceRequestType()),
                getProgramConclusion().getName().getContent());
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

    public static Set<EventType> getPossibleEventTypes() {
        return ImmutableSet.of(EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST,
                EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST,
                EventType.BOLONHA_ADVANCED_FORMATION_REGISTRY_DIPLOMA_REQUEST);
    }

    @Override
    public EventType getEventType() {
        final SetView<EventType> eventTypesToUse =
                Sets.intersection(getPossibleEventTypes(), getProgramConclusion().getEventTypes().getTypes());

        if (eventTypesToUse.size() != 1) {
            throw new DomainException("error.program.conclusion.many.event.types");
        }

        return eventTypesToUse.iterator().next();
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
        if (academicServiceRequestBean.isToProcess()) {

            if (!getProgramConclusion().isConclusionProcessed(getRegistration())) {
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
    public CycleType getRequestedCycle() {
        return getProgramConclusion().groupFor(getRegistration()).filter(CurriculumGroup::isCycleCurriculumGroup)
                .map(cg -> ((CycleCurriculumGroup) cg).getCycleType()).orElse(null);
    }

    @Override
    public LocalDate getConclusionDate() {
        return getProgramConclusion().groupFor(getRegistration()).map(CurriculumGroup::getConclusionProcess)
                .map(ConclusionProcess::getConclusionDate).orElse(null);
    }

    @Override
    public ExecutionYear getConclusionYear() {
        return getProgramConclusion().groupFor(getRegistration()).map(CurriculumGroup::getConclusionProcess)
                .map(ConclusionProcess::getConclusionYear).orElse(null);
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        return getProgramConclusion().groupFor(getRegistration())
                .map(cg -> cg.getDegreeModule().getGraduateTitle(getConclusionYear(), locale)).orElse(null);
    }

    @Override
    public String getFinalAverage(final Locale locale) {
        return getRegistration().getFinalGrade(getProgramConclusion()).getValue();
    }

    @Override
    public String getQualifiedAverageGrade(final Locale locale) {
        Integer finalGrade = getRegistration().getFinalGrade(getProgramConclusion()).getIntegerValue();

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
    public String getProgrammeTypeDescription() {
        return getDegreeType().getName().getContent();
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

    @Override
    public String getDegreeName(final Locale locale, ExecutionYear year) {
        return getDegree().getFilteredName(year, locale);
    }

    @Override
    public String getThesisTitle(Locale locale) {
        return null;
    }
}
