package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import pt.ist.fenixWebFramework.services.Service;

public class PhdCandidacyFeedbackRequestElement extends PhdCandidacyFeedbackRequestElement_Base {

    protected PhdCandidacyFeedbackRequestElement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Service
    public void delete() {
	checkIfCanBeDeleted();
	disconnect();
	deleteDomainObject();
    }

    protected PhdCandidacyFeedbackRequestElement init(final PhdCandidacyFeedbackRequestProcess process,
	    PhdParticipant participant, PhdCandidacyFeedbackRequestElementBean bean) {

	check(process, "error.PhdCandidacyFeedbackRequestElement.invalid.process");
	checkParticipant(process, participant);

	setProcess(process);
	setParticipant(participant);

	setMailSubject(bean.getMailSubject());
	setMailBody(bean.getMailBody());

	return this;
    }

    private void checkParticipant(final PhdCandidacyFeedbackRequestProcess process, final PhdParticipant participant) {
	check(participant, "error.PhdCandidacyFeedbackRequestElement.participant.cannot.be.null");

	/*
	 * Can not have more than one element for the same process
	 */
	for (final PhdCandidacyFeedbackRequestElement element : participant.getCandidacyFeedbackRequestElements()) {
	    if (element.hasProcess() && element.getProcess().equals(process)) {
		throw new DomainException(
			"error.PhdCandidacyFeedbackRequestElement.participant.already.has.jury.element.in.process");
	    }
	}
    }

    private void checkIfCanBeDeleted() {
	if (hasAnyFeedbackDocuments()) {
	    throw new DomainException("error.PhdCandidacyFeedbackRequestElement.has.feedback.documents");
	}
    }

    private void disconnect() {

	final PhdParticipant participant = getParticipant();
	removeParticipant();
	participant.tryDelete();

	removeProcess();
	removeRootDomainObject();
    }

    public PhdCandidacyFeedbackRequestDocument getLastFeedbackDocument() {
	return hasAnyFeedbackDocuments() ? Collections.max(getFeedbackDocumentsSet(),
		PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
    }

    public String getEmail() {
	return getParticipant().getEmail();
    }

    static public PhdCandidacyFeedbackRequestElement create(final PhdCandidacyFeedbackRequestProcess process,
	    final PhdCandidacyFeedbackRequestElementBean bean) {
	return new PhdCandidacyFeedbackRequestElement().init(process, getOrCreateParticipant(process, bean), bean);
    }

    static private PhdParticipant getOrCreateParticipant(final PhdCandidacyFeedbackRequestProcess process,
	    final PhdCandidacyFeedbackRequestElementBean bean) {
	return !bean.hasParticipant() ? PhdParticipant.create(process.getIndividualProgramProcess(), bean) : bean
		.getParticipant();
    }

}
