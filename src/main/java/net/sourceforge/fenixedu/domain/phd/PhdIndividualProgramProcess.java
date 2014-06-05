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
package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Alert;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdFinalProofRequestAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdRegistrationFormalizationAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PublicPhdMissingCandidacyValidationAlert;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdThesisSubjectOrderBean;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent;
import net.sourceforge.fenixedu.domain.phd.guidance.PhdGuidanceDocument;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AbandonIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AcceptEnrolments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInCandidacyState;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInThesisDiscussionState;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ActivatePhdProgramProcessInWorkDevelopmentState;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddAssistantGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddCandidacyReferees;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddCustomAlert;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddGuidingsInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddJobInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddQualification;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddQualifications;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.AddStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.CancelPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ConcludeIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ConfigurePhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteAssistantGuiding;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteCustomAlert;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteGuiding;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteJobInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteQualification;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DeleteStudyPlanEntry;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.DissociateRegistration;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditIndividualProcessInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPersonalInformation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditQualificationExams;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditStudyPlan;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditWhenStartedStudies;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ExemptPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.FlunkedPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.NotAdmittedPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.PhdIndividualProgramProcessActivity;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RejectEnrolments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RemoveCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RemoveLastStateOnPhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.RequestPublicThesisPresentation;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.SendPhdEmail;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.SuspendPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.TransferToAnotherProcess;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadGuidanceAcceptanceLetter;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.UploadGuidanceDocument;
import net.sourceforge.fenixedu.domain.phd.individualProcess.activities.ValidatedByCandidate;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationGuiding;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessDataBean;
import net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.predicates.Predicate;

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
            Set<AcademicProgram> programs =
                    AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.MANAGE_PHD_PROCESSES);

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

        Set<AcademicProgram> programs =
                AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.MANAGE_PHD_PROCESS_STATE);

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
        protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess noProcess, User userView, Object object) {

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
                if (bean.getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)
                        || bean.getPersonBean().getPerson().hasAnyPersonRoles() || bean.getPersonBean().getPerson().hasUser()
                        || bean.getPersonBean().getPerson().hasStudent() || bean.hasInstitutionId()) {
                    result = bean.getPersonBean().getPerson();
                } else {
                    /*
                     * if person never had any identity in the system then let edit information
                     */
                    result = bean.getPersonBean().getPerson().edit(bean.getPersonBean());
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
            setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(
                    bean.getCandidacyDate().getYear(), bean.getPhdStudentNumber()));
            setPhdConfigurationIndividualProgramProcess(PhdConfigurationIndividualProgramProcess
                    .createMigratedProcessConfiguration());
        } else {
            setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(
                    bean.getCandidacyDate().getYear(), null));
            setPhdConfigurationIndividualProgramProcess(PhdConfigurationIndividualProgramProcess.createDefault());
        }

        updatePhdParticipantsWithCoordinators();
    }

    public void removeLastState() {
        if (getStatesSet().size() == 1) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.has.only.one.state");
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

        if (hasCandidacyProcess() && !getCandidacyDate().equals(bean.getCandidacyDate())) {
            getCandidacyProcess().executeActivity(userView,
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.EditCandidacyDate.class.getSimpleName(),
                    bean.getCandidacyDate());
        }

        setPhdProgram(bean.getPhdProgram());
        setPhdProgramFocusArea(bean.getFocusArea());
        setExternalPhdProgram(bean.getExternalPhdProgram());

        for (ThesisSubjectOrder subjectOrder : getThesisSubjectOrders()) {
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
        return getCandidacyProcess().getCandidacyReferees();
    }

    public Collection<Qualification> getQualifications() {
        return getPerson().getAssociatedQualifications();
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

        final Set<PhdIndividualProgramProcess> processesToSearch = new HashSet<PhdIndividualProgramProcess>();
        for (final PhdIndividualProgramProcessNumber phdIndividualProgramProcessNumber : Bennu.getInstance()
                .getPhdIndividualProcessNumbersSet()) {
            if (year == null || phdIndividualProgramProcessNumber.getProcess().getExecutionYear().equals(year)) {
                processesToSearch.add(phdIndividualProgramProcessNumber.getProcess());
            }
        }

        return filter(processesToSearch, searchPredicate);
    }

    private static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        final List<T> result = new ArrayList<T>();
        for (final T each : collection) {
            if (predicate.eval(each)) {
                result.add(each);
            }
        }
        return result;
    }

    public Set<PhdAlert> getActiveAlerts() {
        final Set<PhdAlert> result = new HashSet<PhdAlert>();

        for (final PhdAlert each : getAlerts()) {
            if (each.isActive()) {
                result.add(each);
            }
        }

        return result;
    }

    public Set<PhdAlertMessage> getUnreadAlertMessagesForLoggedPerson() {
        return getUnreadedAlertMessagesFor(AccessControl.getPerson());
    }

    public Set<PhdAlertMessage> getUnreadedAlertMessagesFor(final Person person) {
        final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

        for (final PhdAlertMessage each : getAlertMessages()) {
            if (!each.isReaded() && each.isFor(person)) {
                result.add(each);
            }
        }

        return result;
    }

    public Set<PhdAlertMessage> getAlertMessagesForLoggedPerson() {
        return getAlertMessagesFor(AccessControl.getPerson());
    }

    public Set<PhdAlertMessage> getAlertMessagesFor(Person person) {
        final Set<PhdAlertMessage> result = new HashSet<PhdAlertMessage>();

        for (final PhdAlertMessage each : getAlertMessages()) {
            if (each.isFor(person)) {
                result.add(each);
            }
        }

        return result;
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
        for (final Alert alert : getActiveAlerts()) {
            if (alert.getClass().equals(type)) {
                return true;
            }
        }

        return false;
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
        for (PrecedentDegreeInformation precedentDegreeInfo : getPrecedentDegreeInformations()) {
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
        TreeSet<PrecedentDegreeInformation> degreeInformations =
                new TreeSet<PrecedentDegreeInformation>(
                        Collections.reverseOrder(PrecedentDegreeInformation.COMPARATOR_BY_EXECUTION_YEAR));
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (PrecedentDegreeInformation pdi : getPrecedentDegreeInformations()) {
            if (!pdi.getExecutionYear().isAfter(currentExecutionYear)) {
                degreeInformations.add(pdi);
            }
        }
        degreeInformations.addAll(getPrecedentDegreeInformations());

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
        for (Interval activeInterval : activeStatesIntervals) {
            if (interval.overlaps(activeInterval)) {
                return true;
            }
        }
        return (activeStatesIntervals.size() == 0 && beginActiveDate != null);
    }

    public Student getStudent() {
        return getPerson().getStudent();
    }

    public boolean isCoordinatorForPhdProgram(Person person) {
        if (!getPhdProgram().hasDegree()) {
            return false;
        }

        final ExecutionDegree executionDegree =
                getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan()
                        .getExecutionDegreeByAcademicInterval(ExecutionYear.readCurrentExecutionYear().getAcademicInterval());

        if (executionDegree != null) {
            for (final Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                if (coordinator.getPerson() == person) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isGuiderOrAssistentGuider(Person person) {
        return isGuider(person) || isAssistantGuider(person);
    }

    public boolean isGuider(Person person) {
        for (final PhdParticipant guiding : getGuidings()) {
            if (guiding.isFor(person)) {
                return true;
            }
        }

        return false;
    }

    public boolean isGuider(PhdParticipant participant) {
        return getGuidingsSet().contains(participant);
    }

    public boolean isAssistantGuider(Person person) {
        for (final PhdParticipant guiding : getAssistantGuidings()) {
            if (guiding.isFor(person)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAssistantGuider(PhdParticipant participant) {
        return getAssistantGuidingsSet().contains(participant);
    }

    public boolean isRegistrationAvailable() {
        return hasRegistration()
                && AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_REGISTRATIONS).contains(getRegistration().getDegree());
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
        return this;
    }

    public Set<PhdParticipant> getGuidingsAndAssistantGuidings() {
        final Set<PhdParticipant> result = new HashSet<PhdParticipant>();
        result.addAll(getAssistantGuidings());
        result.addAll(getGuidings());

        return result;
    }

    public void cancelDebts(final Person person) {
        if (hasCandidacyProcess() && !getCandidacyProcess().hasAnyPayments()) {
            getCandidacyProcess().cancelDebt(person);
        }

        if (hasRegistrationFee() && !getRegistrationFee().hasAnyPayments() && getRegistrationFee().isOpen()) {
            getRegistrationFee().cancel(person);
        }
    }

    public boolean hasSchoolPartConcluded() {
        boolean concluded = hasRegistration() && (getRegistration().isSchoolPartConcluded() || getRegistration().isConcluded());
        return (hasStudyPlan() && getStudyPlan().isExempted()) || concluded;
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
        return hasSeminarProcess() && getSeminarProcess().hasReportDocument();
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
        for (final Alert alert : getAlerts()) {
            if (clazz.isAssignableFrom(alert.getClass())) {
                return true;
            }
        }
        return false;
    }

    public PhdParticipant getParticipant(final Person person) {
        for (final PhdParticipant participant : getParticipants()) {
            if (participant.isFor(person)) {
                return participant;
            }
        }
        return null;
    }

    public boolean isParticipant(final Person person) {
        return getParticipant(person) != null;
    }

    public Collection<PhdProcessStateType> getAllPhdProcessStateTypes() {
        final Collection<PhdProcessStateType> result = new HashSet<PhdProcessStateType>();

        if (hasCandidacyProcess()) {

            result.add(getCandidacyProcess().getActiveState());

            if (getCandidacyProcess().hasFeedbackRequest()) {
                result.add(getCandidacyProcess().getFeedbackRequest().getActiveState());
            }
        }

        if (hasSeminarProcess()) {
            result.add(getSeminarProcess().getActiveState());
        }

        if (hasThesisProcess()) {
            result.add(getThesisProcess().getActiveState());
        }

        return result;
    }

    public Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol() {

        if (!hasStudyPlan()) {
            return Collections.emptySet();
        }

        final Collection<CompetenceCourse> result = new HashSet<CompetenceCourse>();
        for (PhdStudyPlanEntry entry : getStudyPlan().getEntries()) {
            if (entry.isInternalEntry()) {
                result.add(((InternalPhdStudyPlanEntry) entry).getCompetenceCourse());
            }
        }
        return result;
    }

    public LocalDate getConclusionDate() {
        if (isConclusionProcessed()) {
            return getLastConclusionProcess().getConclusionDate();
        }

        return hasThesisProcess() ? getThesisProcess().getConclusionDate() : null;
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
        return hasStudyPlan() && !getStudyPlan().isExempted() && getStudyPlan().isToEnrolInCurricularCourses();
    }

    public boolean hasPropaeudeuticsOrExtraEntriesApproved() {

        if (!hasStudyPlan() || !hasRegistration()) {
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
        List<PhdProgramProcessState> result = new ArrayList<PhdProgramProcessState>();
        org.apache.commons.collections.CollectionUtils.select(getStates(), new org.apache.commons.collections.Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((PhdProgramProcessState) arg0).getType().isActive();
            }

        }, result);

        return result;
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
        return hasThesisProcess() && getThesisProcess().isConcluded();
    }

    public boolean getHasStartedStudies() {
        for (PhdProgramProcessState state : this.getActiveStates()) {
            if (state.getType().equals(PhdIndividualProgramProcessState.WORK_DEVELOPMENT)) {
                return true;
            }
        }
        return false;
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
        Set<PhdGuidanceDocument> documents = new HashSet<PhdGuidanceDocument>();

        org.apache.commons.collections.CollectionUtils.select(getLatestDocumentVersions(),
                new org.apache.commons.collections.Predicate() {

                    @Override
                    public boolean evaluate(Object arg0) {
                        return ((PhdProgramProcessDocument) arg0).getDocumentType().isForGuidance();
                    }

                }, documents);

        return documents;
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
                    if (bean.getPersonBean().getPerson().hasRole(RoleType.EMPLOYEE)
                            || bean.getPersonBean().getPerson().hasAnyPersonRoles() || bean.getPersonBean().getPerson().hasUser()
                            || bean.getPersonBean().getPerson().hasStudent() || bean.hasInstitutionId()) {
                        result = bean.getPersonBean().getPerson();
                    } else {
                        /*
                         * if person never had any identity in the system then let edit information
                         */
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

        for (final PhdMigrationProcess migrationProcess : Bennu.getInstance().getPhdMigrationProcessesSet()) {
            for (final PhdMigrationIndividualProcessData processData : migrationProcess.getPhdMigrationIndividualProcessData()) {
                if (processData.getNumber().equals(getPhdStudentNumber())) {
                    return processData;
                }
            }
        }

        return null;
    }

    public PhdMigrationGuiding getAssociatedMigrationGuiding() {
        if (!hasAssociatedMigrationProcess()) {
            return null;
        }

        final PhdMigrationIndividualProcessDataBean processDataBean = getAssociatedMigrationProcess().getProcessBean();

        return getAssociatedMigrationgGuidingOrAssistant(processDataBean.getGuiderNumber());
    }

    public PhdMigrationGuiding getAssociatedMigrationAssistantGuiding() {
        if (!hasAssociatedMigrationProcess()) {
            return null;
        }

        final PhdMigrationIndividualProcessDataBean processDataBean = getAssociatedMigrationProcess().getProcessBean();

        return getAssociatedMigrationgGuidingOrAssistant(processDataBean.getAssistantGuiderNumber());
    }

    private PhdMigrationGuiding getAssociatedMigrationgGuidingOrAssistant(String guiderNumber) {
        for (final PhdMigrationProcess migrationProcess : Bennu.getInstance().getPhdMigrationProcessesSet()) {
            for (final PhdMigrationGuiding guidingData : migrationProcess.getPhdMigrationGuiding()) {
                if (guidingData.getTeacherNumber().equals(guiderNumber)) {
                    return guidingData;
                }
            }
        }

        return null;
    }

    static public List<PhdMigrationIndividualProcessData> searchMigrationProcesses(final ExecutionYear year,
            final Predicate<PhdMigrationIndividualProcessData> searchPredicate) {
        final List<PhdMigrationIndividualProcessData> processDataList = new ArrayList<PhdMigrationIndividualProcessData>();

        for (final PhdMigrationProcess migrationProcess : Bennu.getInstance().getPhdMigrationProcessesSet()) {
            for (final PhdMigrationIndividualProcessData processData : migrationProcess.getPhdMigrationIndividualProcessData()) {
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
        return getHasStartedStudies() && !hasDestiny();
    }

    public boolean isTransferred() {
        return PhdIndividualProgramProcessState.TRANSFERRED.equals(getActiveState());
    }

    public boolean isFromTransferredProcess() {
        return hasSource();
    }

    public void transferToAnotherProcess(final PhdIndividualProgramProcess destiny, final Person responsible, String remarks) {
        if (!isTransferable()) {
            throw new DomainException("phd.PhdIndividualProgramProcess.cannot.be.transferred");
        }

        if (hasRegistration() && getRegistration().isConcluded()) {
            throw new DomainException("phd.PhdIndividualProgramProcess.source.registration.is.concluded");
        }

        this.createState(PhdIndividualProgramProcessState.TRANSFERRED, getPerson(), remarks);

        if (hasRegistration() && getRegistration().isActive()) {
            RegistrationStateCreator.createState(getRegistration(), responsible, new DateTime(),
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

    final public boolean hasInsuranceDebts(final ExecutionYear executionYear) {
        return hasAnyNotPayedInsuranceEventUntil(executionYear);
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebts(final AdministrativeOffice office,
            final ExecutionYear executionYear) {
        return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(office, executionYear);
    }

    private boolean hasAnyNotPayedInsuranceEventUntil(final ExecutionYear executionYear) {
        for (final InsuranceEvent event : getPerson().getNotCancelledInsuranceEventsUntil(executionYear)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEventUntil(final AdministrativeOffice office,
            final ExecutionYear executionYear) {
        for (final AdministrativeOfficeFeeAndInsuranceEvent event : getPerson()
                .getNotCancelledAdministrativeOfficeFeeAndInsuranceEventsUntil(office, executionYear)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedInsuranceEvents() {
        for (final InsuranceEvent event : getPerson().getNotCancelledInsuranceEvents()) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(final AdministrativeOffice office) {
        for (final AdministrativeOfficeFeeAndInsuranceEvent event : getPerson()
                .getNotCancelledAdministrativeOfficeFeeAndInsuranceEvents(office)) {
            if (event.isInDebt()) {
                return true;
            }
        }

        return false;
    }

    final public boolean hasInsuranceDebtsCurrently() {
        return hasAnyNotPayedInsuranceEvents();
    }

    final public boolean hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(final AdministrativeOffice administrativeOffice) {
        return hasAnyNotPayedAdministrativeOfficeFeeAndInsuranceEvents(administrativeOffice);
    }

    public boolean hasDiplomaRequest() {
        for (PhdAcademicServiceRequest academicServiceRequest : getPhdAcademicServiceRequests()) {
            if (academicServiceRequest.isDiploma() && !academicServiceRequest.isCancelled()
                    && !academicServiceRequest.isRejected()) {
                return true;
            }
        }

        return false;
    }

    public PhdRegistryDiplomaRequest getRegistryDiplomaRequest() {
        for (PhdAcademicServiceRequest academicServiceRequest : getPhdAcademicServiceRequests()) {
            if (academicServiceRequest.isRegistryDiploma() && !academicServiceRequest.isCancelled()
                    && !academicServiceRequest.isRejected()) {
                return (PhdRegistryDiplomaRequest) academicServiceRequest;
            }
        }

        return null;
    }

    public boolean hasRegistryDiplomaRequest() {
        return getRegistryDiplomaRequest() != null;
    }

    public PhdDiplomaRequest getDiplomaRequest() {
        for (PhdAcademicServiceRequest academicServiceRequest : getPhdAcademicServiceRequests()) {
            if (academicServiceRequest.isDiploma() && !academicServiceRequest.isCancelled()
                    && !academicServiceRequest.isRejected()) {
                return (PhdDiplomaRequest) academicServiceRequest;
            }
        }

        return null;
    }

    public PhdDiplomaSupplementRequest getDiplomaSupplementRequest() {
        if (!hasRegistryDiplomaRequest()) {
            return null;
        }

        return getRegistryDiplomaRequest().getDiplomaSupplement();
    }

    public PhdConclusionProcess getLastConclusionProcess() {
        if (getPhdConclusionProcesses().isEmpty()) {
            return null;
        }

        Set<PhdConclusionProcess> conclusionProcessSet =
                new TreeSet<PhdConclusionProcess>(PhdConclusionProcess.VERSION_COMPARATOR);
        conclusionProcessSet.addAll(getPhdConclusionProcesses());
        return conclusionProcessSet.iterator().next();
    }

    public boolean isConclusionProcessed() {
        return !getPhdConclusionProcesses().isEmpty();
    }

    public String getGraduateTitle(Locale locale) {
        StringBuilder stringBuilder = new StringBuilder(BundleUtil.getString(Bundle.PHD, locale, "label.phd.graduated.title.in")).append(" ");
        stringBuilder.append(getPhdProgram().getName().getContent(locale));

        return stringBuilder.toString();
    }

    public Boolean isBolonha() {
        return getPhdConfigurationIndividualProgramProcess().getIsBolonha() != null
                && getPhdConfigurationIndividualProgramProcess().getIsBolonha();
    }

    public List<PhdAcademicServiceRequest> getNewAcademicServiceRequests() {
        List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

        for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
            if (!request.isNewRequest()) {
                continue;
            }

            result.add(request);
        }

        return result;
    }

    public List<PhdAcademicServiceRequest> getProcessingAcademicServiceRequests() {
        List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

        for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
            if (request.isNewRequest()) {
                continue;
            }

            if (request.isDelivered() || request.isDeliveredSituationAccepted()) {
                continue;
            }

            if (request.isCancelled() || request.isRejected()) {
                continue;
            }

            result.add(request);
        }

        return result;
    }

    public List<PhdAcademicServiceRequest> getToDeliverAcademicServiceRequests() {
        List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

        for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
            if (!request.isDeliveredSituationAccepted()) {
                continue;
            }

            result.add(request);
        }

        return result;
    }

    public List<PhdAcademicServiceRequest> getHistoricalAcademicServiceRequests() {
        List<PhdAcademicServiceRequest> result = new ArrayList<PhdAcademicServiceRequest>();

        for (PhdAcademicServiceRequest request : getPhdAcademicServiceRequests()) {
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
        subjectOrders.addAll(getThesisSubjectOrders());
        return subjectOrders;
    }

    public int getHighestThesisSubjectOrder() {
        int highestOrder = 0;
        for (ThesisSubjectOrder order : getThesisSubjectOrders()) {
            if (order.getSubjectOrder() > highestOrder) {
                highestOrder = order.getSubjectOrder();
            }
        }
        return highestOrder;
    }

    public boolean hasPhdGratuityEventForYear(int year) {
        for (PhdGratuityEvent event : getPhdGratuityEvents()) {
            if (event.getYear() == year && !event.isCancelled()) {
                return true;
            }
        }
        return false;
    }

    public AdministrativeOffice getAdministrativeOffice() {
        return hasPhdProgram() ? getPhdProgram().getAdministrativeOffice() : null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreeInformations() {
        return getPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreeInformations() {
        return !getPrecedentDegreeInformationsSet().isEmpty();
    }

    @Override
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState> getStates() {
        return getStatesSet();
    }

    @Override
    @Deprecated
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmail> getPhdIndividualProgramProcessEmails() {
        return getPhdIndividualProgramProcessEmailsSet();
    }

    @Deprecated
    public boolean hasAnyPhdIndividualProgramProcessEmails() {
        return !getPhdIndividualProgramProcessEmailsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.alert.PhdAlert> getAlerts() {
        return getAlertsSet();
    }

    @Deprecated
    public boolean hasAnyAlerts() {
        return !getAlertsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess> getPhdConclusionProcesses() {
        return getPhdConclusionProcessesSet();
    }

    @Deprecated
    public boolean hasAnyPhdConclusionProcesses() {
        return !getPhdConclusionProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityEvent> getPhdGratuityEvents() {
        return getPhdGratuityEventsSet();
    }

    @Deprecated
    public boolean hasAnyPhdGratuityEvents() {
        return !getPhdGratuityEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdParticipant> getParticipants() {
        return getParticipantsSet();
    }

    @Deprecated
    public boolean hasAnyParticipants() {
        return !getParticipantsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmail> getPhdProgramEmail() {
        return getPhdProgramEmailSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramEmail() {
        return !getPhdProgramEmailSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdParticipant> getAssistantGuidings() {
        return getAssistantGuidingsSet();
    }

    @Deprecated
    public boolean hasAnyAssistantGuidings() {
        return !getAssistantGuidingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdParticipant> getGuidings() {
        return getGuidingsSet();
    }

    @Deprecated
    public boolean hasAnyGuidings() {
        return !getGuidingsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ThesisSubjectOrder> getThesisSubjectOrders() {
        return getThesisSubjectOrdersSet();
    }

    @Deprecated
    public boolean hasAnyThesisSubjectOrders() {
        return !getThesisSubjectOrdersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage> getAlertMessages() {
        return getAlertMessagesSet();
    }

    @Deprecated
    public boolean hasAnyAlertMessages() {
        return !getAlertMessagesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest> getPhdAcademicServiceRequests() {
        return getPhdAcademicServiceRequestsSet();
    }

    @Deprecated
    public boolean hasAnyPhdAcademicServiceRequests() {
        return !getPhdAcademicServiceRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasThesisProcess() {
        return getThesisProcess() != null;
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasThesisTitleEn() {
        return getThesisTitleEn() != null;
    }

    @Deprecated
    public boolean hasPhdIndividualProcessNumber() {
        return getPhdIndividualProcessNumber() != null;
    }

    @Deprecated
    public boolean hasRegistrationFee() {
        return getRegistrationFee() != null;
    }

    @Deprecated
    public boolean hasPhdConfigurationIndividualProgramProcess() {
        return getPhdConfigurationIndividualProgramProcess() != null;
    }

    @Deprecated
    public boolean hasDestiny() {
        return getDestiny() != null;
    }

    @Deprecated
    public boolean hasWhenFormalizedRegistration() {
        return getWhenFormalizedRegistration() != null;
    }

    @Deprecated
    public boolean hasQualificationExamsPerformed() {
        return getQualificationExamsPerformed() != null;
    }

    @Deprecated
    public boolean hasSeminarProcess() {
        return getSeminarProcess() != null;
    }

    @Deprecated
    public boolean hasQualificationExamsRequired() {
        return getQualificationExamsRequired() != null;
    }

    @Deprecated
    public boolean hasCandidacyProcess() {
        return getCandidacyProcess() != null;
    }

    @Deprecated
    public boolean hasExternalPhdProgram() {
        return getExternalPhdProgram() != null;
    }

    @Deprecated
    public boolean hasThesisTitle() {
        return getThesisTitle() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

    @Deprecated
    public boolean hasOtherCollaborationType() {
        return getOtherCollaborationType() != null;
    }

    @Deprecated
    public boolean hasWhenStartedStudies() {
        return getWhenStartedStudies() != null;
    }

    @Deprecated
    public boolean hasCollaborationType() {
        return getCollaborationType() != null;
    }

    @Deprecated
    public boolean hasSource() {
        return getSource() != null;
    }

    @Deprecated
    public boolean hasStudyPlan() {
        return getStudyPlan() != null;
    }

    @Deprecated
    public boolean hasPhdProgramFocusArea() {
        return getPhdProgramFocusArea() != null;
    }

    @Deprecated
    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasInquiryStudentCycleAnswer() {
        return getInquiryStudentCycleAnswer() != null;
    }

    @Deprecated
    public boolean hasThesisRequestFee() {
        return getThesisRequestFee() != null;
    }

}
