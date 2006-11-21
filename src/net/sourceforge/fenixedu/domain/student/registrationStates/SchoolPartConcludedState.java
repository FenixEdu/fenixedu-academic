package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class SchoolPartConcludedState extends SchoolPartConcludedState_Base {

    public SchoolPartConcludedState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.SCHOOLPARTCONCLUDED;
    }

    public void checkConditionsToForward() {
	checkConditionsToForward(RegistrationStateType.CONCLUDED.toString());
    }

    public void checkConditionsToForward(String nextState) {
	// TODO Auto-generated method stub

    }

    public Set<String> getValidNextStates() {
	Set<String> states = new HashSet<String>();
	states.add(RegistrationStateType.CONCLUDED.name());
	states.add(RegistrationStateType.CANCELED.name());
	states.add(RegistrationStateType.ABANDONED.name());
	states.add(RegistrationStateType.MOBILITY.name());
	return states;
    }

    public void nextState() {
	nextState(RegistrationStateType.CONCLUDED.toString());
    }

}
