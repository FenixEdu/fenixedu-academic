/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Set;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;

import org.joda.time.DateTime;

public class SendReceptionEmailBean implements java.io.Serializable {

    private DateTime beginDate;
    private DateTime endDate;
    private boolean includeOnlyProcessWithNoReceptionEmail;
    private Set<IndividualCandidacyProcess> subjectProcesses;
    private ErasmusCandidacyProcess erasmusCandidacyProcess;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SendReceptionEmailBean(final ErasmusCandidacyProcess erasmusCandidacyProcess) {
        this.erasmusCandidacyProcess = erasmusCandidacyProcess;
    }

    public void removeProcess(ErasmusIndividualCandidacyProcess individualCandidacyProcess) {
        this.subjectProcesses.remove(individualCandidacyProcess);
    }

    public void addProcess(ErasmusIndividualCandidacyProcess individualCandidacyProcess) {
        this.subjectProcesses.add(individualCandidacyProcess);
    }

    public void retrieveProcesses() {

    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isIncludeOnlyProcessWithNoReceptionEmail() {
        return includeOnlyProcessWithNoReceptionEmail;
    }

    public void setIncludeOnlyProcessWithNoReceptionEmail(boolean includeOnlyProcessWithNoReceptionEmail) {
        this.includeOnlyProcessWithNoReceptionEmail = includeOnlyProcessWithNoReceptionEmail;
    }

    public Set<IndividualCandidacyProcess> getSubjectProcesses() {
        return subjectProcesses;
    }

    public void setSubjectProcesses(Set<IndividualCandidacyProcess> subjectProcesses) {
        this.subjectProcesses = subjectProcesses;
    }

    public ErasmusCandidacyProcess getErasmusCandidacyProcess() {
        return erasmusCandidacyProcess;
    }

    public void setErasmusCandidacyProcess(ErasmusCandidacyProcess erasmusCandidacyProcess) {
        this.erasmusCandidacyProcess = erasmusCandidacyProcess;
    }

}