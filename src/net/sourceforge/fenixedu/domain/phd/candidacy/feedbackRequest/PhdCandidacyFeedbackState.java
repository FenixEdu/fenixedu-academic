package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdCandidacyFeedbackState extends PhdCandidacyFeedbackState_Base {

    private PhdCandidacyFeedbackState() {
	super();
    }

    protected PhdCandidacyFeedbackState(final PhdCandidacyFeedbackRequestProcess process, final PhdCandidacyFeedbackStateType type,
	    final Person person, final String remarks, final DateTime stateDate) {
	this();
	init(process, type, person, remarks, stateDate);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
	throw new RuntimeException("invoke other init");
    }

    private void init(PhdCandidacyFeedbackRequestProcess process, PhdCandidacyFeedbackStateType type, Person person,
	    String remarks, final DateTime stateDate) {
	check(process, type);
	setProcess(process);
	super.init(person, remarks, stateDate, type);
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
	    throw new PhdDomainOperationException("error.PhdCandidacyProcessState.equals.previous.state", type.getLocalizedName());
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

    public PhdCandidacyFeedbackState createWithInferredStateDate(final PhdCandidacyFeedbackRequestProcess process,
	    final PhdCandidacyFeedbackStateType type, final Person person, final String remarks) {
	return createWithGivenStateDate(process, type, person, remarks, new DateTime());
    }

    public PhdCandidacyFeedbackState createWithGivenStateDate(final PhdCandidacyFeedbackRequestProcess process,
	    final PhdCandidacyFeedbackStateType type, final Person person, final String remarks, final DateTime stateDate) {
	List<PhdCandidacyFeedbackStateType> possibleNextStates = PhdCandidacyFeedbackStateType.getPossibleNextStates(type);

	if (!possibleNextStates.contains(type)) {
	    String description = buildExpectedStatesDescription(possibleNextStates);

	    throw new PhdDomainOperationException("error.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackState.invalid.state",
		    type.getLocalizedName(),
		    description);
	}

	return new PhdCandidacyFeedbackState(process, type, person, remarks, stateDate);
    }

}
