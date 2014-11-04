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
package org.fenixedu.academic.domain.phd.candidacy.feedbackRequest;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdParticipantBean;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcess;

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
            if (participant.getCandidacyFeedbackRequestElementsSet().isEmpty()) {
                result.add(participant);
            }
        }
        return result;
    }

}
