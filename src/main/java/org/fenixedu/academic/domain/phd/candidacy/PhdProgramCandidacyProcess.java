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
package org.fenixedu.academic.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PhotoState;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import org.fenixedu.academic.domain.candidacy.Ingression;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.phd.PhdCandidacyProcessState;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramCandidacyProcessState;
import org.fenixedu.academic.domain.phd.PhdProgramFocusArea;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.domain.phd.PhdProgramServiceAgreementTemplate;
import org.fenixedu.academic.domain.phd.alert.PhdFinalProofRequestAlert;
import org.fenixedu.academic.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import org.fenixedu.academic.domain.phd.alert.PhdRegistrationFormalizationAlert;
import org.fenixedu.academic.domain.phd.candidacy.activities.AddCandidacyReferees;
import org.fenixedu.academic.domain.phd.candidacy.activities.AddNotification;
import org.fenixedu.academic.domain.phd.candidacy.activities.AddState;
import org.fenixedu.academic.domain.phd.candidacy.activities.AssociateRegistration;
import org.fenixedu.academic.domain.phd.candidacy.activities.DeleteCandidacyReview;
import org.fenixedu.academic.domain.phd.candidacy.activities.DeleteDocument;
import org.fenixedu.academic.domain.phd.candidacy.activities.EditCandidacyDate;
import org.fenixedu.academic.domain.phd.candidacy.activities.EditProcessAttributes;
import org.fenixedu.academic.domain.phd.candidacy.activities.PhdProgramCandidacyProcessActivity;
import org.fenixedu.academic.domain.phd.candidacy.activities.RatifyCandidacy;
import org.fenixedu.academic.domain.phd.candidacy.activities.RegistrationFormalization;
import org.fenixedu.academic.domain.phd.candidacy.activities.RejectCandidacyProcess;
import org.fenixedu.academic.domain.phd.candidacy.activities.RemoveCandidacyDocument;
import org.fenixedu.academic.domain.phd.candidacy.activities.RemoveLastState;
import org.fenixedu.academic.domain.phd.candidacy.activities.RequestCandidacyReview;
import org.fenixedu.academic.domain.phd.candidacy.activities.RequestRatifyCandidacy;
import org.fenixedu.academic.domain.phd.candidacy.activities.UploadCandidacyReview;
import org.fenixedu.academic.domain.phd.candidacy.activities.UploadDocuments;
import org.fenixedu.academic.domain.phd.candidacy.activities.ValidatedByCandidate;
import org.fenixedu.academic.domain.phd.debts.PhdRegistrationFee;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserLoginPeriod;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    @StartActivity
    public static class CreateCandidacy extends PhdProgramCandidacyProcessActivity {

        @Override
        protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        }

        @Override
        protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
            final Object[] values = (Object[]) object;
            final PhdProgramCandidacyProcessBean bean = readBean(values);
            final Person person = readPerson(values);
            final PhdIndividualProgramProcess individualProgramProcess = readPhdIndividualProgramProcess(values);
            final PhdProgramCandidacyProcess result =
                    new PhdProgramCandidacyProcess(bean, person, bean.getMigratedProcess(), individualProgramProcess);
            result.createState(bean.getState(), person, "");
            return result;
        }

        private Person readPerson(final Object[] values) {
            return (Person) values[1];
        }

        private PhdProgramCandidacyProcessBean readBean(final Object[] values) {
            return (PhdProgramCandidacyProcessBean) values[0];
        }

        private PhdIndividualProgramProcess readPhdIndividualProgramProcess(final Object[] values) {
            return (PhdIndividualProgramProcess) values[2];
        }
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new UploadDocuments());
        activities.add(new DeleteDocument());
        activities.add(new EditCandidacyDate());
        activities.add(new AddCandidacyReferees());
        activities.add(new ValidatedByCandidate());

        activities.add(new RequestCandidacyReview());
        activities.add(new UploadCandidacyReview());
        activities.add(new DeleteCandidacyReview());
        activities.add(new RejectCandidacyProcess());

        activities.add(new RequestRatifyCandidacy());
        activities.add(new RatifyCandidacy());

        activities.add(new AddNotification());
        activities.add(new RegistrationFormalization());
        activities.add(new AssociateRegistration());

        activities.add(new AddState());
        activities.add(new RemoveLastState());
        activities.add(new EditProcessAttributes());

        activities.add(new RemoveCandidacyDocument());
    }

    @Override
    public boolean isAllowedToManageProcess(User userView) {
        return this.getIndividualProgramProcess().isAllowedToManageProcess(userView);
    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean bean, final Person person, boolean isMigratedProcess,
            final PhdIndividualProgramProcess individualProgramProcess) {
        super();

        checkCandidacyDate(bean.getExecutionYear(), bean.getCandidacyDate());
        setCandidacyDate(bean.getCandidacyDate());
        setValidatedByCandidate(false);

        setCandidacyHashCode(bean.getCandidacyHashCode());
        PHDProgramCandidacy candidacy = new PHDProgramCandidacy(person);
        setCandidacy(candidacy);

        setIndividualProgramProcess(individualProgramProcess);

        if (bean.hasDegree()) {
            getCandidacy().setExecutionDegree(bean.getExecutionDegree());
        }

        if (!isMigratedProcess) {
            if (!bean.hasCollaborationType() || bean.getCollaborationType().generateCandidacyDebt()) {
                if (hasPaymentFees(EventType.CANDIDACY_ENROLMENT)) {
                    new PhdProgramCandidacyEvent(person, this);
                }
            }
        }

        if (isPublicCandidacy()) {
            if (bean.getPhdCandidacyPeriod() == null) {
                throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.public.candidacy.period.is.missing");
            }

            setPublicPhdCandidacyPeriod(bean.getPhdCandidacyPeriod());
        }

    }

    private boolean hasPaymentFees(final EventType candidacyEnrolmentEventType) {
        final PhdProgramServiceAgreementTemplate serviceAgreementTemplate = getPhdProgram().getServiceAgreementTemplate();
        for (final PostingRule postingRule : serviceAgreementTemplate.getAllPostingRulesFor(candidacyEnrolmentEventType)) {
            if (postingRule.isActive()) {
                if (postingRule instanceof PhdProgramCandidacyPR) {
                    final PhdProgramCandidacyPR phdProgramCandidacyPR = (PhdProgramCandidacyPR) postingRule;
                    if (phdProgramCandidacyPR.getFixedAmount().isPositive()) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasOnlineApplicationForPeriod(final Person person, PhdCandidacyPeriod phdCandidacyPeriod) {
        Collection<PhdIndividualProgramProcess> phdIndividualProgramProcesses = person.getPhdIndividualProgramProcessesSet();

        for (PhdIndividualProgramProcess phdIndividualProgramProcess : phdIndividualProgramProcesses) {
            if (phdCandidacyPeriod == phdIndividualProgramProcess.getCandidacyProcess().getPublicPhdCandidacyPeriod()) {
                return true;
            }
        }

        return false;
    }

    public boolean isPublicCandidacy() {
        return getCandidacyHashCode() != null;
    }

    public IndividualCandidacyPaymentCode getAssociatedPaymentCode() {
        if (getEvent() == null) {
            return null;
        }

        return getEvent().getAssociatedPaymentCode();
    }

    public boolean hasPaymentCodeToPay() {
        if (getEvent() == null) {
            return false;
        }
        return getEvent().getAssociatedPaymentCode().getMinAmount().isPositive()
                && getEvent().getAssociatedPaymentCode().getMaxAmount().isPositive();
    }

    private void checkCandidacyDate(ExecutionYear executionYear, LocalDate candidacyDate) {
        // TODO: check this - in august candidacy dates will not be contained in
        // given execution year

        String[] args = {};
        if (candidacyDate == null) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.invalid.candidacy.date", args);
        }
        // if (!executionYear.containsDate(candidacyDate)) {
        // throw new DomainException(
        // "error.phd.candidacy.PhdProgramCandidacyProcess.executionYear.doesnot.contains.candidacy.date",
        // candidacyDate
        // .toString("dd/MM/yyyy"), executionYear.getQualifiedName(),
        // executionYear.getBeginDateYearMonthDay()
        // .toString("dd/MM/yyyy"),
        // executionYear.getEndDateYearMonthDay().toString("dd/MM/yyyy"));
        // }
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return false;
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.PHD, getClass().getSimpleName());
    }

    public PhdProgramCandidacyProcess edit(final LocalDate candidacyDate) {
        checkCandidacyDate(getExecutionYear(), candidacyDate);
        setCandidacyDate(candidacyDate);
        return this;
    }

    private ExecutionYear getExecutionYear() {
        return getIndividualProgramProcess().getExecutionYear();
    }

    public boolean hasAnyPayments() {
        return getEvent() != null && getEvent().hasAnyPayments();
    }

    public void cancelDebt(final Person responsible) {
        if (getEvent() != null && getEvent().isOpen()) {
            getEvent().cancel(responsible);
        }
    }

    public String getProcessNumber() {
        return getIndividualProgramProcess().getProcessNumber();
    }

    @Override
    public Person getPerson() {
        return getIndividualProgramProcess().getPerson();
    }

    public Set<PhdProgramProcessDocument> getCandidacyReviewDocuments() {
        return getLatestDocumentsByType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);
    }

    public boolean hasAnyDocuments(final PhdIndividualProgramDocumentType type) {
        return !getLatestDocumentsByType(type).isEmpty();
    }

    public int getDocumentsCount(final PhdIndividualProgramDocumentType type) {
        return getLatestDocumentsByType(type).size();
    }

    public boolean isValidatedByCandidate() {
        return getValidatedByCandidate() != null && getValidatedByCandidate().booleanValue();
    }

    public boolean isOngoingApplicationAndValidatedByApplicant() {
        return isValidatedByCandidate()
                || getIndividualProgramProcess().getActiveState() != PhdIndividualProgramProcessState.CANDIDACY;
    }

    public Set<PhdProgramProcessDocument> getStudyPlanRelevantDocuments() {
        final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();
        result.addAll(getLatestDocumentsByType(PhdIndividualProgramDocumentType.STUDY_PLAN));
        result.addAll(getLatestDocumentsByType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW));

        return result;
    }

    public void createState(final PhdProgramCandidacyProcessState stateType, final Person person, final String remarks) {
        PhdCandidacyProcessState.createStateWithInferredStateDate(this, stateType, person, remarks);
    }

    @Override
    public PhdCandidacyProcessState getMostRecentState() {
        return (PhdCandidacyProcessState) super.getMostRecentState();
    }

    @Override
    public PhdProgramCandidacyProcessState getActiveState() {
        return (PhdProgramCandidacyProcessState) super.getActiveState();
    }

    public boolean isInState(final PhdProgramCandidacyProcessState state) {
        return getActiveState().equals(state);
    }

    public void ratify(RatifyCandidacyBean bean, Person responsible) {

        Object obj = bean.getWhenRatified();
        String[] args = {};
        if (obj == null) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.when.ratified.cannot.be.null", args);
        }

        if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && !bean.getRatificationFile().hasAnyInformation()) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.ratification.document.is.required");
        }

        setWhenRatified(bean.getWhenRatified());

        if (bean.getRatificationFile().hasAnyInformation()) {
            addDocument(bean.getRatificationFile(), responsible);
        }

        if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && !getIndividualProgramProcess().hasAnyRegistrationFormalizationActiveAlert()) {
            new PhdRegistrationFormalizationAlert(getIndividualProgramProcess(), bean.getMaxDaysToFormalizeRegistration());
        }

        createState(PhdProgramCandidacyProcessState.RATIFIED_BY_SCIENTIFIC_COUNCIL, responsible, "");

    }

    public PhdProgramCandidacyProcess registrationFormalization(final RegistrationFormalizationBean bean, final Person responsible) {

        if (!hasStudyPlan()) {
            throw new DomainException(
                    "error.phd.candidacy.PhdProgramCandidacyProcess.registrationFormalization.must.create.study.plan");
        }

        LocalDate whenFormalizedRegistration = new LocalDate();

        getIndividualProgramProcess().setWhenFormalizedRegistration(whenFormalizedRegistration);
        getIndividualProgramProcess().setWhenStartedStudies(bean.getWhenStartedStudies());

        assertPersonInformation();

        if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()) {
            final DegreeCurricularPlan dcp = getPhdProgramLastActiveDegreeCurricularPlan();
            assertStudyPlanInformation(bean, dcp);
            assertDebts(bean);
            assertRegistrationFormalizationAlerts();
        }

        createState(PhdProgramCandidacyProcessState.CONCLUDED, responsible, "");
        getIndividualProgramProcess().createState(PhdIndividualProgramProcessState.WORK_DEVELOPMENT, responsible, "");

        return this;
    }

    private void assertStudyPlanInformation(final RegistrationFormalizationBean bean,
            final DegreeCurricularPlan degreeCurricularPlan) {
        final ExecutionYear executionYear = ExecutionYear.readByDateTime(bean.getWhenStartedStudies());

        if (executionYear == null) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.StudyPlan.invalid.start.date");
        }

        if (hasCurricularStudyPlan()) {
            assertCandidacy(degreeCurricularPlan, executionYear);
            assertRegistration(bean, degreeCurricularPlan, executionYear);
        }
    }

    public PhdProgramCandidacyProcess associateRegistration(final RegistrationFormalizationBean bean) {

        if (isStudyPlanExempted()) {
            throw new DomainException(
                    "error.phd.candidacy.PhdProgramCandidacyProcess.associateRegistration.study.plan.is.exempted");
        }

        assertStudyPlanInformation(bean, bean.getRegistration().getLastDegreeCurricularPlan());
        assertRegistrationFormalizationAlerts();

        if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()) {
            assertDebts(bean);
        }

        return this;
    }

    private void assertPersonInformation() {
        final Person person = getPerson();

        if (getPerson().getStudent() == null) {
            new Student(getPerson());
        }

        Student student = getPerson().getStudent();
        PrecedentDegreeInformation precedentDegreeInformation = getCandidacy().getPrecedentDegreeInformation();
        ExecutionYear executionYear = getExecutionYear();
        if (getPerson().getStudent().getPersonalIngressionDataByExecutionYear(executionYear) == null) {
            new PersonalIngressionData(student, executionYear, precedentDegreeInformation);
        }

        PersonalIngressionData personalIngressionData =
                getPerson().getStudent().getPersonalIngressionDataByExecutionYear(executionYear);
        personalIngressionData.addPrecedentDegreesInformations(precedentDegreeInformation);
        precedentDegreeInformation.setPhdIndividualProgramProcess(getIndividualProgramProcess());

        if (person.getUser() == null) {
            person.setUser(new User(person.getProfile()));
        }
        UserLoginPeriod.createOpenPeriod(person.getUser());

        RoleType.grant(RoleType.RESEARCHER, person.getUser());

        if (person.getPersonalPhoto() == null) {
            final Photograph photograph = person.getPersonalPhotoEvenIfPending();
            if (photograph != null) {
                photograph.setState(PhotoState.APPROVED);
            }
        }
    }

    private void assertCandidacy(final DegreeCurricularPlan dcp, final ExecutionYear executionYear) {
        if (getCandidacy().getExecutionDegree() == null) {
            final ExecutionDegree executionDegree = dcp.getExecutionDegreeByAcademicInterval(executionYear.getAcademicInterval());
            getCandidacy().setExecutionDegree(executionDegree);
        }

        getCandidacy().setIngression(Ingression.CIA3C);
    }

    private void assertRegistration(final RegistrationFormalizationBean bean, final DegreeCurricularPlan dcp,
            final ExecutionYear executionYear) {

        final Registration registration = getOrCreateRegistration(bean, dcp, executionYear);

        registration.setHomologationDate(getWhenRatified());
        registration.setStudiesStartDate(bean.getWhenStartedStudies());
        registration.setIngression(Ingression.CIA3C);
        registration.setPhdIndividualProgramProcess(getIndividualProgramProcess());

        registration.editStartDates(bean.getWhenStartedStudies(), getWhenRatified(), bean.getWhenStartedStudies());

        if (registration.getStudentCandidacy() == getIndividualProgramProcess().getCandidacyProcess().getCandidacy()) {
            return;
        }

    }

    private Registration getOrCreateRegistration(final RegistrationFormalizationBean bean, final DegreeCurricularPlan dcp,
            final ExecutionYear executionYear) {

        final Registration registration;
        if (getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && bean.hasRegistration()) {
            return bean.getRegistration();
        }

        if (hasActiveRegistrationFor(dcp)) {
            if (!bean.hasRegistration()) {
                throw new DomainException("error.PhdProgramCandidacyProcess.regisration.formalization.already.has.registration");
            }
            registration = bean.getRegistration();
        } else {
            registration =
                    new Registration(getPerson(), dcp, getCandidacy(), RegistrationProtocol.getDefault(), null, executionYear);
        }

        return registration;
    }

    private void assertDebts(final RegistrationFormalizationBean bean) {
        assertPhdRegistrationFee();
        assertInsuranceEvent(ExecutionYear.readByDateTime(bean.getWhenStartedStudies()));
    }

    private void assertPhdRegistrationFee() {
        if (getIndividualProgramProcess().getRegistrationFee() == null) {
            new PhdRegistrationFee(getIndividualProgramProcess());
        }
    }

    private void assertInsuranceEvent(final ExecutionYear executionYear) {
        if (!getPerson().hasInsuranceEventFor(executionYear)
                && !getPerson().hasAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
            new InsuranceEvent(getPerson(), executionYear);
        }
    }

    private void assertRegistrationFormalizationAlerts() {
        if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && !getIndividualProgramProcess().hasPhdPublicPresentationSeminarAlert()) {
            new PhdPublicPresentationSeminarAlert(getIndividualProgramProcess());
        }

        if (!getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && !getIndividualProgramProcess().hasPhdFinalProofRequestAlert()) {
            new PhdFinalProofRequestAlert(getIndividualProgramProcess());
        }
    }

    private boolean hasCurricularStudyPlan() {
        return hasStudyPlan() && !getIndividualProgramProcess().getStudyPlan().isExempted();
    }

    public DegreeCurricularPlan getPhdProgramLastActiveDegreeCurricularPlan() {
        return hasPhdProgram() ? getPhdProgram().getDegree().getLastActiveDegreeCurricularPlan() : null;
    }

    PhdProgram getPhdProgram() {
        return getIndividualProgramProcess().getPhdProgram();
    }

    PhdProgramFocusArea getPhdProgramFocusArea() {
        return getIndividualProgramProcess().getPhdProgramFocusArea();
    }

    boolean hasPhdProgramFocusArea() {
        return getPhdProgramFocusArea() != null;
    }

    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

    public boolean hasStudyPlan() {
        return getIndividualProgramProcess().getStudyPlan() != null;
    }

    public boolean isStudyPlanExempted() {
        return hasStudyPlan() && getIndividualProgramProcess().getStudyPlan().isExempted();
    }

    public boolean hasActiveRegistrationFor(DegreeCurricularPlan degreeCurricularPlan) {
        return getPerson().getStudent() != null ? getPerson().getStudent().hasActiveRegistrationFor(degreeCurricularPlan) : false;
    }

    public LocalDate getWhenStartedStudies() {
        return getIndividualProgramProcess().getWhenStartedStudies();
    }

    public void deleteLastState() {
        if (getStatesSet().size() <= 1) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.cannot.delete.the.only.state");
        }

        getMostRecentState().delete();
    }

    public PhdCandidacyReferee getCandidacyRefereeByEmail(final String email) {
        Collection<PhdCandidacyReferee> candidacyReferees = getCandidacyRefereesSet();

        for (PhdCandidacyReferee phdCandidacyReferee : candidacyReferees) {
            if (phdCandidacyReferee.getEmail().trim().equals(email.trim())) {
                return phdCandidacyReferee;
            }
        }

        return null;
    }

    @Override
    @Deprecated
    public java.util.Set<org.fenixedu.academic.domain.phd.PhdCandidacyProcessState> getStates() {
        return getStatesSet();
    }

    @Override
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

}
