/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;

public class SendReceptionEmailBean implements java.io.Serializable {

    private boolean includeOnlyProcessWithNoReceptionEmail;
    private List<ErasmusIndividualCandidacyProcess> subjectProcesses;
    private ErasmusCandidacyProcess erasmusCandidacyProcess;

    private String emailSubject;
    private String emailBody;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SendReceptionEmailBean(final ErasmusCandidacyProcess erasmusCandidacyProcess) {
	this.erasmusCandidacyProcess = erasmusCandidacyProcess;
	this.includeOnlyProcessWithNoReceptionEmail = true;
	this.emailSubject = erasmusCandidacyProcess.getReceptionEmailSubject();
	this.emailBody = erasmusCandidacyProcess.getReceptionEmailBody();
    }

    public void removeProcess(ErasmusIndividualCandidacyProcess individualCandidacyProcess) {
	this.subjectProcesses.remove(individualCandidacyProcess);
    }

    public void addProcess(ErasmusIndividualCandidacyProcess individualCandidacyProcess) {
	this.subjectProcesses.add(individualCandidacyProcess);
    }

    public void retrieveProcesses() {
	subjectProcesses = new ArrayList<ErasmusIndividualCandidacyProcess>();

	int i = 0;
	for (IndividualCandidacyProcess child : erasmusCandidacyProcess.getChildProcesses()) {
	    ErasmusIndividualCandidacyProcess individualCandidacyProcess = (ErasmusIndividualCandidacyProcess) child;

	    if (!individualCandidacyProcess.isStudentAcceptedAndNotified()) {
		continue;
	    }

	    if (includeOnlyProcessWithNoReceptionEmail && individualCandidacyProcess.isStudentNotifiedWithReceptionEmail()) {
		continue;
	    }

	    subjectProcesses.add(individualCandidacyProcess);
	}
    }

    public boolean isIncludeOnlyProcessWithNoReceptionEmail() {
	return includeOnlyProcessWithNoReceptionEmail;
    }

    public void setIncludeOnlyProcessWithNoReceptionEmail(boolean includeOnlyProcessWithNoReceptionEmail) {
	this.includeOnlyProcessWithNoReceptionEmail = includeOnlyProcessWithNoReceptionEmail;
    }

    public List<ErasmusIndividualCandidacyProcess> getSubjectProcesses() {
	return subjectProcesses;
    }

    public void setSubjectProcesses(List<ErasmusIndividualCandidacyProcess> subjectProcesses) {
	this.subjectProcesses = subjectProcesses;
    }

    public ErasmusCandidacyProcess getErasmusCandidacyProcess() {
	return erasmusCandidacyProcess;
    }

    public void setErasmusCandidacyProcess(ErasmusCandidacyProcess erasmusCandidacyProcess) {
	this.erasmusCandidacyProcess = erasmusCandidacyProcess;
    }

    public String getEmailSubject() {
	return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
	this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
	return emailBody;
    }

    public void setEmailBody(String emailBody) {
	this.emailBody = emailBody;
    }

}