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
/**
 \ * Copyright © 2002 Instituto Superior Técnico
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

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.DiplomaRequestEvent;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.IDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.joda.time.LocalDate;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class DiplomaRequest extends DiplomaRequest_Base implements IDiplomaRequest, IRectorateSubmissionBatchDocumentEntry {

    public DiplomaRequest() {
        super();
    }

    public DiplomaRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);

        setProgramConclusion(bean.getProgramConclusion());
        RegistryCode code = bean.getRegistryCode();
        if (code != null) {
            setRegistryCode(code);
        } else {
            getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
        }
        if (isPayedUponCreation() && !isFree()) {
            DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
        }
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getProgramConclusion() == null) {
            throw new DomainException("error.program.conclusion.empty");
        }

        RegistryCode code = bean.getRegistryCode();

        if (code != null) {
            if (code.getDiploma() != null) {
                throw new DomainException("DiplomaRequest.registration.diplomaAlreadyExistsForCode");
            }
            final RegistryDiplomaRequest registryDiplomaRequest = code.getRegistryDiploma();
            if (registryDiplomaRequest == null) {
                throw new DomainException("DiplomaRequest.registration.withoutPayedRegistryRequest");
            }
            if (registryDiplomaRequest.isPayedUponCreation() && registryDiplomaRequest.getEvent() != null
                    && !registryDiplomaRequest.getEvent().isPayed()) {
                throw new DomainException("DiplomaRequest.registration.withoutPayedRegistryRequest");
            }
        } else if (getRegistration().isBolonha() && !getRegistration().getDegreeType().isAdvancedFormationDiploma()
                && !getRegistration().getDegreeType().isAdvancedSpecializationDiploma()) {
            throw new DomainException("DiplomaRequest.registration.withoutRegistryCode");
        }
    }

    @Override
    final public String getDescription() {
        return Joiner.on(" : ").join(super.getDescription(getAcademicServiceRequestType()),
                getProgramConclusion().getName().getContent());
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DIPLOMA_REQUEST;
    }

    @Override
    final public String getDocumentTemplateKey() {

        String key = getClass().getName();

        String suffix = null;

        if (getDegreeType().isPreBolonhaMasterDegree()) {
            suffix = "preBolonhaMasterDegree";
        }

        if (getDegreeType().isAdvancedFormationDiploma()) {
            suffix = "advancedFormationDiploma";
        }

        if (getDegreeType().isAdvancedSpecializationDiploma()) {
            suffix = "advancedSpecializationDiploma";
        }

        if (!Strings.isNullOrEmpty(suffix)) {
            return key + "." + suffix;
        }

        return key;
    }

    public static Set<EventType> getPossibleEventTypes() {
        return ImmutableSet.of(EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST, EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST,
                EventType.BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST, EventType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA_REQUEST);
    }

    @Override
    final public EventType getEventType() {
        final Set<EventType> eventTypesToUse =
                Sets.intersection(getPossibleEventTypes(), getProgramConclusion().getEventTypes().getTypes());

        if (eventTypesToUse.size() != 1) {
            throw new DomainException("error.program.conclusion.many.event.types");
        }

        return eventTypesToUse.iterator().next();
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return AcademicServiceRequestType.DIPLOMA_REQUEST;
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToProcess()) {
            if (getRegistration().getDegreeType().isSpecializationDegree()) {
                throw new DomainException("DiplomaRequest.diploma.not.available");
            }

            if (!getProgramConclusion().isConclusionProcessed(getRegistration())) {
                throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
            }

            if (hasDissertationTitle() && !getRegistration().hasDissertationThesis()) {
                throw new DomainException("DiplomaRequest.registration.doesnt.have.dissertation.thesis");
            }

            if (!getFreeProcessed()) {
                if (hasCurriculumGroup()) {
                    assertPayedEvents(getCurriculumGroup().getIEnrolmentsLastExecutionYear());
                } else {
                    assertPayedEvents();
                }
            }

            if (isPayable() && !isPayed()) {
                throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
            }

            if (!getRegistration().getDegreeType().isAdvancedFormationDiploma() && !getRegistration().getDegreeType()
                    .isAdvancedSpecializationDiploma()) {
                getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
            }

            if (getLastGeneratedDocument() == null) {
                generateDocument();
            }
        }

        if (academicServiceRequestBean.isToConclude() && !isFree() && getEvent() == null && !isPayedUponCreation()) {
            DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
        }

        if (academicServiceRequestBean.isToCancelOrReject() && getEvent() != null && getEvent().isOpen()) {
            getEvent().cancel(academicServiceRequestBean.getResponsible());
        }
    }

    final public boolean hasFinalAverageDescription() {
        return !hasDissertationTitle();
    }

    final public boolean hasDissertationTitle() {
        return getDegreeType().isPreBolonhaMasterDegree();
    }

    @Override
    public CycleType getRequestedCycle() {
        return getProgramConclusion().groupFor(getRegistration()).filter(CurriculumGroup::isCycleCurriculumGroup)
                .map(cg -> ((CycleCurriculumGroup) cg).getCycleType()).orElse(null);
    }

    public CurriculumGroup getCurriculumGroup() {
        final Registration registration = getRegistration();

        if (getProgramConclusion() == null) {
            if (registration.getDegreeType().hasExactlyOneCycleType()) {
                return registration.getLastStudentCurricularPlan().getLastOrderedCycleCurriculumGroup();
            } else {
                return null;
            }
        }

        return getProgramConclusion().groupFor(getRegistration()).orElse(null);
    }

    public boolean hasCurriculumGroup() {
        return getCurriculumGroup() != null;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    public boolean isPagedDocument() {
        return false;
    }

    @Override
    public boolean isToPrint() {
        return false;
    }

    public void addToRectorateBatch() {
        getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
        if (getLastGeneratedDocument() == null) {
            generateDocument();
        }
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
        // FIXME: diplomas should be intended for official purposes and those
        // imply external entity signature. DFAs should therefore be another
        // type of document with a specific workflow, the document is completely
        // different anyway.
        return getDegree() == null || getDegreeType() == null || !getDegreeType().isAdvancedFormationDiploma();
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
        // FIXME: see isPossibleToSendToOtherEntity()
        return getDegree() == null || getDegreeType() == null || !getDegreeType().isAdvancedFormationDiploma();
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
        return false;
    }

    @Override
    public boolean isPayedUponCreation() {
        return !getDegreeType().isAdvancedFormationDiploma();
    }

    @Override
    public boolean isManuallySentToRectorate() {
        return isSendToExternalEntitySituationAccepted() && !getProgramConclusion().getGraduationTitle().isEmpty() && (
                getRegistration().getDegreeType().isAdvancedFormationDiploma() || getRegistration().getDegreeType()
                        .isAdvancedSpecializationDiploma());
    }

    @Override
    public void revertToProcessingState() {
        check(this, AcademicPredicates.SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE);
        internalRevertToProcessingState();
    }

    public boolean hasRegistryDiplomaRequest() {
        return getRegistration().getRegistryDiplomaRequest(getProgramConclusion()) != null;
    }

    @Override
    public LocalDate getConclusionDate() {
        final RegistrationConclusionBean registrationConclusionBean =
                new RegistrationConclusionBean(getRegistration(), getProgramConclusion());

        return registrationConclusionBean.getConclusionDate().toLocalDate();
    }

    @Override
    public Integer getFinalAverage() {
        final RegistrationConclusionBean registrationConclusionBean =
                new RegistrationConclusionBean(getRegistration(), getProgramConclusion());

        return registrationConclusionBean.getFinalGrade().getIntegerValue();
    }

    @Override
    public String getFinalAverageQualified() {
        return getRegistration().getDegree().getGradeScale().getQualifiedName(getFinalAverage().toString());
    }

    @Override
    public String getDissertationThesisTitle() {
        return getRegistration().getDissertationThesisTitle();
    }

    public ExecutionYear getConclusionYear() {
        return getProgramConclusion().groupFor(getRegistration()).map(CurriculumGroup::getConclusionProcess)
                .map(ConclusionProcess::getConclusionYear).orElse(null);
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        return getProgramConclusion().groupFor(getRegistration())
                .map(cg -> cg.getDegreeModule().getGraduateTitle(getConclusionYear(), locale)).orElse(null);
    }

    public String getDegreeFilteredName() {
        return getDegree().getFilteredName(getConclusionYear(), getLanguage());
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

}
