/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;

public class SendReceptionEmailBean implements java.io.Serializable {

    private boolean includeOnlyProcessWithNoReceptionEmail;
    private Set<ErasmusIndividualCandidacyProcess> subjectProcesses;
    private ErasmusCandidacyProcess erasmusCandidacyProcess;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SendReceptionEmailBean(final ErasmusCandidacyProcess erasmusCandidacyProcess) {
        this.erasmusCandidacyProcess = erasmusCandidacyProcess;
	this.includeOnlyProcessWithNoReceptionEmail = true;

	retrieveProcesses();
    }

    public void removeProcess(ErasmusIndividualCandidacyProcess individualCandidacyProcess) {
        this.subjectProcesses.remove(individualCandidacyProcess);
    }

    public void addProcess(ErasmusIndividualCandidacyProcess individualCandidacyProcess) {
        this.subjectProcesses.add(individualCandidacyProcess);
    }

    private void retrieveProcesses() {
	subjectProcesses = new HashSet<ErasmusIndividualCandidacyProcess>();

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

    public Set<ErasmusIndividualCandidacyProcess> getSubjectProcesses() {
        return subjectProcesses;
    }

    public void setSubjectProcesses(Set<ErasmusIndividualCandidacyProcess> subjectProcesses) {
        this.subjectProcesses = subjectProcesses;
    }

    public ErasmusCandidacyProcess getErasmusCandidacyProcess() {
        return erasmusCandidacyProcess;
    }

    public void setErasmusCandidacyProcess(ErasmusCandidacyProcess erasmusCandidacyProcess) {
        this.erasmusCandidacyProcess = erasmusCandidacyProcess;
    }

}