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
package net.sourceforge.fenixedu.domain.phd.thesis;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.debts.PhdThesisRequestFee;
import net.sourceforge.fenixedu.domain.phd.migration.activities.SkipThesisJuryActivities;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddPresidentJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddState;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ConcludePhdProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DeleteDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DeleteJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadFinalThesisDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadProvisionalThesisDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadThesisRequirement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.EditJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.EditPhdThesisProcessInformation;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryDocumentsDownload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackExternalUpload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackUpload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReviewDocumentsDownload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.MoveJuryElementOrder;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PhdThesisActivity;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PrintJuryElementsDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RatifyFinalThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElementsDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RemindJuryReviewToReporters;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RemoveLastState;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ReplaceDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryReviews;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ScheduleThesisDiscussion;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SetFinalGrade;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SetPhdJuryElementRatificationEntity;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SkipScheduleThesisDiscussion;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitJuryElementsDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SwapJuryElementsOrder;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.UploadDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ValidateJury;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdThesisProcess extends PhdThesisProcess_Base {

    @StartActivity
    static public class RequestThesis extends PhdThesisActivity {

        @Override
        public void activityPreConditions(PhdThesisProcess process, User userView) {
            // Activity on main process ensures access control
        }

        @Override
        protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

            final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
            LocalDate whenThesisDiscussionRequired = bean.getWhenThesisDiscussionRequired();

            final PhdThesisProcess result = new PhdThesisProcess();

            result.setIndividualProgramProcess(bean.getProcess());
            result.addDocuments(bean.getDocuments(), userView.getPerson());
            result.setWhenThesisDiscussionRequired(whenThesisDiscussionRequired);
            result.setWhenJuryRequired(bean.getWhenThesisDiscussionRequired());

            String presidentTitlePt =
                    MessageFormat.format(BundleUtil.getString(Bundle.PHD, new Locale("pt", "PT"),
                            "message.phd.thesis.president.title.default"), Unit.getInstitutionAcronym());
            String presidentTitleEn =
                    MessageFormat.format(BundleUtil.getString(Bundle.PHD, new Locale("en", "EN"),
                            "message.phd.thesis.president.title.default"), Unit.getInstitutionAcronym());

            result.setPresidentTitle(new MultiLanguageString(MultiLanguageString.pt, presidentTitlePt).with(
                    MultiLanguageString.en, presidentTitleEn));

            if (!result.getIndividualProgramProcess().isMigratedProcess()) {
                new PhdThesisRequestFee(bean.getProcess());
            }

            result.createState(PhdThesisProcessStateType.NEW, userView.getPerson(), bean.getRemarks());
            return result;
        }
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new RequestJuryElements());
        activities.add(new SubmitJuryElementsDocuments());
        activities.add(new AddJuryElement());
        activities.add(new EditJuryElement());
        activities.add(new DeleteJuryElement());
        activities.add(new SwapJuryElementsOrder());
        activities.add(new MoveJuryElementOrder());
        activities.add(new AddPresidentJuryElement());
        activities.add(new ValidateJury());
        activities.add(new PrintJuryElementsDocument());
        activities.add(new RejectJuryElements());
        activities.add(new SubmitThesis());
        activities.add(new DownloadProvisionalThesisDocument());
        activities.add(new DownloadFinalThesisDocument());
        activities.add(new DownloadThesisRequirement());
        activities.add(new RequestJuryReviews());
        activities.add(new RemindJuryReviewToReporters());
        activities.add(new JuryDocumentsDownload());
        activities.add(new JuryReporterFeedbackUpload());
        activities.add(new JuryReporterFeedbackExternalUpload());
        activities.add(new JuryReviewDocumentsDownload());
        activities.add(new ScheduleThesisDiscussion());
        activities.add(new RatifyFinalThesis());
        activities.add(new SetFinalGrade());
        activities.add(new RejectJuryElementsDocuments());
        activities.add(new ReplaceDocument());
        activities.add(new AddState());
        activities.add(new RemoveLastState());
        activities.add(new SkipThesisJuryActivities());
        activities.add(new SkipScheduleThesisDiscussion());
        activities.add(new EditPhdThesisProcessInformation());
        activities.add(new ConcludePhdProcess());
        activities.add(new SetPhdJuryElementRatificationEntity());
        activities.add(new UploadDocuments());
        activities.add(new DeleteDocument());
    }

    @Override
    public boolean isAllowedToManageProcess(User userView) {
        return this.getIndividualProgramProcess().isAllowedToManageProcess(userView);
    }

    private PhdThesisProcess() {
        super();
    }

    public boolean isJuryValidated() {
        return getWhenJuryValidated() != null;
    }

    public boolean isFinalThesisRatified() {
        return getWhenFinalThesisRatified() != null;
    }

    public void swapJuryElementsOrder(ThesisJuryElement e1, ThesisJuryElement e2) {
        if (getThesisJuryElementsSet().contains(e1) && getThesisJuryElementsSet().contains(e2)) {
            final Integer order1 = e1.getElementOrder();
            final Integer order2 = e2.getElementOrder();
            e1.setElementOrder(order2);
            e2.setElementOrder(order1);
        }
    }

    public void deleteJuryElement(ThesisJuryElement element) {
        if (getThesisJuryElementsSet().contains(element)) {
            final Integer elementOrder = element.getElementOrder();
            element.delete();
            reorderJuryElements(elementOrder);
        }
    }

    private void reorderJuryElements(Integer removedElementOrder) {
        for (final ThesisJuryElement element : getOrderedThesisJuryElements()) {
            if (element.getElementOrder().compareTo(removedElementOrder) > 0) {
                element.setElementOrder(Integer.valueOf(element.getElementOrder().intValue() - 1));
            }
        }
    }

    public TreeSet<ThesisJuryElement> getOrderedThesisJuryElements() {
        final TreeSet<ThesisJuryElement> result = new TreeSet<ThesisJuryElement>(ThesisJuryElement.COMPARATOR_BY_ELEMENT_ORDER);
        result.addAll(getThesisJuryElementsSet());
        return result;
    }

    public void createState(PhdThesisProcessStateType type, Person person, String remarks) {
        PhdThesisProcessState.createWithInferredStateDate(this, type, person, remarks);
    }

    @Override
    public Person getPerson() {
        return getIndividualProgramProcess().getPerson();
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

    @Override
    public PhdThesisProcessState getMostRecentState() {
        return (PhdThesisProcessState) super.getMostRecentState();
    }

    @Override
    public PhdThesisProcessStateType getActiveState() {
        return (PhdThesisProcessStateType) super.getActiveState();
    }

    // TODO: find clean solution to return specific documents
    // grouped??
    public PhdProgramProcessDocument getProvisionalThesisDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS);
    }

    public boolean hasProvisionalThesisDocument() {
        return getProvisionalThesisDocument() != null;
    }

    public PhdProgramProcessDocument getFinalThesisDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.FINAL_THESIS);
    }

    public boolean hasFinalThesisDocument() {
        return getFinalThesisDocument() != null;
    }

    public PhdProgramProcessDocument getThesisRequirementDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.THESIS_REQUIREMENT);
    }

    public boolean hasThesisRequirementDocument() {
        return getThesisRequirementDocument() != null;
    }

    public boolean isConcluded() {
        return getActiveState() == PhdThesisProcessStateType.CONCLUDED;
    }

    public PhdProgramProcessDocument getJuryElementsDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_ELEMENTS);
    }

    public Boolean hasJuryElementsDocument() {
        return getJuryElementsDocument() != null;
    }

    public PhdProgramProcessDocument getJuryPresidentDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT);
    }

    public Boolean hasJuryPresidentDocument() {
        return getJuryPresidentDocument() != null;
    }

    public DateTime getWhenRequestJury() {
        if (getWhenJuryRequired() == null) {
            return null;
        }
        return getWhenJuryRequired().toDateTimeAtStartOfDay();
    }

    public DateTime getWhenReceivedJury() {
        return getLastExecutionDateOf(SubmitJuryElementsDocuments.class);
    }

    public DateTime getWhenRequestedJuryReviews() {
        return getLastExecutionDateOf(RequestJuryReviews.class);
    }

    public PhdParticipant getParticipant(final Person person) {
        return getIndividualProgramProcess().getParticipant(person);
    }

    public boolean isParticipant(final Person person) {
        return getIndividualProgramProcess().isParticipant(person);
    }

    public ThesisJuryElement getThesisJuryElement(final Person person) {
        for (final ThesisJuryElement element : getThesisJuryElements()) {
            if (element.isFor(person)) {
                return element;
            }
        }
        return null;
    }

    public boolean isPresidentJuryElement(final Person person) {
        return hasPresidentJuryElement() && getPresidentJuryElement().isFor(person);
    }

    public List<PhdProgramProcessDocument> getThesisDocumentsToFeedback() {
        final List<PhdProgramProcessDocument> documents = new ArrayList<PhdProgramProcessDocument>(3);

        addDocument(documents, getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.THESIS_REQUIREMENT));
        addDocument(documents, getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.THESIS_ABSTRACT));
        addDocument(documents, getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS));
        addDocument(documents, getCV());

        return documents;
    }

    private void addDocument(List<PhdProgramProcessDocument> documents, PhdProgramProcessDocument document) {
        if (document != null) {
            documents.add(document);
        }
    }

    private PhdProgramProcessDocument getCV() {
        final PhdProgramProcessDocument cv = getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.CV);
        return (cv != null) ? cv : getCandidacyProcess().getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.CV);
    }

    private PhdProgramProcess getCandidacyProcess() {
        return getIndividualProgramProcess().getCandidacyProcess();
    }

    public String getProcessNumber() {
        return getIndividualProgramProcess().getProcessNumber();
    }

    public Collection<ThesisJuryElement> getReportThesisJuryElements() {
        final Collection<ThesisJuryElement> result = new ArrayList<ThesisJuryElement>();
        for (final ThesisJuryElement element : getThesisJuryElements()) {
            if (element.getReporter().booleanValue()) {
                result.add(element);
            }
        }
        return result;
    }

    public boolean isWaitingForJuryReporterFeedback() {
        return getActiveState() == PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK;
    }

    public boolean isAnyDocumentToValidate() {
        for (final ThesisJuryElement element : getThesisJuryElements()) {
            if (element.getReporter().booleanValue() && !element.isDocumentValidated()) {
                return true;
            }
        }
        return false;
    }

    public Collection<PhdProgramProcessDocument> getReportThesisJuryElementDocuments() {
        final Collection<PhdProgramProcessDocument> result = new HashSet<PhdProgramProcessDocument>();
        for (final ThesisJuryElement element : getThesisJuryElements()) {
            if (element.getReporter().booleanValue() && element.isDocumentValidated()) {
                result.add(element.getLastFeedbackDocument());
            }
        }
        return result;
    }

    public PhdProgramProcessDocument getJuryMeetingMinutesDocument() {
        return getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_MEETING_MINUTES);
    }

    @Override
    protected void addDocuments(final List<PhdProgramDocumentUploadBean> documents, final Person responsible) {
        for (final PhdProgramDocumentUploadBean each : documents) {

            if (each.isRequired()) {
                if (!each.hasAnyInformation()) {
                    throw new DomainException("error.PhdThesisProcess.document.is.required.and.no.information.given", each
                            .getType().getLocalizedName());
                }
            }

            if (each.hasAnyInformation()) {
                addDocument(each, responsible);
            }
        }
    }

    public void rejectJuryElementsDocuments() {
        if (hasJuryElementsDocument()) {
            getJuryElementsDocument().setDocumentAccepted(false);
        }

        if (hasJuryPresidentDocument()) {
            getJuryPresidentDocument().setDocumentAccepted(false);
        }
    }

    public void deleteLastState() {
        if (getStates().size() <= 1) {
            throw new DomainException("error.PhdThesisProcess.states.size.is.one");
        }

        getMostRecentState().delete();
    }

    public void checkJuryPresidentNotGuider(final PhdThesisJuryElementBean bean) {
        final PhdIndividualProgramProcess process = getIndividualProgramProcess();

        for (PhdParticipant processParticipant : process.getParticipants()) {
            if (processParticipant == bean.getParticipant()) {
                if (process.isGuider(processParticipant) || process.isAssistantGuider(processParticipant)) {
                    throw new DomainException("error.PhdThesisProcess.president.cannot.be.guider.or.assistantguider");
                }

                break;
            }

            if (processParticipant.isFor(retrievePersonFromParticipantBean(bean))) {
                if (process.isGuider(processParticipant) || process.isAssistantGuider(processParticipant)) {
                    throw new DomainException("error.PhdThesisProcess.president.cannot.be.guider.or.assistantguider");
                }
            }
        }
    }

    public void checkJuryReporterNotGuider(final PhdThesisJuryElementBean bean) {

        if (!bean.isReporter()) {
            return;
        }

        final PhdIndividualProgramProcess process = getIndividualProgramProcess();

        for (PhdParticipant processParticipant : process.getParticipants()) {
            if (processParticipant == bean.getParticipant()) {
                if (process.isGuider(processParticipant) || process.isAssistantGuider(processParticipant)) {
                    throw new DomainException("error.PhdThesisProcess.reporter.cannot.be.guider.or.assistantguider");
                }

                break;
            }

            if (processParticipant.isFor(retrievePersonFromParticipantBean(bean))) {
                if (process.isGuider(processParticipant) || process.isAssistantGuider(processParticipant)) {
                    throw new DomainException("error.PhdThesisProcess.reporter.cannot.be.guider.or.assistantguider");
                }
            }
        }

    }

    private Person retrievePersonFromParticipantBean(PhdThesisJuryElementBean participantBean) {
        if (participantBean.getParticipant() == null && participantBean.getPersonName() != null) {
            return participantBean.getPerson();
        }

        if (participantBean.getParticipant() != null && participantBean.getParticipant().isInternal()) {
            return ((InternalPhdParticipant) participantBean.getParticipant()).getPerson();
        }

        return null;
    }

    public PhdMeetingSchedulingProcessStateType getPhdMeetingSchedulingActiveState() {
        if (getMeetingProcess() == null) {
            return null;
        }

        return getMeetingProcess().getActiveState();
    }

    public List<PhdThesisProcessStateType> getPossibleNextStates() {
        PhdThesisProcessStateType currentState = getActiveState();

        if (currentState == null) {
            return Collections.singletonList(PhdThesisProcessStateType.NEW);
        }

        switch (currentState) {
        case NEW:
            return Arrays.asList(PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION,
                    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case WAITING_FOR_JURY_CONSTITUTION:
            return Arrays.asList(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION, PhdThesisProcessStateType.JURY_VALIDATED,
                    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case JURY_WAITING_FOR_VALIDATION:
            return Arrays.asList(PhdThesisProcessStateType.JURY_VALIDATED,
                    PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case JURY_VALIDATED:
            return Collections.singletonList(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case WAITING_FOR_JURY_REPORTER_FEEDBACK:
            return Collections.singletonList(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING);
        case WAITING_FOR_THESIS_MEETING_SCHEDULING:
            return Collections.singletonList(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING);
        case WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING:
            return Collections.singletonList(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED);
        case THESIS_DISCUSSION_DATE_SCHECULED:
            return Collections.singletonList(PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION);
        case WAITING_FOR_THESIS_RATIFICATION:
            return Collections.singletonList(PhdThesisProcessStateType.WAITING_FOR_FINAL_GRADE);
        case WAITING_FOR_FINAL_GRADE:
            return Collections.singletonList(PhdThesisProcessStateType.CONCLUDED);
        }

        return Collections.EMPTY_LIST;
    }

    public void removeLastState() {
        if (getStatesSet().size() <= 1) {
            throw new DomainException("phd.thesis.PhdThesisProcess.cannot.remove.state");
        }

        getMostRecentState().delete();
    }

    public PhdThesisProcess edit(final User userView, final PhdThesisProcessBean bean) {

        setWhenThesisDiscussionRequired(bean.getWhenThesisDiscussionRequired());

        setWhenJuryRequired(bean.getWhenJuryRequested());

        setWhenJuryValidated(bean.getWhenJuryValidated());
        setWhenJuryDesignated(bean.getWhenJuryDesignated());

        setWhenFinalThesisRatified(bean.getWhenFinalThesisRatified());

        setConclusionDate(bean.getConclusionDate());
        setFinalGrade(bean.getFinalGrade());

        return this;
    }

    @Override
    public PhdJuryElementsRatificationEntity getPhdJuryElementsRatificationEntity() {
        if (super.getPhdJuryElementsRatificationEntity() == null) {
            return PhdJuryElementsRatificationEntity.BY_COORDINATOR;
        }

        return super.getPhdJuryElementsRatificationEntity();
    }

    @Atomic
    public void createRequestFee() {
        new PhdThesisRequestFee(getIndividualProgramProcess());
    }

    @Override
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessState> getStates() {
        return getStatesSet();
    }

    @Override
    @Deprecated
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement> getThesisJuryElements() {
        return getThesisJuryElementsSet();
    }

    @Deprecated
    public boolean hasAnyThesisJuryElements() {
        return !getThesisJuryElementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasWhenThesisDiscussionRequired() {
        return getWhenThesisDiscussionRequired() != null;
    }

    @Deprecated
    public boolean hasConclusionDate() {
        return getConclusionDate() != null;
    }

    @Deprecated
    public boolean hasWhenJuryValidated() {
        return getWhenJuryValidated() != null;
    }

    @Deprecated
    public boolean hasMeetingProcess() {
        return getMeetingProcess() != null;
    }

    @Deprecated
    public boolean hasPresidentJuryElement() {
        return getPresidentJuryElement() != null;
    }

    @Deprecated
    public boolean hasWhenJuryDesignated() {
        return getWhenJuryDesignated() != null;
    }

    @Deprecated
    public boolean hasPhdJuryElementsRatificationEntity() {
        return getPhdJuryElementsRatificationEntity() != null;
    }

    @Deprecated
    public boolean hasDiscussionPlace() {
        return getDiscussionPlace() != null;
    }

    @Deprecated
    public boolean hasWhenFinalThesisRatified() {
        return getWhenFinalThesisRatified() != null;
    }

    @Deprecated
    public boolean hasDiscussionDate() {
        return getDiscussionDate() != null;
    }

    @Deprecated
    public boolean hasIndividualProgramProcess() {
        return getIndividualProgramProcess() != null;
    }

    @Deprecated
    public boolean hasPresidentTitle() {
        return getPresidentTitle() != null;
    }

    @Deprecated
    public boolean hasMeetingDate() {
        return getMeetingDate() != null;
    }

    @Deprecated
    public boolean hasFinalGrade() {
        return getFinalGrade() != null;
    }

    @Deprecated
    public boolean hasRatificationEntityCustomMessage() {
        return getRatificationEntityCustomMessage() != null;
    }

    @Deprecated
    public boolean hasMeetingPlace() {
        return getMeetingPlace() != null;
    }

    @Deprecated
    public boolean hasWhenJuryRequired() {
        return getWhenJuryRequired() != null;
    }

}
