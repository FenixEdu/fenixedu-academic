package net.sourceforge.fenixedu.domain.phd.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdThesisProcessState extends PhdThesisProcessState_Base {

    private PhdThesisProcessState() {
	super();
    }

    PhdThesisProcessState(PhdThesisProcess process, PhdThesisProcessStateType type, Person person, String remarks) {
	this();

	super.init(person, remarks);

	check(process, "error.PhdThesisProcessState.invalid.process");
	check(type, "error.PhdThesisProcessState.invalid.type");

	checkType(process, type);

	setProcess(process);
	setType(type);
    }

    private void checkType(final PhdThesisProcess process, final PhdThesisProcessStateType type) {
	final PhdThesisProcessStateType currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdThesisProcessState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Override
    public boolean isLast() {
	return getProcess().getMostRecentState() == this;
    }

}
