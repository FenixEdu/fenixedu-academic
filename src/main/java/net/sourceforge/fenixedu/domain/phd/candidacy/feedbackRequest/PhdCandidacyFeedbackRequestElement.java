package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import pt.ist.fenixframework.Atomic;

public class PhdCandidacyFeedbackRequestElement extends PhdCandidacyFeedbackRequestElement_Base {

    protected PhdCandidacyFeedbackRequestElement() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected PhdCandidacyFeedbackRequestElement init(final PhdCandidacyFeedbackRequestProcess process,
            PhdParticipant participant, PhdCandidacyFeedbackRequestElementBean bean) {

        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestElement.invalid.process", args);
        }
        checkParticipant(process, participant);

        setProcess(process);
        setParticipant(participant);

        setMailSubject(bean.getMailSubject());
        setMailBody(bean.getMailBody());

        return this;
    }

    private void checkParticipant(final PhdCandidacyFeedbackRequestProcess process, final PhdParticipant participant) {
        String[] args = {};
        if (participant == null) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestElement.participant.cannot.be.null", args);
        }

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

    public PhdProgramCandidacyProcess getCandidacyProcess() {
        return getProcess().getCandidacyProcess();
    }

    @Atomic
    public void delete() {
        checkIfCanBeDeleted();
        disconnect();
        deleteDomainObject();
    }

    private void checkIfCanBeDeleted() {
        if (hasAnyFeedbackDocuments()) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestElement.has.feedback.documents");
        }
    }

    private void disconnect() {

        final PhdParticipant participant = getParticipant();
        setParticipant(null);
        participant.tryDelete();

        setProcess(null);
        setRootDomainObject(null);
    }

    public PhdCandidacyFeedbackRequestDocument getLastFeedbackDocument() {
        return hasAnyFeedbackDocuments() ? Collections.max(getFeedbackDocumentsSet(),
                PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
    }

    public String getNameWithTitle() {
        return getParticipant().getNameWithTitle();
    }

    public String getName() {
        return getParticipant().getName();
    }

    public String getEmail() {
        return getParticipant().getEmail();
    }

    public boolean isFeedbackSubmitted() {
        return hasAnyFeedbackDocuments();
    }

    public boolean isFor(PhdCandidacyFeedbackRequestProcess process) {
        return getProcess().equals(process);
    }

    public boolean isFor(Person person) {
        return getParticipant().isFor(person);
    }

    static public PhdCandidacyFeedbackRequestElement create(final PhdCandidacyFeedbackRequestProcess process,
            final PhdCandidacyFeedbackRequestElementBean bean) {

        return new PhdCandidacyFeedbackRequestElement().init(process,
                PhdParticipant.getUpdatedOrCreate(process.getIndividualProgramProcess(), bean), bean);
    }

    static public PhdCandidacyFeedbackRequestElement create(final PhdCandidacyFeedbackRequestProcess process,
            final PhdParticipant participant, final PhdCandidacyFeedbackRequestElementBean bean) {
        return new PhdCandidacyFeedbackRequestElement().init(process, participant, bean);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestDocument> getFeedbackDocuments() {
        return getFeedbackDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyFeedbackDocuments() {
        return !getFeedbackDocumentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMailBody() {
        return getMailBody() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

    @Deprecated
    public boolean hasParticipant() {
        return getParticipant() != null;
    }

    @Deprecated
    public boolean hasMailSubject() {
        return getMailSubject() != null;
    }

}
