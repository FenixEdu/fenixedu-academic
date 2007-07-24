package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ConcludedState extends ConcludedState_Base {

    public ConcludedState(Registration registration, Person person, DateTime dateTime) {
	super();

	for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
	    if (!registration.hasConcludedCycle(cycleType, (ExecutionYear) null)) {
		final LabelFormatter labelFormatter = new LabelFormatter();
		labelFormatter.appendLabel(cycleType.getDescription());
		
		throw new DomainExceptionWithLabelFormatter("error.registration.has.not.concluded.cycle", labelFormatter);
	    }
	}
	
	init(registration, person, dateTime);
	registration.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);
	if (registration.getStudent().getRegistrationsCount() == 1) {
	    registration.getPerson().removeRoleByType(RoleType.STUDENT);
	}
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

    @Override
    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.CONCLUDED;
    }

}
