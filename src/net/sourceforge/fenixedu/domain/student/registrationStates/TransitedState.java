package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

import org.joda.time.DateTime;

public class TransitedState extends TransitedState_Base {

    public TransitedState() {
	super();
    }

    public TransitedState(final Registration registration, final Person responsiblePerson, final DateTime stateDate) {
	this();
	init(registration, responsiblePerson, stateDate);
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.TRANSITED;
    }

    public void checkConditionsToForward() {
    }

    public void checkConditionsToForward(String nextState) {
    }

    public Set<String> getValidNextStates() {
	return Collections.emptySet();
    }

    public IState nextState() {
	return null;
    }

}
