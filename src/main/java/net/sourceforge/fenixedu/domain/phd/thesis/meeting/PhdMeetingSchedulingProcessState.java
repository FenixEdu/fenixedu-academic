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
package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class PhdMeetingSchedulingProcessState extends PhdMeetingSchedulingProcessState_Base {

    public PhdMeetingSchedulingProcessState() {
        super();
    }

    protected PhdMeetingSchedulingProcessState(PhdMeetingSchedulingProcess process, PhdMeetingSchedulingProcessStateType type,
            Person person, String remarks, final DateTime stateDate) {
        this();
        checkType(process, type);
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdMeetingSchedulingProcessState.invalid.process", args);
        }
        String[] args1 = {};
        if (type == null) {
            throw new DomainException("error.PhdMeetingSchedulingProcessState.invalid.type", args1);
        }

        setMeetingProcess(process);

        super.init(person, remarks, stateDate, type);

        setType(type);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
        throw new RuntimeException("invoke other init");
    }

    private void checkType(final PhdMeetingSchedulingProcess process, final PhdMeetingSchedulingProcessStateType type) {
        final PhdMeetingSchedulingProcessStateType currentType = process.getActiveState();
        if (currentType != null && currentType.equals(type)) {
            throw new PhdDomainOperationException("error.PhdMeetingSchedulingProcessState.equals.previous.state",
                    type.getLocalizedName());
        }
    }

    @Override
    protected void disconnect() {
        setMeetingProcess(null);
        super.disconnect();
    }

    @Override
    public boolean isLast() {
        return getMeetingProcess().getMostRecentState() == this;
    }

    public static PhdMeetingSchedulingProcessState createWithInferredStateDate(PhdMeetingSchedulingProcess process,
            PhdMeetingSchedulingProcessStateType type, Person person, String remarks) {

        DateTime stateDate = null;

        PhdMeetingSchedulingProcessState mostRecentState = process.getMostRecentState();

        switch (type) {
        case WAITING_FIRST_THESIS_MEETING_REQUEST:
            if (process.getThesisProcess().getWhenJuryValidated() == null) {
                throw new PhdDomainOperationException(
                        "error.phd.thesis.meeting.PhdMeetingSchedulingProcessState.whenJuryValidated.required");
            }

            stateDate = process.getThesisProcess().getWhenJuryValidated().toDateTimeAtStartOfDay();
            break;
        case WAITING_FIRST_THESIS_MEETING_SCHEDULE:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case WAITING_THESIS_MEETING_SCHEDULE:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
            break;
        case WITHOUT_THESIS_MEETING_REQUEST:
            stateDate = mostRecentState.getStateDate().plusMinutes(1);
        }

        return createWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PhdMeetingSchedulingProcessState createWithGivenStateDate(PhdMeetingSchedulingProcess process,
            PhdMeetingSchedulingProcessStateType type, Person person, String remarks, DateTime stateDate) {
        List<PhdMeetingSchedulingProcessStateType> possibleNextStates =
                PhdMeetingSchedulingProcessStateType.getPossibleNextStates(process);

        String expectedStatesDescription = buildExpectedStatesDescription(possibleNextStates);
        if (!possibleNextStates.contains(type)) {
            throw new PhdDomainOperationException("error.phd.thesis.meeting.PhdMeetingSchedulingProcessState.invalid.next.state",
                    type.getLocalizedName(), expectedStatesDescription);
        }

        return new PhdMeetingSchedulingProcessState(process, type, person, remarks, stateDate);
    }

    @Override
    public PhdProgramProcess getProcess() {
        return getMeetingProcess();
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasMeetingProcess() {
        return getMeetingProcess() != null;
    }

}
