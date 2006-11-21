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
public class ConcludedState extends ConcludedState_Base {

    public ConcludedState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
    }

    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.CONCLUDED;
    }

}
