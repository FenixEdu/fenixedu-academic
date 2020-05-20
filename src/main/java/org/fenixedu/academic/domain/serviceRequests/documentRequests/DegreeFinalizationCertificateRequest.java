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

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.DegreeFinalizationCertificateRequestEvent;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.IProgramConclusionRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.student.MobilityProgram;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import pt.ist.fenixframework.Atomic;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base implements
        IProgramConclusionRequest {

    protected DegreeFinalizationCertificateRequest() {
        super();
    }

    public DegreeFinalizationCertificateRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setAverage(bean.getAverage());
        super.setDetailed(bean.getDetailed());
        super.setMobilityProgram(bean.getMobilityProgram());
        super.setTechnicalEngineer(bean.getTechnicalEngineer());
        super.setInternshipAbolished(bean.getInternshipAbolished());
        super.setInternshipApproved(bean.getInternshipApproved());
        super.setStudyPlan(bean.getStudyPlan());
        super.setBranch(bean.getBranchName());
        super.setExceptionalConclusionDate(bean.getExceptionalConclusionDate());
        super.setLanguage(bean.getLanguage());
        super.setProgramConclusion(bean.getProgramConclusion());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getAverage() == null) {
            throw new DomainException("DegreeFinalizationCertificateRequest.average.cannot.be.null");
        }

        if (bean.getDetailed() == null) {
            throw new DomainException("DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
        }

        if (bean.getMobilityProgram() != null && bean.isIgnoreExternalEntries()) {
            throw new DomainException("ApprovementCertificateRequest.cannot.ignore.external.entries.within.a.mobility.program");
        }

        if ((bean.getInternshipAbolished() || bean.getInternshipApproved() || bean.getStudyPlan())
                && bean.getExceptionalConclusionDate() == null) {
            throw new DomainException(
                    "DegreeFinalizationCertificateRequest.must.indicate.date.for.exceptional.conclusion.situation");
        }

        if (bean.getLanguage() == null) {
            throw new DomainException("DegreeFinalizationCertificateRequest.missing.language");
        }

        if (bean.getProgramConclusion() == null) {
            throw new DomainException("error.program.conclusion.empty");
        }

        checkSpecificConditions(bean.getProgramConclusion());
    }

    protected void checkSpecificConditions(ProgramConclusion programConclusion) {
        if (!programConclusion.getGraduationTitle().isEmpty()) {
            checkForDiplomaRequest(getRegistration(), programConclusion);
        } else {
            if (!programConclusion.isConclusionProcessed(getRegistration())) {
                throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
            }
        }
    }

    static public void checkForDiplomaRequest(final Registration registration, final ProgramConclusion programConclusion) {
        final DiplomaRequest diplomaRequest = registration.getDiplomaRequest(programConclusion);
        final PastDiplomaRequest pastDiplomaRequest = registration.getPastDiplomaRequest();
        if (diplomaRequest == null) {
            if (pastDiplomaRequest == null) {
                checkForRegistryRequest(registration, programConclusion);
            }
        } else if (diplomaRequest.isPayedUponCreation() && diplomaRequest.getEvent() != null
                && !diplomaRequest.getEvent().isPayed()) {
            throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutPayedDiplomaRequest");
        }
    }

    static public void checkForRegistryRequest(final Registration registration, final ProgramConclusion programConclusion) {
        final RegistryDiplomaRequest registryRequest = registration.getRegistryDiplomaRequest(programConclusion);
        if (registryRequest == null) {
            throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutRegistryRequest");
        } else if (registryRequest.isPayedUponCreation() && registryRequest.getEvent() != null
                && !registryRequest.getEvent().isPayed()) {
            throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutPayedRegistryRequest");
        }
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        if (academicServiceRequestBean.isToProcess()) {
            checkSpecificConditions(getProgramConclusion());

            if (!getProgramConclusion().isConclusionProcessed(getRegistration())) {
                throw new DomainException("DegreeFinalizationCertificateRequest.registration.not.submited.to.conclusion.process");
            }

            final RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getProgramConclusion());
            if (registryRequest != null
                    && registryRequest.getAcademicServiceRequestSituationType().compareTo(
                            AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY) < 0) {
                throw new DomainException(
                        "DegreeFinalizationCertificateRequest.registration.registryRequestIsNotSentToExternalEntity");
            }

            if (hasPersonalInfo() && hasMissingPersonalInfo()) {
                throw new DomainException("AcademicServiceRequest.has.missing.personal.info");
            }
        }

        if (academicServiceRequestBean.isToConclude()) {
            tryConcludeServiceRequest(academicServiceRequestBean);
        }

        if (academicServiceRequestBean.isToCancelOrReject() && getEvent() != null) {
            getEvent().cancel(academicServiceRequestBean.getResponsible());
        }

        if (academicServiceRequestBean.isToDeliver()) {
            if (isPayable() && !isPayed()) {
                throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
            }
        }
    }

    @Override
    final public String getDescription() {
        return Joiner.on(" : ").join(BundleUtil.getString(Bundle.ENUMERATION, getDocumentRequestType().name()),
                getProgramConclusion().getName().getContent());
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public void setAverage(final Boolean average) {
        throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

    @Override
    final public void setDetailed(final Boolean detailed) {
        throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.detailed");
    }

    @Override
    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        throw new DomainException("error.DegreeFinalizationCertificateRequest.cannot.modify");
    }

    @Override
    public void setTechnicalEngineer(Boolean technicalEngineer) {
        throw new DomainException("error.DegreeFinalizationCertificateRequest.cannot.modify");
    }

    @Override
    public void setInternshipAbolished(Boolean internshipAbolished) {
        throw new DomainException("error.DegreeFinalizationCertificateRequest.cannot.modify");
    }

    @Override
    public void setInternshipApproved(Boolean internshipApproved) {
        throw new DomainException("error.DegreeFinalizationCertificateRequest.cannot.modify");
    }

    @Override
    public void setExceptionalConclusionDate(YearMonthDay exceptionalConclusionDate) {
        throw new DomainException("error.DegreeFinalizationCertificateRequest.cannot.modify");
    }

    @Override
    public void setStudyPlan(Boolean studyPlan) {
        throw new DomainException("error.DegreeFinalizationCertificateRequest.cannot.modify");
    }

    @Override
    public CycleType getRequestedCycle() {
        return getProgramConclusion().groupFor(getRegistration()).filter(CurriculumGroup::isCycleCurriculumGroup)
                .map(ccg -> ((CycleCurriculumGroup) ccg).getCycleType()).orElse(null);
    }

    public static Set<EventType> getPossibleEventTypes() {
        return ImmutableSet.of(EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST);
    }

    @Override
    final public EventType getEventType() {
        return EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
        return getDetailed() ? getEntriesToReport().size() : 0;
    }

    final private RegistrationConclusionBean getBean() {
        return new RegistrationConclusionBean(getRegistration(), getProgramConclusion());
    }

    final public boolean hasExceptionalConclusionDate() {
        return getExceptionalConclusionDate() != null;
    }

    final public boolean hasExceptionalConclusionInfo() {
        return getTechnicalEngineer() || getInternshipAbolished() || getInternshipApproved() || getStudyPlan();
    }

    final public boolean mustHideConclusionDate() {
        return getInternshipAbolished() || getInternshipApproved()
                || (getStudyPlan() && getRegistration().isFirstCycleAtributionIngression());
    }

    final public YearMonthDay getConclusionDate() {
        return getBean().getConclusionDate();
    }

    final public Grade getFinalAverageGrade() {
        return getBean().getFinalGrade();
    }
    final public Integer getFinalAverage() {
        return getFinalAverageGrade().getIntegerValue();
    }

    final public double getEctsCredits() {
        return getBean().getEctsCredits();
    }

    final public ICurriculum getCurriculum() {
        return getBean().getCurriculumForConclusion();
    }

    final public Collection<ICurriculumEntry> getEntriesToReport() {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        for (final ICurriculumEntry entry : getCurriculum().getCurriculumEntries()) {
            if (entry instanceof Dismissal) {
                final Dismissal dismissal = (Dismissal) entry;
                if (dismissal.getCredits().isEquivalence()
                        || (dismissal.isCreditsDismissal() && !dismissal.getCredits().isSubstitution())) {
                    continue;
                }
            }

            result.add(entry);
        }

        return result;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    public RegistryCode getRegistryCode() {
        RegistryDiplomaRequest registry = getRegistration().getRegistryDiplomaRequest(getProgramConclusion());
        return registry != null ? registry.getRegistryCode() : null;
    }

    @Atomic
    @Override
    public void revertToProcessingState() {
        check(this, AcademicPredicates.SERVICE_REQUESTS_REVERT_TO_PROCESSING_STATE);
        internalRevertToProcessingState();
    }

    @Override
    protected void createCertificateRequestEvent() {
        new DegreeFinalizationCertificateRequestEvent(getAdministrativeOffice(), getRegistration().getPerson(), this);
    }

    public ExecutionYear getConclusionYear() {
        return getProgramConclusion().groupFor(getRegistration()).map(CurriculumGroup::getConclusionProcess)
                .map(ConclusionProcess::getConclusionYear).orElse(null);
    }

    @Override
    public String getGraduateTitle(Locale locale) {
        if (!getProgramConclusion().getGraduationTitle().isEmpty()) {
            return getRegistration().getGraduateTitle(getProgramConclusion(), getLanguage());
        }
        return StringUtils.EMPTY;
    }

}
