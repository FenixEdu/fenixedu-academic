package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdCandidacyFeedbackState extends PhdCandidacyFeedbackState_Base {

    private PhdCandidacyFeedbackState() {
	super();
    }

    public PhdCandidacyFeedbackState(final PhdCandidacyFeedbackRequestProcess process, final PhdCandidacyFeedbackStateType type,
	    final Person person) {
	this(process, type, person, null);
    }

    public PhdCandidacyFeedbackState(final PhdCandidacyFeedbackRequestProcess process, final PhdCandidacyFeedbackStateType type,
	    final Person person, final String remarks) {
	this();
	init(process, type, person, remarks);
    }

    private void init(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type, Person person,
	    String remarks) {
	super.init(person, remarks);

	check(process, type);

	setProcess(process);
	setType(type);
    }

    private void check(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type) {
	check(process, "error.PhdCandidacyProcessState.invalid.process");
	check(type, "error.PhdCandidacyProcessState.invalid.type");
	checkType(process, type);
    }

    private void checkType(final PhdCandidacyFeedbackRequestProcess process, final PhdCandidacyFeedbackStateType type) {
	final PhdCandidacyFeedbackStateType currentType = process.getActiveState();
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
    static public PhdCandidacyFeedbackState create(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type) {
	final PhdCandidacyFeedbackState result = new PhdCandidacyFeedbackState();

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
