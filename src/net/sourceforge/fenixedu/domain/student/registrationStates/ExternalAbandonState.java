package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalAbandonState extends ExternalAbandonState_Base {

    public ExternalAbandonState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
    }

    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.abandoned");
    }

    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.abandoned");
    }

    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.abandoned");
    }

    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.abandoned");
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.EXTERNAL_ABANDON;
    }

}
