package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdProgramProcessState extends PhdProgramProcessState_Base {

    private PhdProgramProcessState() {
	super();
    }

    public PhdProgramProcessState(final PhdIndividualProgramProcess process, final PhdIndividualProgramProcessState type,
	    final Person person) {
	this(process, type, person, null);
    }

    public PhdProgramProcessState(final PhdIndividualProgramProcess process, final PhdIndividualProgramProcessState type,
	    final Person person, final String remarks) {
	this();
	init(process, type, person, remarks);
    }

    private void init(PhdIndividualProgramProcess process, PhdIndividualProgramProcessState type, Person person, String remarks) {
	super.init(person, remarks);

	check(process, "error.PhdProgramProcessState.invalid.process");
	check(type, "error.PhdProgramProcessState.invalid.type");
	checkType(process, type);

	setProcess(process);
	setType(type);
    }

    private void checkType(final PhdIndividualProgramProcess process, final PhdIndividualProgramProcessState type) {
	final PhdIndividualProgramProcessState currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdProgramProcessState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

}
