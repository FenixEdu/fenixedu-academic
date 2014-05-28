/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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