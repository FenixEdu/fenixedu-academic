package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

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

	check(process, "error.PhdCandidacyProcessState.invalid.process");
	check(type, "error.PhdCandidacyProcessState.invalid.type");
	checkType(process, type);

	setProcess(process);
	setType(type);
    }

    private void checkType(final PhdProgramCandidacyProcess process, final PhdProgramCandidacyProcessState type) {
	final PhdProgramCandidacyProcessState currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PhdCandidacyProcessState.equals.previous.state");
	}
    }

}
