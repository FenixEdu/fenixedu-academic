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
package org.fenixedu.academic.domain.phd.candidacy.feedbackRequest;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdParticipantBean;
import org.fenixedu.academic.domain.phd.PhdProgramCandidacyProcessState;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.domain.phd.access.ExternalAccessPhdActivity;
import org.fenixedu.academic.domain.phd.access.PhdExternalOperationBean;
import org.fenixedu.academic.domain.phd.access.PhdProcessAccessType;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.phd.PhdProperties;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.messaging.core.domain.Message;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhdCandidacyFeedbackRequestProcess extends PhdCandidacyFeedbackRequestProcess_Base {

    private PhdCandidacyFeedbackRequestProcess() {
        super();
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
        return getCandidacyProcess().getIndividualProgramProcess();
    }

    @Override
    protected Person getPerson() {
        return getIndividualProgramProcess().getPerson();
    }

    @Override
    public boolean canExecuteActivity(User userView) {
        return true;
    }

    @Override
    public boolean isAllowedToManageProcess(User userView) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.PHD, getClass().getSimpleName());
    }

    @Override
    public PhdCandidacyFeedbackStateType getActiveState() {
        return (PhdCandidacyFeedbackStateType) super.getActiveState();
    }

    protected void createState(final PhdCandidacyFeedbackStateType type, final Person person, final String remarks,
            final DateTime stateDate) {
        new PhdCandidacyFeedbackState(this, type, person, remarks, stateDate);
    }

    protected PhdCandidacyFeedbackRequestElement getCandidacyFeedbackRequestElement(final PhdParticipant participant) {
        for (final PhdCandidacyFeedbackRequestElement element : getElementsSet()) {
            if (element.getParticipant() == participant) {
                return element;
            }
        }
        return null;
    }

    public Set<PhdIndividualProgramDocumentType> getSortedSharedDocumentTypes() {
        return getSharedDocuments().getSortedTypes();
    }

    /*
     * Documents used by elements to analize candidacy and upload candidacy
     */
    public Set<PhdProgramProcessDocument> getSharedDocumentsContent() {
        final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();

        for (final PhdIndividualProgramDocumentType type : getSortedSharedDocumentTypes()) {
            if (type.isVersioned()) {
                final PhdProgramProcessDocument document = getCandidacyProcess().getLatestDocumentVersionFor(type);
                if (document != null) {
                    result.add(document);
                }
            }
        }
        return result;
    }

    public Set<PhdProgramProcessDocument> getSubmittedCandidacyFeedbackDocuments() {
        final Set<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();
        for (final PhdCandidacyFeedbackRequestElement element : getElementsSet()) {
            final PhdCandidacyFeedbackRequestDocument document = element.getLastFeedbackDocument();
            if (document != null) {
                result.add(document);
            }
        }
        return result;
    }

    public boolean canUploadDocuments() {
        /*
         * CORRECT THIS METHOD
         */
        // return
        // getCandidacyProcess().isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
        return true;
    }

    private void notifyCoordinationOfCandidacyFeedback(final PhdCandidacyFeedbackRequestElement element) {

        AlertService.alertParticipants(getIndividualProgramProcess(), AlertMessage
                .create("message.phd.candidacy.feedback.coordinator.notification.subject"), AlertMessage.create(
                "message.phd.candidacy.feedback.coordinator.notification.body", element.getNameWithTitle(),
                getIndividualProgramProcess().getPerson().getName()), getOrCreateParticipantsToNofify());

    }

    private PhdParticipant[] getOrCreateParticipantsToNofify() {

        final PhdIndividualProgramProcess mainProcess = getIndividualProgramProcess();
        final List<PhdParticipant> result = new ArrayList<PhdParticipant>();

        for (final Person person : mainProcess.getCoordinatorsFor(ExecutionYear.readCurrentExecutionYear())) {

            final PhdParticipant participant = mainProcess.getParticipant(person);

            if (participant == null) {
                result.add(PhdParticipant.getUpdatedOrCreate(mainProcess, new PhdParticipantBean().setInternalParticipant(person)));

            } else if (participant.getCandidacyFeedbackRequestElementsSet().isEmpty()) {
                result.add(participant);
            }
        }

        return result.toArray(new PhdParticipant[result.size()]);
    }

    public boolean hasElement(final Person person) {
        for (final PhdCandidacyFeedbackRequestElement element : getElementsSet()) {
            if (element.isFor(person)) {
                return true;
            }
        }
        return false;
    }

    public PhdCandidacyFeedbackRequestElement getElement(final Person person) {
        for (final PhdCandidacyFeedbackRequestElement element : getElementsSet()) {
            if (element.isFor(person)) {
                return element;
            }
        }
        return null;
    }

    /*
     * ACTIVITIES
     */

    static abstract private class PhdActivity extends Activity<PhdCandidacyFeedbackRequestProcess> {

        @Override
        final public void checkPreConditions(final PhdCandidacyFeedbackRequestProcess process, final User userView) {
            processPreConditions(process, userView);
            activityPreConditions(process, userView);
        }

        protected void processPreConditions(final PhdCandidacyFeedbackRequestProcess process, final User userView) {
            if (!process.getCandidacyProcess().isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
                throw new PreConditionNotValidException(
                        "error.PhdCandidacyFeedbackRequestProcess.candidacy.process.is.not.pending.for.coordinator.opinion");
            }
        }

        abstract protected void activityPreConditions(final PhdCandidacyFeedbackRequestProcess process, final User userView);
    }

    @StartActivity
    static public class CreateCandidacy extends PhdActivity {

        @Override
        protected void processPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            // Auto-generated method stub
        }

        @Override
        protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess noProcess, User userView) {

        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess noProcess, User userView,
                Object object) {

            final PhdCandidacyFeedbackRequestProcessBean bean = (PhdCandidacyFeedbackRequestProcessBean) object;
            final PhdCandidacyFeedbackRequestProcess process = new PhdCandidacyFeedbackRequestProcess();

            process.setSharedDocuments(new PhdCandidacySharedDocumentsList(bean.getSharedDocuments()));
            process.setCandidacyProcess(bean.getCandidacyProcess());

            process.createState(PhdCandidacyFeedbackStateType.NEW, userView.getPerson(), null, new DateTime());

            return process;
        }
    }

    static public class EditSharedDocumentTypes extends PhdActivity {

        @Override
        protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            if (!process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess process, User userView,
                Object object) {

            final PhdCandidacyFeedbackRequestProcessBean bean = (PhdCandidacyFeedbackRequestProcessBean) object;
            process.setSharedDocuments(new PhdCandidacySharedDocumentsList(bean.getSharedDocuments()));

            return process;
        }
    }

    static public class AddPhdCandidacyFeedbackRequestElements extends PhdActivity {

        @Override
        protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            if (!process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess process, User userView,
                Object object) {

            final PhdCandidacyFeedbackRequestElementBean bean = (PhdCandidacyFeedbackRequestElementBean) object;

            if (bean.isExistingElement()) {

                if (!bean.hasAnyParticipants()) {
                    throw new DomainException("error.PhdCandidacyFeedbackRequestProcess.must.add.participants");
                }

                for (final PhdParticipant participant : bean.getParticipants()) {
                    final PhdCandidacyFeedbackRequestElement element =
                            PhdCandidacyFeedbackRequestElement.create(process, participant, bean);
                    addAccessPermissionsIfNecessary(element);
                    notifyElement(process, element, bean);
                }

            } else {
                final PhdCandidacyFeedbackRequestElement element = PhdCandidacyFeedbackRequestElement.create(process, bean);
                addAccessPermissionsIfNecessary(element);
                notifyElement(process, element, bean);
            }

            return process;
        }

        private void addAccessPermissionsIfNecessary(final PhdCandidacyFeedbackRequestElement element) {
            if (!element.getParticipant().isInternal()) {
                element.getParticipant().addAccessType(PhdProcessAccessType.CANDIDACY_FEEDBACK_DOCUMENTS_DOWNLOAD,
                        PhdProcessAccessType.CANDIDACY_FEEDBACK_UPLOAD);
            }
        }

        private void notifyElement(final PhdCandidacyFeedbackRequestProcess process,
                final PhdCandidacyFeedbackRequestElement element, final PhdCandidacyFeedbackRequestElementBean bean) {

            final String body =
                    bean.getMailBody() + "\n\n"
                            + getAccessInformation(process.getIndividualProgramProcess(), element.getParticipant()) + "\n\n";

            email(element.getEmail(), bean.getMailSubject(), body);
        }

        private String getAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant) {

            if (!participant.isInternal()) {
                return AlertMessage.get("message.phd.external.access", PhdProperties.getPhdExternalAccessLink(),
                        participant.getAccessHashCode(), participant.getPassword());

            } else {
                final Person person = ((InternalPhdParticipant) participant).getPerson();

                if (process.isCoordinatorForPhdProgram(person)) {
                    return AlertMessage.get("message.phd.candidacy.feedback.coordinator.access");

                } else if (process.isGuiderOrAssistentGuider(person) || RoleType.TEACHER.isMember(person.getUser())) {
                    return AlertMessage.get("message.phd.candidacy.feedback.teacher.access");
                }
            }

            throw new DomainException("error.PhdThesisProcess.unexpected.participant.type");
        }

        private void email(String email, String subject, String body) {
            Message.fromSystem()
                    .replyToSender()
                    .singleBcc(email)
                    .subject(subject)
                    .textBody(body)
                    .send();
        }
    }

    static public class DeleteCandidacyFeedbackRequestElement extends PhdActivity {

        @Override
        protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            if (!process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess process, User userView,
                Object object) {

            ((PhdCandidacyFeedbackRequestElement) object).delete();
            return process;
        }
    }

    static public class UploadCandidacyFeedback extends PhdActivity {

        @Override
        protected void activityPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            if (!process.hasElement(userView.getPerson())) {
                throw new PreConditionNotValidException();
            }
        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess executeActivity(PhdCandidacyFeedbackRequestProcess process, User userView,
                Object object) {

            final PhdProgramDocumentUploadBean bean = (PhdProgramDocumentUploadBean) object;

            if (bean.hasAnyInformation()) {
                final PhdCandidacyFeedbackRequestElement element = process.getElement(userView.getPerson());

                new PhdCandidacyFeedbackRequestDocument(element, bean.getRemarks(), bean.getFileContent(), bean.getFilename(),
                        null);

                process.notifyCoordinationOfCandidacyFeedback(element);
            }

            return process;
        }
    }

    static public class DownloadCandidacyFeedbackDocuments extends ExternalAccessPhdActivity<PhdCandidacyFeedbackRequestProcess> {

        @Override
        public void checkPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            if (!process.getCandidacyProcess().isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
                throw new PreConditionNotValidException(
                        "error.PhdCandidacyFeedbackRequestProcess.candidacy.process.is.not.pending.for.coordinator.opinion");
            }
        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess internalExecuteActivity(PhdCandidacyFeedbackRequestProcess process,
                User userView, PhdExternalOperationBean bean) {
            // Auto-generated method stub
            return process;
        }
    }

    static public class ExternalUploadCandidacyFeedback extends ExternalAccessPhdActivity<PhdCandidacyFeedbackRequestProcess> {

        @Override
        public void checkPreConditions(PhdCandidacyFeedbackRequestProcess process, User userView) {
            if (!process.getCandidacyProcess().isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
                throw new PreConditionNotValidException(
                        "error.PhdCandidacyFeedbackRequestProcess.candidacy.process.is.not.pending.for.coordinator.opinion");
            }
        }

        @Override
        protected PhdCandidacyFeedbackRequestProcess internalExecuteActivity(PhdCandidacyFeedbackRequestProcess process,
                User userView, PhdExternalOperationBean bean) {

            if (bean.getDocumentBean().hasAnyInformation()) {

                final PhdCandidacyFeedbackRequestElement element =
                        process.getCandidacyFeedbackRequestElement(bean.getParticipant());

                final PhdProgramDocumentUploadBean documentBean = bean.getDocumentBean();
                new PhdCandidacyFeedbackRequestDocument(element, documentBean.getRemarks(), documentBean.getFileContent(),
                        documentBean.getFilename(), null);

                process.notifyCoordinationOfCandidacyFeedback(element);
            }

            return process;
        }

    }

    static private List<Activity> activities = new ArrayList<>();
    static {
        activities.add(new EditSharedDocumentTypes());
        activities.add(new AddPhdCandidacyFeedbackRequestElements());
        activities.add(new DeleteCandidacyFeedbackRequestElement());
        activities.add(new UploadCandidacyFeedback());

        activities.add(new DownloadCandidacyFeedbackDocuments());
        activities.add(new ExternalUploadCandidacyFeedback());
    }

    @Override
    @Deprecated
    public java.util.Set<org.fenixedu.academic.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackState> getStates() {
        return getStatesSet();
    }

    @Override
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

}
