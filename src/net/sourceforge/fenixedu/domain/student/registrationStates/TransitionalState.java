package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

import org.joda.time.DateTime;

public class TransitionalState extends TransitionalState_Base {

    public TransitionalState() {
	super();
    }

    public TransitionalState(final Registration registration, final Person responsiblePerson, final DateTime stateDate) {
	this();
	init(registration, responsiblePerson, stateDate);
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.TRANSITION;
    }

    public void checkConditionsToForward() {
    }

    public void checkConditionsToForward(String nextState) {
    }

    public Set<String> getValidNextStates() {
	final Set<String> result = new HashSet<String>();
	result.add(RegistrationStateType.TRANSITED.name());
	result.add(RegistrationStateType.CANCELED.name());

	return result;
    }

    public IState nextState() {
	return null;
    }

}
