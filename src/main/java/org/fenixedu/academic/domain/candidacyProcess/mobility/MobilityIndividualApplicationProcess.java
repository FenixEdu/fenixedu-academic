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
package org.fenixedu.academic.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import org.fenixedu.academic.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcessBean;
import org.fenixedu.academic.domain.candidacyProcess.PrecedentDegreeInformationForIndividualCandidacyFactory;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ApprovedLearningAgreementDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ApprovedLearningAgreementExecutedAction;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusAlert;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusAlertEntityType;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusCandidacyProcessExecutedAction;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcessExecutedAction;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ExecutedActionType;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.NationalIdCardAvoidanceQuestion;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.ReceptionEmailExecutedAction;
import org.fenixedu.academic.domain.candidacyProcess.erasmus.StorkAttributesList;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.period.MobilityApplicationPeriod;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserLoginPeriod;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MobilityIndividualApplicationProcess extends MobilityIndividualApplicationProcess_Base {

    private static final Logger logger = LoggerFactory.getLogger(MobilityIndividualApplicationProcess.class);

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new CancelCandidacy());
        activities.add(new EditCandidacyPersonalInformation());
        activities.add(new EditCandidacyInformation());
        activities.add(new EditDegreeAndCoursesInformation());
        activities.add(new SendEmailForApplicationSubmission());
        activities.add(new EditPublicCandidacyPersonalInformation());
        activities.add(new EditPublicCandidacyInformation());
        activities.add(new EditPublicDegreeAndCoursesInformation());
        activities.add(new SetGriValidation());
        activities.add(new SetCoordinatorValidation());
        activities.add(new VisualizeAlerts());
        activities.add(new EditDocuments());
        activities.add(new EditPublicCandidacyDocumentFile());
        activities.add(new CreateStudentData());
        activities.add(new SetEIdentifierForTesting());
        activities.add(new BindLinkSubmitedIndividualCandidacyWithEidentifier());
        activities.add(new UploadApprovedLearningAgreement());
        activities.add(new ViewApprovedLearningAgreements());
        activities.add(new MarkAlertAsViewed());
        activities.add(new SendEmailToAcceptedStudent());
        activities.add(new SendEmailToCandidateForMissingDocuments());
        activities.add(new RevokeDocumentFile());
        activities.add(new RejectCandidacy());
        activities.add(new CreateRegistration());
        activities.add(new RevertCandidacyToStandBy());
        activities.add(new EnrolStudent());
        activities.add(new AnswerNationalIdCardAvoidanceOnSubmissionQuestion());

    }

    public MobilityIndividualApplicationProcess() {
        super();
    }

    public MobilityIndividualApplicationProcess(final MobilityIndividualApplicationProcessBean bean) {
        this();

        /*
         * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
         * candidacy information are made in the init method
         */
        init(bean);

        /*
         * 27/04/2009 - New document files specific to Erasmus candidacies
         */
        setSpecificIndividualCandidacyDocumentFiles(bean);

        setValidatedByMobilityCoordinator(false);
        setValidatedByGri(false);

        setPersonalFieldsFromStork(bean.getPersonalFieldsFromStork() != null ? bean.getPersonalFieldsFromStork() : StorkAttributesList.EMPTY);
    }

    @Override
    protected void init(IndividualCandidacyProcessBean bean) {
        checkParameters(bean.getCandidacyProcess());

        if (bean.getPublicCandidacyHashCode() == null) {
            throw new DomainException("error.IndividualCandidacy.hash.code.is.null");
        }

        if (existsIndividualCandidacyProcessForDocumentId(bean.getCandidacyProcess(), bean.getPersonBean().getIdDocumentType(),
                bean.getPersonBean().getDocumentIdNumber())) {
            throw new DomainException("error.IndividualCandidacy.exists.for.same.document.id");
        }

        if (!StringUtils.isEmpty(bean.getPersonBean().getEidentifier())
                && existsIndividualCandidacyProcessForEidentifier(bean.getCandidacyProcess(), bean.getPersonBean()
                        .getEidentifier())) {
            throw new DomainException("error.individualCandidacy.exists.for.same.eIdentifier");
        }

        setCandidacyProcess(bean.getCandidacyProcess());
        createIndividualCandidacy(bean);

        /*
         * 11/04/2009 - An external candidacy submission requires documents like
         * identification and habilitation certificate documents
         */
        setCandidacyHashCode(bean.getPublicCandidacyHashCode());

        setCandidacyDocumentFiles(bean);

        setProcessCodeForThisIndividualCandidacy(bean.getCandidacyProcess());
    }

    protected boolean existsIndividualCandidacyProcessForEidentifier(final CandidacyProcess process, String eidentifier) {
        return process.getOpenChildProcessByEidentifier(eidentifier) != null;
    }

    private void setSpecificIndividualCandidacyDocumentFiles(MobilityIndividualApplicationProcessBean bean) {

    }

    @Override
    protected void checkParameters(final CandidacyProcess process) {
        if (process == null || process.getCandidacyPeriod() == null) {
            throw new DomainException("error.SecondCycleIndividualCandidacyProcess.invalid.candidacy.process");
        }
    }

    @Override
    protected void createIndividualCandidacy(final IndividualCandidacyProcessBean bean) {
        new MobilityIndividualApplication(this, (MobilityIndividualApplicationProcessBean) bean);
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return isAllowedToManageProcess(this, userView) || RoleType.SCIENTIFIC_COUNCIL.isMember(userView.getPerson().getUser())
                || RoleType.COORDINATOR.isMember(userView.getPerson().getUser()) || isCoordinatorOfProcess(userView);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    static private boolean isAllowedToManageProcess(MobilityIndividualApplicationProcess process, User userView) {
        if (process == null || process.getCandidacy() == null) {
            return false;
        }

        return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES, process
                .getCandidacy().getSelectedDegree(), userView.getPerson().getUser());
    }

    static private boolean isInternationalRelationsOfficer(User userView) {
        return RoleType.INTERNATIONAL_RELATION_OFFICE.isMember(userView.getPerson().getUser());
    }

    static private boolean isManager(User userView) {
        return RoleType.MANAGER.isMember(userView.getPerson().getUser());
    }

    private boolean isCoordinatorOfProcess(User userView) {
        if (userView.getPerson().getTeacher() == null) {
            return false;
        }

        return ((MobilityApplicationProcess) getCandidacyProcess()).isTeacherErasmusCoordinatorForDegree(userView.getPerson()
                .getTeacher(), getCandidacy().getSelectedDegree());
    }

    @Override
    public final MobilityIndividualApplication getCandidacy() {
        return (MobilityIndividualApplication) super.getCandidacy();
    }

    public Degree getCandidacySelectedDegree() {
        return getCandidacy().getSelectedDegree();
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
        return getCandidacySelectedDegree() == degree;
    }

    public String getCandidacyNotes() {
        return getCandidacy().getNotes();
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
        return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public Boolean isCandidacyProcessComplete() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getIsCandidacyProcessWithEidentifer() {
        return !StringUtils.isEmpty(getPersonalDetails().getEidentifier());
    }

    @Override
    protected void executeOperationsBeforeDocumentFileBinding(IndividualCandidacyDocumentFile documentFile) {
        IndividualCandidacyDocumentFileType type = documentFile.getCandidacyFileType();
        IndividualCandidacyDocumentFile file = this.getActiveFileForType(type);

        if (file != null) {
            file.setCandidacyFileActive(false);
        }
    }

    @Override
    public List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles() {
        List<IndividualCandidacyDocumentFileType> missingDocumentFiles = new ArrayList<IndividualCandidacyDocumentFileType>();

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PHOTO);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
        }

        if (!getCandidacy().getMobilityProgram().getRegistrationProtocol().isOnlyAllowedDegreeEnrolment()
                && getActiveFileForType(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS);
        }

        if (getActiveFileForType(IndividualCandidacyDocumentFileType.ENGLISH_LEVEL_DECLARATION) == null) {
            missingDocumentFiles.add(IndividualCandidacyDocumentFileType.ENGLISH_LEVEL_DECLARATION);
        }

        return missingDocumentFiles;
    }

    private MobilityIndividualApplicationProcess editCandidacyInformation(final MobilityIndividualApplicationProcessBean bean) {
        getCandidacy().getMobilityStudentData().edit(bean.getMobilityStudentDataBean());
        PrecedentDegreeInformationForIndividualCandidacyFactory.edit(bean);

        return this;
    }

    private MobilityIndividualApplicationProcess editDegreeAndCoursesInformation(
            final MobilityIndividualApplicationProcessBean bean) {

        bean.getMobilityStudentDataBean().setMobilityAgreement();
        editPrecedentDegreeInformation(bean);
        getCandidacy().editDegreeAndCoursesInformation(bean);

        return this;
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.CASE_HANDLEING, getClass().getSimpleName());
    }

    public List<CurricularCourse> getSortedSelectedCurricularCourses() {
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>(getCandidacy().getCurricularCoursesSet());
        Collections.sort(curricularCourses, CurricularCourse.COMPARATOR_BY_NAME);
        return curricularCourses;
    }

    public List<ErasmusAlert> getAlertsNotViewed() {
        List<ErasmusAlert> alertsNotViewed = new ArrayList<ErasmusAlert>();

        CollectionUtils.select(getAlertSet(), arg0 -> {
            ErasmusAlert alert = (ErasmusAlert) arg0;
            return alert.isToFire();
        }, alertsNotViewed);

        Collections.sort(alertsNotViewed, Collections.reverseOrder(ErasmusAlert.WHEN_CREATED_COMPARATOR));

        return alertsNotViewed;
    }

    public ErasmusAlert getMostRecentAlert() {
        List<ErasmusAlert> alerts = new ArrayList<ErasmusAlert>(getAlertSet());
        Collections.sort(alerts, Collections.reverseOrder(ErasmusAlert.WHEN_CREATED_COMPARATOR));

        return alerts.iterator().next();
    }

    public boolean isProcessWithMostRecentAlertMessageNotViewed() {
        List<ErasmusAlert> alertsNotViewed = getAlertsNotViewed();

        return !alertsNotViewed.isEmpty() && alertsNotViewed.iterator().next() == getMostRecentAlert();
    }

    public boolean isStudentAccepted() {
        return isStudentAcceptedAtDate(new DateTime());
    }

    public boolean isStudentAcceptedAtDate(final DateTime dateTime) {
        return !isCandidacyCancelled()
                && !isCandidacyRejected()
                && getValidatedByMobilityCoordinator()
                && getValidatedByGri()
                && ((getCandidacy().getMostRecentApprovedLearningAgreement() != null && getCandidacy()
                        .getMostRecentApprovedLearningAgreement().getUploadTime().isBefore(dateTime)) || getMobilityProgram()
                        .getRegistrationProtocol().isOnlyAllowedDegreeEnrolment());
    }

    public boolean isStudentAcceptedAndNotifiedAtDate(final DateTime dateTime) {
        return isStudentAcceptedAtDate(dateTime) && getCandidacy().hasProcessWithAcceptNotificationAtDate(dateTime);
    }

    public boolean isStudentAcceptedAndNotified() {
        return isStudentAcceptedAndNotifiedAtDate(new DateTime());
    }

    public boolean isStudentNotifiedWithReceptionEmail() {
        return !getAllReceptionEmailNotifications().isEmpty();
    }

    List<ReceptionEmailExecutedAction> getAllReceptionEmailNotifications() {
        List<ReceptionEmailExecutedAction> list = new ArrayList<ReceptionEmailExecutedAction>();

        for (ErasmusCandidacyProcessExecutedAction executedAction : ((MobilityApplicationProcess) getCandidacyProcess())
                .getErasmusCandidacyProcessExecutedActionSet()) {
            if (executedAction.isReceptionEmailExecutedAction()
                    && executedAction.getSubjectMobilityIndividualApplicationProcessSet().contains(this)) {
                list.add((ReceptionEmailExecutedAction) executedAction);
            }
        }

        Collections.sort(list, Collections.reverseOrder(new BeanComparator("whenOccured")));

        return list;
    }

    public DateTime getLastReceptionEmailSent() {
        List<ReceptionEmailExecutedAction> list = getAllReceptionEmailNotifications();
        return list.isEmpty() ? null : list.iterator().next().getWhenOccured();
    }

    public String getErasmusCandidacyStateDescription() {
        String registeredMessage =
                getCandidacy().getRegistration() != null ? "/"
                        + BundleUtil.getString(Bundle.CANDIDATE, "label.erasmus.candidacy.state.registered") : "";
        if (isCandidacyCancelled()) {
            return BundleUtil.getString(Bundle.CANDIDATE, "label.erasmus.candidacy.state.description.cancelled")
                    + registeredMessage;
        }

        if (isCandidacyRejected()) {
            return BundleUtil.getString(Bundle.CANDIDATE, "label.erasmus.candidacy.state.description.rejected")
                    + registeredMessage;
        }

        if (isCandidacyInStandBy() && isStudentAccepted()) {
            return BundleUtil.getString(Bundle.CANDIDATE, "label.erasmus.candidacy.state.description.student.accepted")
                    + registeredMessage;
        }

        return BundleUtil.getString(Bundle.CANDIDATE, "label.erasmus.candidacy.state.description.student.pending")
                + registeredMessage;
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess dummy, User userView,
                Object object) {
            return new MobilityIndividualApplicationProcess((MobilityIndividualApplicationProcessBean) object);
        }
    }

    static private class CancelCandidacy extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView) && !isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled() || !process.isCandidacyInStandBy() || process.hasAnyPaymentForCandidacy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.cancelCandidacy(userView.getPerson());
            return process;
        }
    }

    static private class EditCandidacyPersonalInformation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView) && !isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            final MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;
            process.editPersonalCandidacyInformation(bean.getPersonBean());
            return process;
        }
    }

    static private class EditCandidacyInformation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView) && !isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process.editCandidacyInformation((MobilityIndividualApplicationProcessBean) object);
        }
    }

    static private class EditDegreeAndCoursesInformation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView) && !isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process.editDegreeAndCoursesInformation((MobilityIndividualApplicationProcessBean) object);
        }

    }

    static private class SendEmailForApplicationSubmission extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            DegreeOfficePublicCandidacyHashCode hashCode = (DegreeOfficePublicCandidacyHashCode) object;
            hashCode.sendEmailForApplicationSuccessfullySubmited();
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }
    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.editPersonalCandidacyInformation(((MobilityIndividualApplicationProcessBean) object).getPersonBean());
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }
    }

    static private class EditPublicCandidacyInformation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process.editCandidacyInformation((MobilityIndividualApplicationProcessBean) object);
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicDegreeAndCoursesInformation extends Activity<MobilityIndividualApplicationProcess> {
        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process.editDegreeAndCoursesInformation((MobilityIndividualApplicationProcessBean) object);
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }

    }

    static private class SetGriValidation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;
            process.setValidatedByGri(bean.getValidatedByGri());

            if (bean.getCreateAlert()) {
                ErasmusAlert alert =
                        new ErasmusAlert(process, bean.getSendEmail(), new LocalDate(), new MultiLanguageString(
                                bean.getAlertSubject()), new MultiLanguageString(bean.getAlertBody()), ErasmusAlertEntityType.GRI);
                alert.setFireDate(new DateTime());
            }

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    static private class SetCoordinatorValidation extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            // TODO Check Permissions
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;

            process.setValidatedByMobilityCoordinator(bean.getValidatedByErasmusCoordinator());

            if (bean.getCreateAlert()) {
                new ErasmusAlert(process, bean.getSendEmail(), new LocalDate(), new MultiLanguageString(bean.getAlertSubject()),
                        new MultiLanguageString(bean.getAlertBody()), ErasmusAlertEntityType.COORDINATOR);
            }

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.TRUE;
        }
    }

    static private class VisualizeAlerts extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {

        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.TRUE;
        }

    }

    static private class EditDocuments extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (process.isCandidacyCancelled()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

    }

    static private class EditPublicCandidacyDocumentFile extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
            process.bindIndividualCandidacyDocumentFile(bean);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }
    }

    static private class CreateStudentData extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isManager(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            if (process.getPersonalDetails().getPerson().getStudent() == null) {
                new Student(process.getPersonalDetails().getPerson(), null);
                UserLoginPeriod.createOpenPeriod(process.getPersonalDetails().getPerson().getUser());

                if (StringUtils.isEmpty(process.getPersonalDetails().getPerson().getUsername())) {
                    throw new DomainException("error.erasmus.create.user", new String[] { null, "User not created" });
                }

            }

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.TRUE;
        }

    }

    private static class SetEIdentifierForTesting extends Activity<MobilityIndividualApplicationProcess> {
        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isManager(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;

            String eidentifier = bean.getPersonBean().getEidentifier();
            process.getPersonalDetails().getPerson().setEidentifier(eidentifier);

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.TRUE;
        }

    }

    private static class BindLinkSubmitedIndividualCandidacyWithEidentifier extends
            Activity<MobilityIndividualApplicationProcess> {
        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!StringUtils.isEmpty(process.getPersonalDetails().getEidentifier())) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            String eidentifier = (String) object;

            if (StringUtils.isEmpty(eidentifier)) {
                throw new DomainException("error.erasmus.candidacy.eidentifer.must.not.be.empty");
            }

            MobilityApplicationProcess parentProcess = (MobilityApplicationProcess) process.getCandidacyProcess();

            if (parentProcess.getOpenChildProcessByEidentifier(eidentifier) != null) {
                throw new DomainException("error.erasmus.candidacy.already.exists.with.eidentifier");
            }

            process.getPersonalDetails().getPerson().setEidentifier(eidentifier);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return Boolean.FALSE;
        }

    }

    private static class UploadApprovedLearningAgreement extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (isManager(userView)) {
                return;
            }

            if (isInternationalRelationsOfficer(userView)) {
                return;
            }

            if (process.isCoordinatorOfProcess(userView)) {
                return;
            }

            throw new PreConditionNotValidException();
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.getCandidacy().addApprovedLearningAgreements((ApprovedLearningAgreementDocumentFile) object);

            return process;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return true;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return true;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

    }

    private static class ViewApprovedLearningAgreements extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (isManager(userView)) {
                return;
            }

            if (isInternationalRelationsOfficer(userView)) {
                return;
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return true;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }
    }

    private static class MarkAlertAsViewed extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (isInternationalRelationsOfficer(userView)) {
                return;
            }

            throw new PreConditionNotValidException();
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            ErasmusAlert alert = (ErasmusAlert) object;
            alert.setFireDate(new DateTime());

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }
    }

    private static class SendEmailToAcceptedStudent extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isStudentAccepted()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            MobilityApplicationPeriod candidacyPeriod =
                    (MobilityApplicationPeriod) process.getCandidacyProcess().getCandidacyPeriod();

            MobilityEmailTemplate emailTemplateFor =
                    candidacyPeriod.getEmailTemplateFor(process.getMobilityProgram(),
                            MobilityEmailTemplateType.CANDIDATE_ACCEPTED);

            emailTemplateFor.sendEmailFor(process.getCandidacyHashCode());

            new ApprovedLearningAgreementExecutedAction(process.getCandidacy().getMostRecentApprovedLearningAgreement(),
                    ExecutedActionType.SENT_EMAIL_ACCEPTED_STUDENT);

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }
    }

    private static class SendEmailToCandidateForMissingDocuments extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }

            if (!process.isProcessMissingRequiredDocumentFiles()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {

            MobilityApplicationPeriod candidacyPeriod =
                    (MobilityApplicationPeriod) process.getCandidacyProcess().getCandidacyPeriod();

            MobilityEmailTemplate emailTemplateFor =
                    candidacyPeriod.getEmailTemplateFor(MobilityEmailTemplateType.MISSING_DOCUMENTS);

            emailTemplateFor.sendEmailFor(process.getCandidacyHashCode());

            new ErasmusIndividualCandidacyProcessExecutedAction(process,
                    ExecutedActionType.SENT_EMAIL_FOR_MISSING_REQUIRED_DOCUMENTS);

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }

    }

    static protected class RevokeDocumentFile extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView) && !isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            ((CandidacyProcessDocumentUploadBean) object).getDocumentFile().setCandidacyFileActive(Boolean.FALSE);
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return Boolean.FALSE;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }
    }

    static private class RejectCandidacy extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!process.isCandidacyInStandBy()) {
                throw new PreConditionNotValidException();
            }

            if (isInternationalRelationsOfficer(userView)) {
                return;
            }

            if (isManager(userView)) {
                return;
            }

            throw new PreConditionNotValidException();
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.rejectCandidacy(userView.getPerson());
            return process;
        }
    }

    static private class CreateRegistration extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.getCandidacy().getRegistration() != null) {
                throw new PreConditionNotValidException();
            }

            if (!process.isStudentAcceptedAndNotified()) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.createRegistration();

            return process;
        }

    }

    static private class EnrolOnFirstSemester extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isAllowedToManageProcess(process, userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.getCandidacy().getRegistration() == null) {
                throw new PreConditionNotValidException();
            }

            if (!process.isStudentAcceptedAndNotified()) {
                throw new PreConditionNotValidException();
            }

            if (!process.isStudentNotifiedWithReceptionEmail()) {
                throw new PreConditionNotValidException();
            }

        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.enrol();
            return process;
        }

    }

    static private class RevertCandidacyToStandBy extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.isCandidacyRejected()) {
                return;
            }

            if (process.isCandidacyCancelled()) {
                return;
            }

            throw new PreConditionNotValidException();
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            process.revertToStandBy(userView.getPerson());

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return true;
        }
    }

    static private class EnrolStudent extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {
            if (!isInternationalRelationsOfficer(userView)) {
                throw new PreConditionNotValidException();
            }

            if (process.getCandidacy().getRegistration() == null) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return true;
        }

    };

    static private class AnswerNationalIdCardAvoidanceOnSubmissionQuestion extends Activity<MobilityIndividualApplicationProcess> {

        @Override
        public void checkPreConditions(MobilityIndividualApplicationProcess process, User userView) {

            if (!NationalIdCardAvoidanceQuestion.UNANSWERED.equals(process.getCandidacy().getNationalIdCardAvoidanceQuestion())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected MobilityIndividualApplicationProcess executeActivity(MobilityIndividualApplicationProcess process,
                User userView, Object object) {
            MobilityIndividualApplicationProcessBean bean = (MobilityIndividualApplicationProcessBean) object;
            MobilityIndividualApplication candidacy = process.getCandidacy();

            candidacy.answerNationalIdCardAvoidanceOnSubmission(bean);

            return process;
        }

        @Override
        public Boolean isVisibleForAdminOffice() {
            return false;
        }

        @Override
        public Boolean isVisibleForCoordinator() {
            return false;
        }

        @Override
        public Boolean isVisibleForGriOffice() {
            return false;
        }
    };

    private void createRegistration() {
        if (getCandidacy().getCandidacyProcess().getDegreeCurricularPlan(this).isFirstCycle()) {
            getCandidacy().createRegistration(getDegreeCurricularPlan(this), CycleType.FIRST_CYCLE, null);
        } else {
            getCandidacy().createRegistration(getDegreeCurricularPlan(this), CycleType.SECOND_CYCLE, null);
        }
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final MobilityIndividualApplicationProcess candidacyProcess) {
        return candidacyProcess.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
    }

    private void enrol() {
        getCandidacy().enrol();
    }

    public DateTime getMostRecentSentEmailAcceptedStudentActionWhenOccured() {
        if (getCandidacy().getMostRecentApprovedLearningAgreement() == null) {
            return null;
        }

        return getCandidacy().getMostRecentApprovedLearningAgreement().getMostRecentSentLearningAgreementActionWhenOccured();
    }

    public MobilityProgram getMobilityProgram() {
        return getCandidacy().getMobilityProgram();
    }

}
