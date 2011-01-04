package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdCandidacyProcessState extends PhdCandidacyProcessState_Base {

    private PhdCandidacyProcessState() {
	super();
    }

    public PhdCandidacyProcessState(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type,
	    final Person person) {
	this(process, type, person, null);
    }

    public PhdCandidacyProcessState(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type,
	    final Person person, final String remarks) {
	this();
	init(process, type, person, remarks);
    }

    private void init(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type, Person person, String remarks) {
	super.init(person, remarks);

	check(process, type);

	setProcess(process);
	setType(type);
    }

    private void check(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type) {
	check(process, "error.PhdCandidacyProcessState.invalid.process");
	check(type, "error.PhdCandidacyProcessState.invalid.type");
	checkType(process, type);
    }

    private void checkType(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type) {
	final PhdProgramCandidacyProcessState currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdCandidacyProcessState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    static public PhdCandidacyProcessState create(PhdProgramCandidacyProcess process, PhdProgramCandidacyProcessState type) {
	final PhdCandidacyProcessState result = new PhdCandidacyProcessState();

	result.check(process, type);
	result.setProcess(process);
	result.setType(type);

	return result;
    }

    @Override
    public boolean isLast() {
	return getProcess().getMostRecentState() == this;
    }

}
