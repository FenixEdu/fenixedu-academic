package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ApprovedLearningAgreementExecutedAction extends ApprovedLearningAgreementExecutedAction_Base {

	private ApprovedLearningAgreementExecutedAction() {
		super();
	}

	public ApprovedLearningAgreementExecutedAction(ApprovedLearningAgreementDocumentFile documentFile, ExecutedActionType type) {
		this();
		init(documentFile, type);
	}

	protected void init(ApprovedLearningAgreementDocumentFile documentFile, ExecutedActionType type) {
		super.init(type);

		if (documentFile == null) {
			throw new DomainException("error.erasmus.approved.learning.agreement.execution.action.document.file.is.null");
		}

		setApprovedLearningAgreement(documentFile);
	}

	public boolean isSentLearningAgreementAction() {
		return ExecutedActionType.SENT_APPROVED_LEARNING_AGREEMENT.equals(getType());
	}

	public boolean isViewedLearningAgreementAction() {
		return ExecutedActionType.VIEWED_APPROVED_LEARNING_AGREEMENT.equals(getType());
	}

	public boolean isSentEmailAcceptedStudent() {
		return ExecutedActionType.SENT_EMAIL_ACCEPTED_STUDENT.equals(getType());
	}
}
