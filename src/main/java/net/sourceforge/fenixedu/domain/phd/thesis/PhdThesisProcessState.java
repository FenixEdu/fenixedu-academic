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
package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class PhdThesisProcessState extends PhdThesisProcessState_Base {

    private PhdThesisProcessState() {
        super();
    }

    protected PhdThesisProcessState(PhdThesisProcess process, PhdThesisProcessStateType type, Person person, String remarks,
            final DateTime stateDate) {
        this();
        String[] args = {};

        if (process == null) {
            throw new DomainException("error.PhdThesisProcessState.invalid.process", args);
        }
        String[] args1 = {};
        if (type == null) {
            throw new DomainException("error.PhdThesisProcessState.invalid.type", args1);
        }

        checkType(process, type);

        setProcess(process);

        super.init(person, remarks, stateDate, type);
        setType(type);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
        throw new RuntimeException("invoke other init");
    }

    private void checkType(final PhdThesisProcess process, final PhdThesisProcessStateType type) {
        final PhdThesisProcessStateType currentType = process.getActiveState();
        if (currentType != null && currentType.equals(type)) {
            throw new PhdDomainOperationException("error.PhdThesisProcessState.equals.previous.state", type.getLocalizedName());
        }
    }

    @Override
    protected void disconnect() {
        setProcess(null);
        super.disconnect();
    }

    @Override
    public boolean isLast() {
        return getProcess().getMostRecentState() == this;
    }

    public static PhdThesisProcessState createWithInferredStateDate(PhdThesisProcess process, PhdThesisProcessStateType type,
            Person person, String remarks) {

        DateTime stateDate = null;

        PhdThesisProcessState mostRecentState = process.getMostRecentState();

        switch (type) {
        case NEW:
            if (process.getWhenThesisDiscussionRequired() == null) {
                throw new PhdDomainOperationException(
                        "error.phd.thesis.PhdThesisProcessState.whenThesisDiscussionRequired.required");
            }

            stateDate = process.getWhenThesisDiscussionRequired().toDateTimeAtStartOfDay();
            break;
        case WAITING_FOR_JURY_CONSTITUTION:
            if (process.getWhenRequestJury() == null) {
                throw new PhdDomainOperationException("error.phd.thesis.PhdThesisProcessState.whenRequestJury.required");
            }

            stateDate = process.getWhenRequestJury().plusMinutes(1);
            break;
        case JURY_WAITING_FOR_VALIDATION:
            if (process.getWhenJuryDesignated() == null) {
                throw new PhdDomainOperationException("error.phd.thesis.PhdThesisProcessState.whenJuryDesignated.required");
            }

            stateDate = process.getWhenJuryDesignated().toDateTimeAtStartOfDay();
            break;
        case JURY_VALIDATED:
            if (process.getWhenJuryValidated() == null) {
                throw new PhdDomainOperationException("error.phd.thesis.PhdThesisProcessState.whenJuryValidated.required");
            }

            stateDate = process.getWhenJuryValidated().toDateTimeAtStartOfDay();
            break;
        case WAITING_FOR_JURY_REPORTER_FEEDBACK:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case WAITING_FOR_THESIS_MEETING_SCHEDULING:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING:
            if (process.getMeetingDate() == null) {
                throw new PhdDomainOperationException("error.phd.thesis.PhdThesisProcessState.meetingDate.required");
            }

            stateDate = process.getMeetingDate();
            break;
        case THESIS_DISCUSSION_DATE_SCHECULED:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case WAITING_FOR_THESIS_RATIFICATION:
            if (process.getDiscussionDate() == null) {
                throw new PhdDomainOperationException("error.phd.thesis.PhdThesisProcessState.discussionDate.required");
            }

            stateDate = process.getDiscussionDate();
            break;
        case WAITING_FOR_FINAL_GRADE:
            if (process.getWhenFinalThesisRatified() == null) {
                throw new PhdDomainOperationException("error.phd.thesis.PhdThesisProcessState.whenFinalThesisRatified.required");
            }

            stateDate = process.getWhenFinalThesisRatified().toDateTimeAtStartOfDay();
            break;
        case CONCLUDED:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        }

        return createWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PhdThesisProcessState createWithGivenStateDate(PhdThesisProcess process, PhdThesisProcessStateType type,
            Person person, String remarks, final DateTime stateDate) {
        List<PhdThesisProcessStateType> possibleNextStates = PhdThesisProcessStateType.getPossibleNextStates(process);

        if (!possibleNextStates.contains(type)) {
            String expectedStatesDescription = buildExpectedStatesDescription(possibleNextStates);
            throw new PhdDomainOperationException("phd.thesis.PhdThesisProcess.invalid.next.state", type.getLocalizedName(),
                    expectedStatesDescription);
        }

        return new PhdThesisProcessState(process, type, person, remarks, stateDate);
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

}
