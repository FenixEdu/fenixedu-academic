package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class ApprovedLearningAgreementDocumentFile extends ApprovedLearningAgreementDocumentFile_Base {

	public static final Comparator<ApprovedLearningAgreementDocumentFile> SUBMISSION_DATE_COMPARATOR =
			new Comparator<ApprovedLearningAgreementDocumentFile>() {

				@Override
				public int compare(ApprovedLearningAgreementDocumentFile o1, ApprovedLearningAgreementDocumentFile o2) {
					return o1.getUploadTime().compareTo(o2.getUploadTime());
				}
			};

	private ApprovedLearningAgreementDocumentFile() {
		super();
		this.setCandidacyFileActive(Boolean.TRUE);
	}

	public ApprovedLearningAgreementDocumentFile(IndividualCandidacy candidacy, byte[] contents, String filename) {
		this();
		this.setCandidacyFileActive(Boolean.TRUE);
		addIndividualCandidacy(candidacy);
		setCandidacyFileType(IndividualCandidacyDocumentFileType.APPROVED_LEARNING_AGREEMENT);
		init(getVirtualPath(candidacy), filename, filename, null, contents, null);
	}

	protected ApprovedLearningAgreementDocumentFile(byte[] contents, String filename, VirtualPath path) {
		this();
		this.setCandidacyFileActive(Boolean.TRUE);
		setCandidacyFileType(IndividualCandidacyDocumentFileType.APPROVED_LEARNING_AGREEMENT);
		init(path, filename, filename, null, contents, null);
	}

	@Service
	public static ApprovedLearningAgreementDocumentFile createCandidacyDocument(byte[] contents, String filename,
			String processName, String documentIdNumber) {
		return new ApprovedLearningAgreementDocumentFile(contents, filename, obtainVirtualPath(processName, documentIdNumber));
	}

	@Service
	public void markLearningAgreementViewed() {
		new ApprovedLearningAgreementExecutedAction(this, ExecutedActionType.VIEWED_APPROVED_LEARNING_AGREEMENT);
	}

	@Service
	public void markLearningAgreementSent() {
		new ApprovedLearningAgreementExecutedAction(this, ExecutedActionType.SENT_APPROVED_LEARNING_AGREEMENT);
	}

	public boolean isApprovedLearningAgreementSent() {
		return !getSentLearningAgreementActions().isEmpty();
	}

	public boolean isApprovedLearningAgreementViewed() {
		return !getViewedLearningAgreementActions().isEmpty();
	}

	protected List<ApprovedLearningAgreementExecutedAction> getSentLearningAgreementActions() {
		List<ApprovedLearningAgreementExecutedAction> executedActionList =
				new ArrayList<ApprovedLearningAgreementExecutedAction>();

		CollectionUtils.select(getExecutedActions(), new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				return ((ApprovedLearningAgreementExecutedAction) arg0).isSentLearningAgreementAction();
			};

		}, executedActionList);

		Collections.sort(executedActionList, Collections.reverseOrder(ExecutedAction.WHEN_OCCURED_COMPARATOR));

		return executedActionList;
	}

	public ApprovedLearningAgreementExecutedAction getMostRecentSentLearningAgreementAction() {
		List<ApprovedLearningAgreementExecutedAction> executedActionList = getSentLearningAgreementActions();

		return executedActionList.isEmpty() ? null : executedActionList.get(0);
	}

	public DateTime getMostRecentSentLearningAgreementActionWhenOccured() {
		if (getMostRecentSentLearningAgreementAction() == null) {
			return null;
		}

		return getMostRecentSentLearningAgreementAction().getWhenOccured();
	}

	protected List<ApprovedLearningAgreementExecutedAction> getViewedLearningAgreementActions() {
		List<ApprovedLearningAgreementExecutedAction> executedActionList =
				new ArrayList<ApprovedLearningAgreementExecutedAction>();

		CollectionUtils.select(getExecutedActions(), new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				return ((ApprovedLearningAgreementExecutedAction) arg0).isViewedLearningAgreementAction();
			};

		}, executedActionList);

		Collections.sort(executedActionList, Collections.reverseOrder(ExecutedAction.WHEN_OCCURED_COMPARATOR));

		return executedActionList;
	}

	public ApprovedLearningAgreementExecutedAction getMostRecentViewedLearningAgreementAction() {
		List<ApprovedLearningAgreementExecutedAction> executedActionList = getViewedLearningAgreementActions();

		return executedActionList.isEmpty() ? null : executedActionList.get(0);
	}

	public DateTime getMostRecentViewedLearningAgreementActionWhenOccured() {
		if (getMostRecentViewedLearningAgreementAction() == null) {
			return null;
		}

		return getMostRecentViewedLearningAgreementAction().getWhenOccured();
	}

	protected List<ApprovedLearningAgreementExecutedAction> getSentEmailAcceptedStudentActions() {
		List<ApprovedLearningAgreementExecutedAction> executedActionList =
				new ArrayList<ApprovedLearningAgreementExecutedAction>();

		CollectionUtils.select(getExecutedActions(), new Predicate() {

			@Override
			public boolean evaluate(Object arg0) {
				return ((ApprovedLearningAgreementExecutedAction) arg0).isSentEmailAcceptedStudent();
			};

		}, executedActionList);

		Collections.sort(executedActionList, Collections.reverseOrder(ExecutedAction.WHEN_OCCURED_COMPARATOR));

		return executedActionList;
	}

	public ApprovedLearningAgreementExecutedAction getMostRecentSentEmailAcceptedStudentAction() {
		List<ApprovedLearningAgreementExecutedAction> executedActionList = getSentEmailAcceptedStudentActions();

		return executedActionList.isEmpty() ? null : executedActionList.get(0);
	}

	public DateTime getMostRecentSentEmailAcceptedStudentActionWhenOccured() {
		if (getMostRecentSentEmailAcceptedStudentAction() == null) {
			return null;
		}

		return getMostRecentSentEmailAcceptedStudentAction().getWhenOccured();
	}

	public boolean isMostRecent() {
		return getMobilityIndividualApplication().getMostRecentApprovedLearningAgreement() == this;
	}

	public MobilityIndividualApplicationProcess getProcess() {
		return getMobilityIndividualApplication().getCandidacyProcess();
	}

	public boolean isAbleToSendEmailToAcceptStudent() {
		return getProcess().isStudentAccepted() && isMostRecent() && getCandidacyFileActive();
	}
}
