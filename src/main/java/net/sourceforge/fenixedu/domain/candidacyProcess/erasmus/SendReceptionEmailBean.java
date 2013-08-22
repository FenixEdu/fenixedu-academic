/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

public class SendReceptionEmailBean implements java.io.Serializable {

    private boolean includeOnlyProcessWithNoReceptionEmail;
    private MobilityProgram mobilityProgram;
    private List<MobilityIndividualApplicationProcess> subjectProcesses;
    private MobilityApplicationProcess mobilityApplicationProcess;

    private String emailSubject;
    private String emailBody;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SendReceptionEmailBean(final MobilityApplicationProcess mobilityApplicationProcess) {
        this.mobilityApplicationProcess = mobilityApplicationProcess;
        includeOnlyProcessWithNoReceptionEmail = true;
        mobilityProgram = MobilityProgram.getByRegistrationAgreement(RegistrationAgreement.ERASMUS);
        emailSubject =
                mobilityApplicationProcess.getCandidacyPeriod()
                        .getEmailTemplateFor(mobilityProgram, MobilityEmailTemplateType.IST_RECEPTION).getSubject();
        emailBody =
                mobilityApplicationProcess.getCandidacyPeriod()
                        .getEmailTemplateFor(mobilityProgram, MobilityEmailTemplateType.IST_RECEPTION).getBody();
    }

    public void removeProcess(MobilityIndividualApplicationProcess individualCandidacyProcess) {
        this.subjectProcesses.remove(individualCandidacyProcess);
    }

    public void addProcess(MobilityIndividualApplicationProcess individualCandidacyProcess) {
        this.subjectProcesses.add(individualCandidacyProcess);
    }

    public void retrieveProcesses() {
        subjectProcesses = new ArrayList<MobilityIndividualApplicationProcess>();

        for (IndividualCandidacyProcess child : mobilityApplicationProcess.getChildProcesses()) {
            MobilityIndividualApplicationProcess individualCandidacyProcess = (MobilityIndividualApplicationProcess) child;

            if (!individualCandidacyProcess.isStudentAcceptedAndNotified()) {
                continue;
            }

            if (includeOnlyProcessWithNoReceptionEmail && individualCandidacyProcess.isStudentNotifiedWithReceptionEmail()) {
                continue;
            }

            if (individualCandidacyProcess.getMobilityProgram() != mobilityProgram) {
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

    public MobilityProgram getMobilityProgram() {
        return mobilityProgram;
    }

    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        this.mobilityProgram = mobilityProgram;
        emailSubject =
                mobilityApplicationProcess.getCandidacyPeriod()
                        .getEmailTemplateFor(mobilityProgram, MobilityEmailTemplateType.IST_RECEPTION).getSubject();
        emailBody =
                mobilityApplicationProcess.getCandidacyPeriod()
                        .getEmailTemplateFor(mobilityProgram, MobilityEmailTemplateType.IST_RECEPTION).getBody();
    }

    public List<MobilityIndividualApplicationProcess> getSubjectProcesses() {
        return subjectProcesses;
    }

    public void setSubjectProcesses(List<MobilityIndividualApplicationProcess> subjectProcesses) {
        this.subjectProcesses = subjectProcesses;
    }

    public MobilityApplicationProcess getMobilityApplicationProcess() {
        return mobilityApplicationProcess;
    }

    public void setMobilityApplicationProcess(MobilityApplicationProcess mobilityApplicationProcess) {
        this.mobilityApplicationProcess = mobilityApplicationProcess;
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