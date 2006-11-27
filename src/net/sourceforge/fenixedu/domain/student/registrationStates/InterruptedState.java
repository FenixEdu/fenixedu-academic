package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InterruptedState extends InterruptedState_Base {

    public InterruptedState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.INTERRUPTED;
    }

    public void checkConditionsToForward() {
	throw new DomainException("error.no.default.nextState.defined");
    }

    public void checkConditionsToForward(String nextState) {
	// TODO Auto-generated method stub

    }

    public Set<String> getValidNextStates() {
	Set<String> states = new HashSet<String>();
	//states.add(RegistrationStateType.INTERRUPTED.name());
	states.add(RegistrationStateType.CANCELED.name());
	states.add(RegistrationStateType.REGISTERED.name());
	states.add(RegistrationStateType.INTERNAL_ABANDON.name());
	states.add(RegistrationStateType.EXTERNAL_ABANDON.name());
	states.add(RegistrationStateType.MOBILITY.name());
	return states;
    }

    public void nextState() {
	throw new DomainException("error.no.default.nextState.defined");
    }

}
