package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;

import org.joda.time.DateTime;

public class PhdMeetingSchedulingProcessState extends PhdMeetingSchedulingProcessState_Base {

    public PhdMeetingSchedulingProcessState() {
	super();
    }

    protected PhdMeetingSchedulingProcessState(PhdMeetingSchedulingProcess process, PhdMeetingSchedulingProcessStateType type,
	    Person person, String remarks, final DateTime stateDate) {
	this();
	checkType(process, type);
	check(process, "error.PhdMeetingSchedulingProcessState.invalid.process");
	check(type, "error.PhdMeetingSchedulingProcessState.invalid.type");

	setMeetingProcess(process);

	super.init(person, remarks, stateDate);


	setType(type);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
	throw new RuntimeException("invoke other init");
    }

    private void checkType(final PhdMeetingSchedulingProcess process, final PhdMeetingSchedulingProcessStateType type) {
	final PhdMeetingSchedulingProcessStateType currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdMeetingSchedulingProcessState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeMeetingProcess();
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
	List<PhdMeetingSchedulingProcessStateType> possibleNextStates = PhdMeetingSchedulingProcessStateType
		.getPossibleNextStates(process);
	
	if(!possibleNextStates.contains(type)) {
	    throw new DomainException("error.phd.thesis.meeting.PhdMeetingSchedulingProcessState.invalid.next.state");
	}

	return new PhdMeetingSchedulingProcessState(process, type, person, remarks, stateDate);
    }

    @Override
    public PhdProgramProcess getProcess() {
	return getMeetingProcess();
    }

}
