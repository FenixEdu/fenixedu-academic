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
package org.fenixedu.academic.domain.phd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Job;
import org.fenixedu.academic.domain.JobBean;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.QualificationBean;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.PersonalInformationBean;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.alert.PhdAlert;
import org.fenixedu.academic.domain.phd.alert.PhdAlertMessage;
import org.fenixedu.academic.domain.phd.alert.PhdFinalProofRequestAlert;
import org.fenixedu.academic.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import org.fenixedu.academic.domain.phd.alert.PhdRegistrationFormalizationAlert;
import org.fenixedu.academic.domain.phd.alert.PublicPhdMissingCandidacyValidationAlert;
import org.fenixedu.academic.domain.phd.candidacy.PhdCandidacyReferee;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import org.fenixedu.academic.domain.phd.candidacy.PhdThesisSubjectOrderBean;
import org.fenixedu.academic.domain.phd.conclusion.PhdConclusionProcess;
import org.fenixedu.academic.domain.phd.guidance.PhdGuidanceDocument;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AbandonIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AcceptEnrolments;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInCandidacyState;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInThesisDiscussionState;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInWorkDevelopmentState;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddAssistantGuidingInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddCandidacyReferees;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddCustomAlert;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddGuidingInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddGuidingsInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddJobInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddQualification;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddQualifications;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddStudyPlan;
import org.fenixedu.academic.domain.phd.individualProcess.activities.AddStudyPlanEntry;
import org.fenixedu.academic.domain.phd.individualProcess.activities.CancelPhdProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ConcludeIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ConfigurePhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteAssistantGuiding;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteCustomAlert;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteGuidanceDocument;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteGuiding;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteJobInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteQualification;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteStudyPlan;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DeleteStudyPlanEntry;
import org.fenixedu.academic.domain.phd.individualProcess.activities.DissociateRegistration;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditIndividualProcessInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditPersonalInformation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditPhdParticipant;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditQualificationExams;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditStudyPlan;
import org.fenixedu.academic.domain.phd.individualProcess.activities.EditWhenStartedStudies;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ExemptPublicPresentationSeminarComission;
import org.fenixedu.academic.domain.phd.individualProcess.activities.FlunkedPhdProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.NotAdmittedPhdProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.PhdIndividualProgramProcessActivity;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RejectEnrolments;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RemoveCandidacyReferee;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RemoveLastStateOnPhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RequestPublicPresentationSeminarComission;
import org.fenixedu.academic.domain.phd.individualProcess.activities.RequestPublicThesisPresentation;
import org.fenixedu.academic.domain.phd.individualProcess.activities.SendPhdEmail;
import org.fenixedu.academic.domain.phd.individualProcess.activities.SuspendPhdProgramProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.TransferToAnotherProcess;
import org.fenixedu.academic.domain.phd.individualProcess.activities.UploadDocuments;
import org.fenixedu.academic.domain.phd.individualProcess.activities.UploadGuidanceAcceptanceLetter;
import org.fenixedu.academic.domain.phd.individualProcess.activities.UploadGuidanceDocument;
import org.fenixedu.academic.domain.phd.individualProcess.activities.ValidatedByCandidate;
import org.fenixedu.academic.domain.phd.migration.PhdMigrationGuiding;
import org.fenixedu.academic.domain.phd.migration.PhdMigrationIndividualProcessData;
import org.fenixedu.academic.domain.phd.migration.PhdMigrationIndividualProcessDataBean;
import org.fenixedu.academic.domain.phd.migration.PhdMigrationProcess;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaSupplementRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisFinalGrade;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static protected List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new EditPersonalInformation());

        activities.add(new AddQualification());
        activities.add(new AddQualifications());
        activities.add(new DeleteQualification());

        activities.add(new AddJobInformation());
        activities.add(new DeleteJobInformation());

        activities.add(new EditIndividualProcessInformation());

        activities.add(new AddGuidingInformation());
        activities.add(new AddGuidingsInformation());
        activities.add(new DeleteGuiding());
        activities.add(new AddAssistantGuidingInformation());
        activities.add(new DeleteAssistantGuiding());

        activities.add(new CancelPhdProgramProcess());
        activities.add(new NotAdmittedPhdProgramProcess());
        activities.add(new SuspendPhdProgramProcess());
        activities.add(new FlunkedPhdProgramProcess());

        activities.add(new AddCandidacyReferees());
        activities.add(new RemoveCandidacyReferee());
        activities.add(new UploadDocuments());

        activities.add(new AddCustomAlert());
        activities.add(new DeleteCustomAlert());

        activities.add(new ValidatedByCandidate());

        activities.add(new AddStudyPlan());
        activities.add(new EditStudyPlan());
        activities.add(new AddStudyPlanEntry());
        activities.add(new DeleteStudyPlanEntry());
        activities.add(new DeleteStudyPlan());

        activities.add(new EditQualificationExams());

        activities.add(new RequestPublicPresentationSeminarComission());
        activities.add(new ExemptPublicPresentationSeminarComission());

        activities.add(new RequestPublicThesisPresentation());

        activities.add(new AcceptEnrolments());
        activities.add(new RejectEnrolments());

        activities.add(new EditWhenStartedStudies());

        activities.add(new ActivatePhdProgramProcessInCandidacyState());
        activities.add(new ActivatePhdProgramProcessInWorkDevelopmentState());
        activities.add(new ActivatePhdProgramProcessInThesisDiscussionState());
        activities.add(new ConcludeIndividualProgramProcess());

        activities.add(new ConfigurePhdIndividualProgramProcess());
        activities.add(new RemoveLastStateOnPhdIndividualProgramProcess());

        activities.add(new SendPhdEmail());

        activities.add(new DeleteGuidanceDocument());
        activities.add(new UploadGuidanceDocument());
        activities.add(new EditPhdParticipant());
        activities.add(new TransferToAnotherProcess());
        activities.add(new DissociateRegistration());

        activities.add(new UploadGuidanceAcceptanceLetter());

        activities.add(new AbandonIndividualProgramProcess());
    }

    /**
     * Checks whether the specified user is allowed to manage this process.
     */
    @Override
    public boolean isAllowedToManageProcess(User userView) {
        if (userView != null) {
            Set<AcademicProgram> programs = AcademicAccessRule
                    .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_PHD_PROCESSES, userView.getPerson().getUser())
                    .collect(Collectors.toSet());

            return programs.contains(this.getPhdProgram());
        } else {
            return false;
        }
    }

    public boolean isCurrentUserAllowedToManageProcess() {
        return isAllowedToManageProcess(Authenticate.getUser());
    }

    /**
     * Checks whether the specified user has permission to manage this process,
     * as well as its state
     */
    public boolean isAllowedToManageProcessState(User userView) {
        if (!isAllowedToManageProcess(userView)) {
            return false;
        }

        Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_PHD_PROCESS_STATE, userView.getPerson().getUser())
                .collect(Collectors.toSet());

        return programs.contains(this.getPhdProgram());
    }

    public boolean isCurrentUserAllowedToManageProcessState() {
        return isAllowedToManageProcessState(Authenticate.getUser());
    }

    @StartActivity
    static public class CreateCandidacy extends PhdIndividualProgramProcessActivity {

        @Override
        protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
            // no precondition to check
        }

        @Override
        protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess noProcess, User userView,
                Object object) {

            final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) object;
            final Person person = getOrCreatePerson(bean);
            final PhdIndividualProgramProcess createdProcess = new PhdIndividualProgramProcess(bean, person);
            for (PhdThesisSubjectOrderBean thesisSubjectBean : bean.getThesisSubjectBeans()) {
                new ThesisSubjectOrder(thesisSubjectBean.getThesisSubject(), createdProcess, thesisSubjectBean.getOrder());
            }

            Process.createNewProcess(userView, PhdProgramCandidacyProcess.class, new Object[] { bean, person, createdProcess });

            if (bean.hasCandidacyHashCode()) {
                new PublicPhdMissingCandidacyValidationAlert(createdProcess);
            }

            createdProcess.createState(PhdIndividualProgramProcessState.CANDIDACY, person, "");

            return createdProcess;
        }

        protected Person getOrCreatePerson(final PhdProgramCandidacyProcessBean bean) {
            Person result;

            if (!bean.getPersonBean().hasPerson()) {
                result = new Person(bean.getPersonBean());
            } else {
                if (bean.getPersonBean().getPerson().getUser() != null) {
                    result = bean.getPersonBean().getPerson();
                } else {
                    result = bean.getPersonBean().save();
                }
            }
            return result;
        }
    }

    private PhdIndividualProgramProcess(final PhdProgramCandidacyProcessBean bean, final Person person) {
        super();

        checkParameters(person, bean.getExecutionYear());
        setPerson(person);

        setPhdProgramFocusArea(bean.getFocusArea());
        setPhdProgram(bean.getProgram());
        setExternalPhdProgram(bean.getExternalPhdProgram());

        setExecutionYear(bean.getExecutionYear());

        setCollaborationType(bean);
        setThesisTitle(bean.getThesisTitle());

        if (bean.getMigratedProcess()) {
            setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(bean.getCandidacyDate().getYear(),
                    bean.getPhdStudentNumber()));
            setPhdConfigurationIndividualProgramProcess(
                    PhdConfigurationIndividualProgramProcess.createMigratedProcessConfiguration());
        } else {
            setPhdIndividualProcessNumber(
                    PhdIndividualProgramProcessNumber.generateNextForYear(bean.getCandidacyDate().getYear(), null));
            setPhdConfigurationIndividualProgramProcess(PhdConfigurationIndividualProgramProcess.createDefault());
        }

        updatePhdParticipantsWithCoordinators();
    }

    public void removeLastState() {
        if (getStatesSet().size() == 1) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess.has.only.one.state");
        }

        getMostRecentState().delete();
    }

    /*
     * 
     * TODO: Refactor -> Participants should be shared at PhdProcessesManager level, and each PhdProgram should also have
     * phdparticipants as coordinators
     */
    private void updatePhdParticipantsWithCoordinators() {
        for (final Person person : getCoordinatorsFor(getExecutionYear())) {
            if (getParticipant(person) == null) {
                PhdParticipant.getUpdatedOrCreate(this, new PhdParticipantBean().setInternalParticipant(person));
            }
        }
    }

    public void createState(final PhdIndividualProgramProcessState state, final Person person, final String remarks) {
        PhdProgramProcessState.createWithInferredStateDate(this, state, person, remarks);
    }

    private void setCollaborationType(PhdProgramCandidacyProcessBean bean) {
        if (bean.getCollaborationType() != null) {
            setCollaborationType(bean.getCollaborationType());
        } else {
            setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
        }
    }

    private void checkParameters(final Person person, final ExecutionYear executionYear) {

        String[] args = {};
        if (person == null) {
            throw new DomainException("error.phd.PhdIndividualProgramProcess.person.cannot.be.null", args);
        }
        String[] args1 = {};
        if (executionYear == null) {
            throw new DomainException("error.phd.PhdIndividualProgramProcess.executionYear.cannot.be.null", args1);
        }
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return true;
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.PHD, getClass().getSimpleName());
    }

    public String getCollaborationTypeName() {
        return getCollaborationType().getLocalizedName()
                + (isOtherCollaborationType() ? " (" + getOtherCollaborationType() + ")" : "");
    }

    private boolean isOtherCollaborationType() {
        return getCollaborationType() == PhdIndividualProgramCollaborationType.OTHER;
    }

    public PhdIndividualProgramProcess addQualification(final Person person, final QualificationBean bean) {
        final Qualification qualification = new Qualification(getPerson(), bean);
        qualification.setCreator(person);
        return this;
    }

    public PhdIndividualProgramProcess addJobInformation(final Person person, final JobBean bean) {
        final Job job = new Job(getPerson(), bean);
        job.setCreator(person);
        return this;
    }

    public PhdParticipant addGuiding(final PhdParticipantBean bean) {
        PhdParticipant phdParticipant = bean.hasParticipant() ? bean.getParticipant() : createPhdParticipant(bean);
        addGuidings(phdParticipant);
        return phdParticipant;
    }

    public PhdIndividualProgramProcess deleteGuiding(final PhdParticipant guiding) {
        if (getGuidingsSet().contains(guiding)) {
            removeGuidings(guiding);
            guiding.tryDelete();
        }
        return this;
    }

    public PhdParticipant addAssistantGuiding(final PhdParticipantBean bean) {
        PhdParticipant phdParticipant = bean.hasParticipant() ? bean.getParticipant() : createPhdParticipant(bean);
        addAssistantGuidings(phdParticipant);
        return phdParticipant;
    }

    private PhdParticipant createPhdParticipant(final PhdParticipantBean bean) {
        return PhdParticipant.getUpdatedOrCreate(this, bean);
    }

    public PhdIndividualProgramProcess deleteAssistantGuiding(final PhdParticipant assistant) {
        if (getAssistantGuidingsSet().contains(assistant)) {
            removeAssistantGuidings(assistant);
            assistant.tryDelete();
        }
        return this;
    }

    public String getProcessNumber() {
        return getPhdIndividualProcessNumber().getFullProcessNumber();
    }

    public PhdIndividualProgramProcess edit(final User userView, final PhdIndividualProgramProcessBean bean) {

        checkParameters(getPerson(), getExecutionYear());

        if (getCandidacyProcess() != null && !getCandidacyDate().equals(bean.getCandidacyDate())) {
            getCandidacyProcess().executeActivity(userView,
                    org.fenixedu.academic.domain.phd.candidacy.activities.EditCandidacyDate.class.getSimpleName(),
                    bean.getCandidacyDate());
        }

        setPhdProgram(bean.getPhdProgram());
        setPhdProgramFocusArea(bean.getFocusArea());
        setExternalPhdProgram(bean.getExternalPhdProgram());

        for (ThesisSubjectOrder subjectOrder : getThesisSubjectOrdersSet()) {
            subjectOrder.delete();
        }
        for (PhdThesisSubjectOrderBean subjectOrderBean : bean.getThesisSubjectBeans()) {
            new ThesisSubjectOrder(subjectOrderBean.getThesisSubject(), this, subjectOrderBean.getOrder());
        }

        setThesisTitle(bean.getThesisTitle());
        setThesisTitleEn(bean.getThesisTitleEn());
        setCollaborationType(bean.getCollaborationType());

        if (bean.getCollaborationType().needExtraInformation()) {
            String obj = bean.getOtherCollaborationType();
            String[] args = {};
            if (obj == null || obj.isEmpty()) {
                throw new DomainException("error.PhdIndividualProgramProcess.invalid.other.collaboration.type", args);
            }
            setOtherCollaborationType(bean.getOtherCollaborationType());
        }

        setQualificationExamsRequired(bean.getQualificationExamsRequiredBooleanValue());
        setQualificationExamsPerformed(bean.getQualificationExamsPerformedBooleanValue());

        if (getHasStartedStudies()) {
            getCandidacyProcess().setWhenRatified(bean.getWhenRatified());
            setWhenFormalizedRegistration(bean.getWhenFormalizedRegistration());
            setWhenStartedStudies(bean.getWhenStartedStudies());
        }

        getPhdIndividualProcessNumber().edit(bean);

        return this;
    }

    public LocalDate getCandidacyDate() {
        return getCandidacyProcess().getCandidacyDate();
    }

    public boolean isCancelled() {
        return getActiveState() == PhdIndividualProgramProcessState.CANCELLED;
    }

    public Collection<PhdCandidacyReferee> getPhdCandidacyReferees() {
        return getCandidacyProcess().getCandidacyRefereesSet();
    }

    public Collection<Qualification> getQualifications() {
        return getPerson().getAssociatedQualificationsSet();
    }

    public List<Qualification> getQualificationsSortedByAttendedEndDate() {
        final List<Qualification> result = new ArrayList<Qualification>(getQualifications());
        Collections.sort(result, Qualification.COMPARATOR_BY_MOST_RECENT_ATTENDED_END);
        return result;
    }

    public List<PhdProgramProcessDocument> getCandidacyProcessDocuments() {
        return new ArrayList<PhdProgramProcessDocument>(getCandidacyProcess().getLatestDocumentVersions());
    }

    public boolean hasCandidacyProcessDocument(final PhdIndividualProgramDocumentType type) {
        return getCandidacyProcess().getLatestDocumentVersionFor(type) != null;
    }

    public int getCandidacyProcessDocumentsCount(final PhdIndividualProgramDocumentType type) {
        return getCandidacyProcess().getDocumentsCount(type);
    }

    public PhdProgramPublicCandidacyHashCode getCandidacyProcessHashCode() {
        return getCandidacyProcess().getCandidacyHashCode();
    }

    static public List<PhdIndividualProgramProcess> search(Predicate<PhdIndividualProgramProcess> searchPredicate) {
        return search(null, searchPredicate);
    }

    static public List<PhdIndividualProgramProcess> search(final ExecutionYear year,
            final Predicate<PhdIndividualProgramProcess> searchPredicate) {

        return Bennu.getInstance().getPhdIndividualProcessNumbersSet().stream()
                .filter(phdIndividualProgramProcessNumber -> year == null ||
                        phdIndividualProgramProcessNumber.getProcess().getExecutionYear().equals(year))
                .map(PhdIndividualProgramProcessNumber::getProcess)
                .filter(searchPredicate)
                .collect(Collectors.toList());
    }

    private static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public Set<PhdAlert> getActiveAlerts() {
        return getAlertsSet().stream().filter(PhdAlert::isActive).collect(Collectors.toSet());
    }

    public Set<PhdAlertMessage> getUnreadAlertMessagesForLoggedPerson() {
        return getUnreadedAlertMessagesFor(AccessControl.getPerson());
    }

    public Set<PhdAlertMessage> getUnreadedAlertMessagesFor(final Person person) {
        return getAlertMessagesSet().stream().filter(each -> !each.isReaded() && each.isFor(person)).collect(Collectors.toSet());
    }

    public Set<PhdAlertMessage> getAlertMessagesForLoggedPerson() {
        return getAlertMessagesFor(AccessControl.getPerson());
    }

    public Set<PhdAlertMessage> getAlertMessagesFor(Person person) {
        return getAlertMessagesSet().stream().filter(each -> each.isFor(person)).collect(Collectors.toSet());
    }

    public boolean isValidatedByCandidate() {
        return getCandidacyProcess().isValidatedByCandidate();
    }

    public Set<PhdProgramProcessDocument> getStudyPlanRelevantDocuments() {
        return getCandidacyProcess().getStudyPlanRelevantDocuments();
    }

    public boolean isRegistrationFormalized() {
        return getWhenFormalizedRegistration() != null;
    }

    private boolean hasAnyActiveAlertFor(Class<? extends PhdAlert> type) {
        return getActiveAlerts().stream().anyMatch(alert -> alert.getClass().equals(type));
    }

    public boolean hasAnyRegistrationFormalizationActiveAlert() {
        return hasAnyActiveAlertFor(PhdRegistrationFormalizationAlert.class);
    }

    public boolean hasMissingPersonalInformation(ExecutionYear executionYear) {
        if (getPrecedentDegreeInformation(executionYear) != null && getPersonalInformationBean(executionYear).isValid()) {
            return false;
        }

        return true;
    }

    public PrecedentDegreeInformation getPrecedentDegreeInformation(ExecutionYear executionYear) {
        PrecedentDegreeInformation result = null;
        for (PrecedentDegreeInformation precedentDegreeInfo : getPrecedentDegreeInformationsSet()) {
            if (precedentDegreeInfo.getPersonalIngressionData().getExecutionYear().equals(executionYear)
                    && (result == null || (result.getLastModifiedDate().isBefore(precedentDegreeInfo.getLastModifiedDate())))) {
                result = precedentDegreeInfo;
            }
        }
        return result;
    }

    public PersonalInformationBean getPersonalInformationBean(ExecutionYear executionYear) {
        PrecedentDegreeInformation precedentInformation = getPrecedentDegreeInformation(executionYear);

        if (precedentInformation == null) {
            precedentInformation = getLatestPrecedentDegreeInformation();
        }
        if (precedentInformation == null) {
            return new PersonalInformationBean(this);
        }

        return new PersonalInformationBean(precedentInformation);
    }

    public PrecedentDegreeInformation getLatestPrecedentDegreeInformation() {
        TreeSet<PrecedentDegreeInformation> degreeInformations = new TreeSet<PrecedentDegreeInformation>(
                Collections.reverseOrder(PrecedentDegreeInformation.COMPARATOR_BY_EXECUTION_YEAR));
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (PrecedentDegreeInformation pdi : getPrecedentDegreeInformationsSet()) {
            if (!pdi.getExecutionYear().isAfter(currentExecutionYear)) {
                degreeInformations.add(pdi);
            }
        }
        degreeInformations.addAll(getPrecedentDegreeInformationsSet());

        return (degreeInformations.iterator().hasNext()) ? degreeInformations.iterator().next() : null;
    }

    @Override
    public PhdProgramProcessState getMostRecentState() {
        return (PhdProgramProcessState) super.getMostRecentState();
    }

    @Override
    public PhdIndividualProgramProcessState getActiveState() {
        return (PhdIndividualProgramProcessState) super.getActiveState();
    }

    public boolean isActive(Interval interval) {
        List<Interval> activeStatesIntervals = new ArrayList<Interval>();
        Set<PhdProgramProcessState> states = new TreeSet<PhdProgramProcessState>(new Comparator<PhdProcessState>() {
            @Override
            public int compare(PhdProcessState o1, PhdProcessState o2) {
                DateTime o1StateDate = o1.getStateDate() == null ? o1.getWhenCreated() : o1.getStateDate();
                DateTime o2StateDate = o2.getStateDate() == null ? o2.getWhenCreated() : o2.getStateDate();
                int result = o1StateDate.compareTo(o2StateDate);
                return result != 0 ? result : o1.getExternalId().compareTo(o2.getExternalId());
            }
        });
        states.addAll(getStates());
        DateTime beginActiveDate = null;
        for (PhdProgramProcessState state : states) {
            if (state.getType().isActive() && beginActiveDate == null) {
                beginActiveDate = state.getStateDate() == null ? state.getWhenCreated() : state.getStateDate();
            }
            if ((!state.getType().isActive()) && beginActiveDate != null) {
                activeStatesIntervals.add(new Interval(beginActiveDate,
                        state.getStateDate() == null ? state.getWhenCreated() : state.getStateDate()));
                beginActiveDate = null;
            }
        }
        return activeStatesIntervals.stream().anyMatch(interval::overlaps) || (activeStatesIntervals.isEmpty() && beginActiveDate != null);
    }

    public Student getStudent() {
        return getPerson().getStudent();
    }

    public boolean isCoordinatorForPhdProgram(Person person) {
        if (getPhdProgram().getDegree() == null) {
            return false;
        }

        final ExecutionDegree executionDegree = getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan()
                .getExecutionDegreeByAcademicInterval(ExecutionYear.readCurrentExecutionYear().getAcademicInterval());

        if (executionDegree != null) {
            return executionDegree.getCoordinatorsListSet().stream().anyMatch(coordinator -> coordinator.getPerson() == person);
        }

        return false;
    }

    public boolean isGuiderOrAssistentGuider(Person person) {
        return isGuider(person) || isAssistantGuider(person);
    }

    public boolean isGuider(Person person) {
        return getGuidingsSet().stream().anyMatch(guiding -> guiding.isFor(person));
    }

    public boolean isGuider(PhdParticipant participant) {
        return getGuidingsSet().contains(participant);
    }

    public boolean isAssistantGuider(Person person) {
        return getAssistantGuidingsSet().stream().anyMatch(guiding -> guiding.isFor(person));
    }

    public boolean isAssistantGuider(PhdParticipant participant) {
        return getAssistantGuidingsSet().contains(participant);
    }

    public boolean isRegistrationAvailable() {
        return getRegistration() != null && AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_REGISTRATIONS, Authenticate.getUser())
                .anyMatch(p -> p == getRegistration().getDegree());
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
        return this;
    }

    public Set<PhdParticipant> getGuidingsAndAssistantGuidings() {
        final Set<PhdParticipant> result = new HashSet<PhdParticipant>();
        result.addAll(getAssistantGuidingsSet());
        result.addAll(getGuidingsSet());

        return result;
    }

    public void cancelDebts(final Person person) {
        if (getCandidacyProcess() != null && !getCandidacyProcess().hasAnyPayments()) {
            getCandidacyProcess().cancelDebt(person);
        }

        if (getRegistrationFee() != null && !getRegistrationFee().hasAnyPayments() && getRegistrationFee().isOpen()) {
            getRegistrationFee().cancel(person);
        }
    }

    public boolean hasSchoolPartConcluded() {
        boolean concluded =
                getRegistration() != null && (getRegistration().isSchoolPartConcluded() || getRegistration().isConcluded());
        return (getStudyPlan() != null && getStudyPlan().isExempted()) || concluded;
    }

    public boolean hasQualificationExamsToPerform() {
        return isQualificationExamsRequired() && !isQualificationExamsPerformed();
    }

    private boolean isQualificationExamsPerformed() {
        return getQualificationExamsPerformed() != null && getQualificationExamsPerformed();
    }

    private boolean isQualificationExamsRequired() {
        return getQualificationExamsRequired() != null && getQualificationExamsRequired();
    }

    public boolean hasSeminarReportDocument() {
        return getSeminarProcess() != null && getSeminarProcess().hasReportDocument();
    }

    public Set<Person> getCoordinatorsFor(ExecutionYear executionYear) {
        return getPhdProgram().getCoordinatorsFor(executionYear);
    }

    public Set<Person> getResponsibleCoordinatorsFor(ExecutionYear executionYear) {
        return getPhdProgram().getResponsibleCoordinatorsFor(executionYear);
    }

    public boolean hasPhdPublicPresentationSeminarAlert() {
        return hasPhdAlert(PhdPublicPresentationSeminarAlert.class);
    }

    public boolean hasPhdFinalProofRequestAlert() {
        return hasPhdAlert(PhdFinalProofRequestAlert.class);
    }

    protected boolean hasPhdAlert(final Class<? extends PhdAlert> clazz) {
        return getAlertsSet().stream().anyMatch(alert -> clazz.isAssignableFrom(alert.getClass()));
    }

    public PhdParticipant getParticipant(final Person person) {
        return getParticipantsSet().stream().filter(participant -> participant.isFor(person)).findFirst().orElse(null);
    }

    public boolean isParticipant(final Person person) {
        return getParticipant(person) != null;
    }

    public Collection<PhdProcessStateType> getAllPhdProcessStateTypes() {
        final Collection<PhdProcessStateType> result = new HashSet<PhdProcessStateType>();

        if (getCandidacyProcess() != null) {

            result.add(getCandidacyProcess().getActiveState());

            if (getCandidacyProcess().getFeedbackRequest() != null) {
                result.add(getCandidacyProcess().getFeedbackRequest().getActiveState());
            }
        }

        if (getSeminarProcess() != null) {
            result.add(getSeminarProcess().getActiveState());
        }

        if (getThesisProcess() != null) {
            result.add(getThesisProcess().getActiveState());
        }

        return result;
    }

    public Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol() {

        if (getStudyPlan() == null) {
            return Collections.emptySet();
        }

        return getStudyPlan().getEntriesSet().stream()
                .filter(PhdStudyPlanEntry::isInternalEntry)
                .map(entry -> ((InternalPhdStudyPlanEntry) entry).getCompetenceCourse())
                .collect(Collectors.toSet());
    }

    public LocalDate getConclusionDate() {
        if (isConclusionProcessed()) {
            return getLastConclusionProcess().getConclusionDate();
        }

        return getThesisProcess() != null ? getThesisProcess().getConclusionDate() : null;
    }

    public ExecutionYear getConclusionYear() {
        LocalDate conclusionDate = getConclusionDate();
        if (conclusionDate == null) {
            return null;
        }

        int year = conclusionDate.getYear();
        String executionYearName = String.format("%s/%s", year - 1, year);
        return ExecutionYear.readExecutionYearByName(executionYearName);
    }

    public boolean hasCurricularCoursesToEnrol() {
        return getStudyPlan() != null && !getStudyPlan().isExempted() && getStudyPlan().isToEnrolInCurricularCourses();
    }

    public boolean hasPropaeudeuticsOrExtraEntriesApproved() {

        if (getStudyPlan() == null || getRegistration() == null) {
            return false;
        }

        return getStudyPlan().isExempted() || getStudyPlan().hasPropaeudeuticsOrExtraEntriesApproved();
    }

    public boolean isFlunked() {
        return getMostRecentState().isFlunked();
    }

    public boolean isSuspended() {
        return getMostRecentState().isSuspended();
    }

    protected List<PhdProgramProcessState> getActiveStates() {
        return getStates().stream()
                .filter(process -> process.getType().isActive())
                .collect(Collectors.toList());
    }

    protected boolean hasActiveStates() {
        return !getActiveStates().isEmpty();
    }

    public PhdProgramProcessState getLastActiveState() {
        return hasActiveStates() ? Collections.max(getActiveStates(), PhdProcessState.COMPARATOR_BY_DATE) : null;
    }

    public boolean isMigratedProcess() {
        return getPhdConfigurationIndividualProgramProcess().isMigratedProcess();
    }

    public Integer getPhdStudentNumber() {
        return getPhdIndividualProcessNumber().getPhdStudentNumber();
    }

    @Override
    public boolean isProcessIndividualProgram() {
        return true;
    }

    public boolean isConcluded() {
        return getThesisProcess() != null && getThesisProcess().isConcluded();
    }

    public boolean getHasStartedStudies() {
        return this.getActiveStates().stream().anyMatch(state -> state.getType().equals(PhdIndividualProgramProcessState.WORK_DEVELOPMENT));
    }

    public boolean isProcessActive() {
        PhdProgramProcessState mostRecentState = getMostRecentState();
        return mostRecentState != null ? mostRecentState.getType().equals(PhdIndividualProgramProcessState.WORK_DEVELOPMENT)
                || mostRecentState.getType().equals(PhdIndividualProgramProcessState.THESIS_DISCUSSION) : false;
    }

    public PhdThesisFinalGrade getFinalGrade() {
        if (!isConcluded()) {
            return null;
        }

        if (isConclusionProcessed()) {
            return getLastConclusionProcess().getFinalGrade();
        }

        return getThesisProcess().getFinalGrade();
    }

    public Set<PhdGuidanceDocument> getLatestGuidanceDocumentVersions() {
        return getLatestDocumentVersions().stream()
                .filter(process -> process.getDocumentType().isForGuidance())
                .map(process -> ((PhdGuidanceDocument) process))
                .collect(Collectors.toSet());
    }

    public static class PublicPhdIndividualProgramProcess extends PhdIndividualProgramProcess {
        static {
            activities.add(new CreatePublicCandidacy());
        }

        // Prevent instantiation of this class
        private PublicPhdIndividualProgramProcess() {
            super(null, null);
        }

        @StartActivity
        static public class CreatePublicCandidacy extends CreateCandidacy {

            @Override
            protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
                // no precondition to check
            }

            @Override
            protected Person getOrCreatePerson(final PhdProgramCandidacyProcessBean bean) {
                Person result;

                if (!bean.getPersonBean().hasPerson()) {
                    result = new Person(bean.getPersonBean());
                } else {
                    if (bean.getPersonBean().getPerson().getUser() != null) {
                        result = bean.getPersonBean().getPerson();
                    } else {
                        result = bean.getPersonBean().getPerson().editByPublicCandidate(bean.getPersonBean());
                    }
                }
                return result;
            }

            @Override
            protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess noProcess, User userView,
                    Object object) {
                final PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) object;

                if (bean.getPersonBean().hasPerson()) {
                    if (PhdProgramCandidacyProcess.hasOnlineApplicationForPeriod(bean.getPersonBean().getPerson(),
                            bean.getPhdCandidacyPeriod())) {
                        throw new DomainException("error.phd.public.candidacy.fill.personal.information.and.institution.id");
                    }
                }

                return super.executeActivity(noProcess, userView, object);
            }
        }

    }

    public boolean hasAssociatedMigrationProcess() {
        return getAssociatedMigrationProcess() != null;
    }

    public PhdMigrationIndividualProcessData getAssociatedMigrationProcess() {
        if (!isMigratedProcess() || getPhdStudentNumber() == null) {
            return null;
        }

        return Bennu.getInstance().getPhdMigrationProcessesSet().stream()
                .flatMap(migrationProcess -> migrationProcess.getPhdMigrationIndividualProcessDataSet().stream())
                .filter(processData -> processData.getNumber().equals(getPhdStudentNumber()))
                .findFirst().orElse(null);

    }

    public PhdMigrationGuiding getAssociatedMigrationGuiding() {
        if (!hasAssociatedMigrationProcess()) {
            return null;
        }

        final PhdMigrationIndividualProcessDataBean processDataBean = getAssociatedMigrationProcess().getProcessBean();

        return getAssociatedMigrationgGuidingOrAssistant(processDataBean.getGuiderId());
    }

    public PhdMigrationGuiding getAssociatedMigrationAssistantGuiding() {
        if (!hasAssociatedMigrationProcess()) {
            return null;
        }

        final PhdMigrationIndividualProcessDataBean processDataBean = getAssociatedMigrationProcess().getProcessBean();

        return getAssociatedMigrationgGuidingOrAssistant(processDataBean.getAssistantGuiderId());
    }

    private PhdMigrationGuiding getAssociatedMigrationgGuidingOrAssistant(String guiderId) {
        return Bennu.getInstance().getPhdMigrationProcessesSet().stream()
                .flatMap(migrationProcess -> migrationProcess.getPhdMigrationGuidingSet().stream())
                .filter(guidingData -> guidingData.getTeacherId().equals(guiderId))
                .findFirst().orElse(null);

    }

    static public List<PhdMigrationIndividualProcessData> searchMigrationProcesses(final ExecutionYear year,
            final Predicate<PhdMigrationIndividualProcessData> searchPredicate) {
        final List<PhdMigrationIndividualProcessData> processDataList = new ArrayList<PhdMigrationIndividualProcessData>();

        for (final PhdMigrationProcess migrationProcess : Bennu.getInstance().getPhdMigrationProcessesSet()) {
            for (final PhdMigrationIndividualProcessData processData : migrationProcess
                    .getPhdMigrationIndividualProcessDataSet()) {
                final ExecutionYear processYear = processData.getExecutionYear();
                if (processYear == null || year == null || processYear.equals(year)) {
                    processDataList.add(processData);
                }
            }
        }

        return filter(processDataList, searchPredicate);

    }

    public static Collection<PhdMigrationProcess> getMigrationProcesses() {
        return Bennu.getInstance().getPhdMigrationProcessesSet();
    }

    public boolean isTransferable() {
        return getHasStartedStudies() && getDestiny() == null;
    }

    public boolean isTransferred() {
        return PhdIndividualProgramProcessState.TRANSFERRED.equals(getActiveState());
    }

    public boolean isFromTransferredProcess() {
        return getSource() != null;
    }

    public void transferToAnotherProcess(final PhdIndividualProgramProcess destiny, final Person responsible, String remarks) {
        if (!isTransferable()) {
            throw new DomainException("phd.PhdIndividualProgramProcess.cannot.be.transferred");
        }

        if (getRegistration() != null && getRegistration().isConcluded()) {
            throw new DomainException("phd.PhdIndividualProgramProcess.source.registration.is.concluded");
        }

        this.createState(PhdIndividualProgramProcessState.TRANSFERRED, getPerson(), remarks);

        if (getRegistration() != null && getRegistration().isActive()) {
            RegistrationState.createRegistrationState(getRegistration(), responsible, new DateTime(),
                    RegistrationStateType.INTERNAL_ABANDON);
        }

        super.setDestiny(destiny);
        destiny.assignSource(this);
    }

    private void assignSource(PhdIndividualProgramProcess source) {
        if (source.getDestiny() != this) {
            throw new DomainException("phdIndividualProgramProcess.source.has.different.destiny");
        }

        super.setSource(source);
    }

    @Override
    public void setSource(PhdIndividualProgramProcess source) {
        throw new DomainException("phd.PhdIndividualProgramProcess.cannot.modify.source");
    }

    @Override
    public void setDestiny(PhdIndividualProgramProcess destiny) {
        throw new DomainException("phd.PhdIndividualProgramProcess.cannot.modify.destiny");
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebts(final AdministrativeOffice office,
            final ExecutionYear executionYear) {
        return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(office, executionYear);
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(final AdministrativeOffice office,
            final ExecutionYear executionYear) {
        return getPerson().getNotCancelledAdministrativeOfficeFeeAndInsuranceEventsUntil(office, executionYear).stream()
                .anyMatch(AdministrativeOfficeFeeAndInsuranceEvent::isInDebt);
    }

    private boolean hasAnyNotPayedInsuranceEvents() {
        return getPerson().getNotCancelledInsuranceEvents().stream().anyMatch(Event::isInDebt);
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(final AdministrativeOffice office) {
        return getPerson().getNotCancelledAdministrativeOfficeFeeAndInsuranceEvents(office).stream()
                .anyMatch(AdministrativeOfficeFeeAndInsuranceEvent::isInDebt);
    }

    final public boolean hasInsuranceDebtsCurrently() {
        return hasAnyNotPayedInsuranceEvents();
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(final AdministrativeOffice administrativeOffice) {
        return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(administrativeOffice);
    }

    public boolean hasDiplomaRequest() {
        return getPhdAcademicServiceRequestsSet().stream()
                .filter(AcademicServiceRequest::isDiploma)
                .anyMatch(academicServiceRequest -> !academicServiceRequest.isCancelled() && !academicServiceRequest.isRejected());
    }

    public PhdRegistryDiplomaRequest getRegistryDiplomaRequest() {
        return (PhdRegistryDiplomaRequest) getPhdAcademicServiceRequestsSet().stream()
                .filter(AcademicServiceRequest::isRegistryDiploma)
                .filter(academicServiceRequest -> !academicServiceRequest.isCancelled() && !academicServiceRequest.isRejected())
                .findFirst().orElse(null);

    }

    public boolean hasRegistryDiplomaRequest() {
        return getRegistryDiplomaRequest() != null;
    }

    public PhdDiplomaRequest getDiplomaRequest() {
        return (PhdDiplomaRequest) getPhdAcademicServiceRequestsSet().stream()
                .filter(AcademicServiceRequest::isDiploma)
                .filter(academicServiceRequest -> !academicServiceRequest.isCancelled() && !academicServiceRequest.isRejected())
                .findFirst().orElse(null);
    }

    public PhdDiplomaSupplementRequest getDiplomaSupplementRequest() {
        if (!hasRegistryDiplomaRequest()) {
            return null;
        }

        return getRegistryDiplomaRequest().getDiplomaSupplement();
    }

    public PhdConclusionProcess getLastConclusionProcess() {
        if (getPhdConclusionProcessesSet().isEmpty()) {
            return null;
        }

        Set<PhdConclusionProcess> conclusionProcessSet =
                new TreeSet<PhdConclusionProcess>(PhdConclusionProcess.VERSION_COMPARATOR);
        conclusionProcessSet.addAll(getPhdConclusionProcessesSet());
        return conclusionProcessSet.iterator().next();
    }

    public boolean isConclusionProcessed() {
        return !getPhdConclusionProcessesSet().isEmpty();
    }

    public String getGraduateTitle(Locale locale) {
        final StringBuilder stringBuilder =
                new StringBuilder(BundleUtil.getString(Bundle.PHD, locale, "label.phd.graduated.title.in")).append(" ");
        stringBuilder.append(getPhdProgram().getName(getExecutionYear()).getContent(locale));
        return stringBuilder.toString();
    }

    public Boolean isBolonha() {
        return getPhdConfigurationIndividualProgramProcess().getIsBolonha() != null
                && getPhdConfigurationIndividualProgramProcess().getIsBolonha();
    }

    public List<PhdAcademicServiceRequest> getNewAcademicServiceRequests() {
        return getPhdAcademicServiceRequestsSet().stream().filter(AcademicServiceRequest::isNewRequest).collect(Collectors.toList());
    }

    public List<PhdAcademicServiceRequest> getProcessingAcademicServiceRequests() {
        return getPhdAcademicServiceRequestsSet().stream()
                .filter(request -> !request.isNewRequest())
                .filter(request -> !request.isDelivered() && !request.isDeliveredSituationAccepted())
                .filter(request -> !request.isCancelled() && !request.isRejected()).collect(Collectors.toList());
    }

    public List<PhdAcademicServiceRequest> getToDeliverAcademicServiceRequests() {
        return getPhdAcademicServiceRequestsSet().stream()
                .filter(AcademicServiceRequest::isDeliveredSituationAccepted)
                .collect(Collectors.toList());
    }

    public List<PhdAcademicServiceRequest> getHistoricalAcademicServiceRequests() {
        List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

        for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequestsSet()) {
            if (request.isDelivered() || request.isCancelled() || request.isRejected()) {
                result.add(request);
                continue;
            }
        }

        return result;
    }

    public String getThesisTitleForCertificateGeneration() {
        return getThesisTitle();
    }

    public Collection<ThesisSubjectOrder> getThesisSubjectOrdersSorted() {
        TreeSet<ThesisSubjectOrder> subjectOrders = new TreeSet<ThesisSubjectOrder>(ThesisSubjectOrder.COMPARATOR_BY_ORDER);
        subjectOrders.addAll(getThesisSubjectOrdersSet());
        return subjectOrders;
    }

    public int getHighestThesisSubjectOrder() {
        return getThesisSubjectOrdersSet().stream()
                .map(ThesisSubjectOrder_Base::getSubjectOrder).mapToInt(order -> order)
                .filter(order -> order >= 0).max().orElse(0);
    }

    public boolean hasPhdGratuityEventForYear(int year) {
        return getPhdGratuityEventsSet().stream().anyMatch(event -> event.getYear() == year && !event.isCancelled());
    }

    public AdministrativeOffice getAdministrativeOffice() {
        return getPhdProgram() != null ? getPhdProgram().getAdministrativeOffice() : null;
    }

    @Override
    @Deprecated
    public java.util.Set<org.fenixedu.academic.domain.phd.PhdProgramProcessState> getStates() {
        return getStatesSet();
    }

    @Override
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

}
