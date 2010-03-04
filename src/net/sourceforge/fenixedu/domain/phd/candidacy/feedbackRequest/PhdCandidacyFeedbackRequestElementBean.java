package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;

public class PhdCandidacyFeedbackRequestElementBean extends PhdParticipantBean {

    static private final long serialVersionUID = -5365333247731361583L;

    private PhdCandidacyFeedbackRequestProcess feedbackProcess;

    private String mailSubject, mailBody;

    public PhdCandidacyFeedbackRequestElementBean(final PhdCandidacyFeedbackRequestProcess thesisProcess) {
	super(thesisProcess.getIndividualProgramProcess());
	setFeedbackProcess(thesisProcess);
    }

    public PhdCandidacyFeedbackRequestProcess getFeedbackProcess() {
	return feedbackProcess;
    }

    public void setFeedbackProcess(PhdCandidacyFeedbackRequestProcess feedbackProcess) {
	this.feedbackProcess = feedbackProcess;
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

}
