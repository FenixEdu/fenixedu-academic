package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

import org.joda.time.DateTime;

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
	states.add(RegistrationStateType.INTERNAL_ABANDON.name());
	states.add(RegistrationStateType.EXTERNAL_ABANDON.name());
	states.add(RegistrationStateType.MOBILITY.name());
	return states;
    }

    public IState nextState() {
	return nextState(RegistrationStateType.CONCLUDED.toString());
    }

}
