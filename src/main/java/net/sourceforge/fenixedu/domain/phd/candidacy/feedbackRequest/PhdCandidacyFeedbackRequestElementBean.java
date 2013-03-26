package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class PhdCandidacyFeedbackRequestElementBean extends PhdParticipantBean {

    static private final long serialVersionUID = -5365333247731361583L;

    private PhdCandidacyFeedbackRequestProcess feedbackProcess;

    private List<PhdParticipant> participants;

    private String mailSubject, mailBody;

    public PhdCandidacyFeedbackRequestElementBean(final PhdProgramCandidacyProcess process) {
        super(process.getIndividualProgramProcess());
        setFeedbackProcess(process.getFeedbackRequest());
    }

    public PhdCandidacyFeedbackRequestProcess getFeedbackProcess() {
        return feedbackProcess;
    }

    public void setFeedbackProcess(PhdCandidacyFeedbackRequestProcess feedbackProcess) {
        this.feedbackProcess = feedbackProcess;
    }

    public List<PhdParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<PhdParticipant> participants) {
        this.participants = participants;
    }

    public boolean hasAnyParticipants() {
        return !this.participants.isEmpty();
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public void updateWithExistingPhdParticipants() {
        setParticipants(new ArrayList<PhdParticipant>());

        if (!getExistingParticipants().isEmpty()) {
            setParticipantSelectType(PhdParticipantSelectType.EXISTING);
        } else {
            setParticipantSelectType(PhdParticipantSelectType.NEW);
        }
    }

    public List<PhdParticipant> getExistingParticipants() {
        final List<PhdParticipant> result = new ArrayList<PhdParticipant>();
        for (final PhdParticipant participant : getIndividualProgramProcess().getParticipantsSet()) {
            if (!participant.hasAnyCandidacyFeedbackRequestElements()) {
                result.add(participant);
            }
        }
        return result;
    }

}
