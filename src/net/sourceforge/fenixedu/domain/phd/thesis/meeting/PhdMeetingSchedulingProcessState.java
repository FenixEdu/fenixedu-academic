package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdMeetingSchedulingProcessState extends PhdMeetingSchedulingProcessState_Base {

    public PhdMeetingSchedulingProcessState() {
	super();
    }

    PhdMeetingSchedulingProcessState(PhdMeetingSchedulingProcess process, PhdMeetingSchedulingProcessStateType type,
	    Person person, String remarks) {
	this();

	super.init(person, remarks);

	check(process, "error.PhdMeetingSchedulingProcessState.invalid.process");
	check(type, "error.PhdMeetingSchedulingProcessState.invalid.type");

	checkType(process, type);

	setMeetingProcess(process);
	setType(type);
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
	return false;
    }

}
