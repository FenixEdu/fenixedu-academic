/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package org.fenixedu.academic.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityProgram;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.bennu.core.domain.Bennu;

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
        setMobilityProgram(findSomeDefaultMobilityProgram());
    }

    // Look for erasmus program
    private MobilityProgram findSomeDefaultMobilityProgram() {
        for (final MobilityProgram mobilityProgram : Bennu.getInstance().getProgramsSet()) {
            final RegistrationProtocol protocol = mobilityProgram.getRegistrationProtocol();
            if (!protocol.isEnrolmentByStudentAllowed() && !protocol.isToPayGratuity() && protocol.allowsIDCard()
                    && !protocol.isOnlyAllowedDegreeEnrolment() && !protocol.isAlien()
                    && protocol.allowDissertationCandidacyWithoutChecks() && !protocol.isForOfficialMobilityReporting()
                    && !protocol.attemptAlmaMatterFromPrecedent()) {
                return mobilityProgram;
            }
        }
        return null;
    }

    public void removeProcess(MobilityIndividualApplicationProcess individualCandidacyProcess) {
        this.subjectProcesses.remove(individualCandidacyProcess);
    }

    public void addProcess(MobilityIndividualApplicationProcess individualCandidacyProcess) {
        this.subjectProcesses.add(individualCandidacyProcess);
    }

    public void retrieveProcesses() {
        subjectProcesses = new ArrayList<MobilityIndividualApplicationProcess>();

        for (IndividualCandidacyProcess child : mobilityApplicationProcess.getChildProcessesSet()) {
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