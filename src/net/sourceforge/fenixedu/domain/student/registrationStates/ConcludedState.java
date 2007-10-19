package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ConcludedState extends ConcludedState_Base {

    public ConcludedState(Registration registration, Person person, DateTime dateTime) {
	super();

	if (!registration.hasConcluded()) {
	    throw new DomainException("error.registration.is.not.concluded");
	}
	
	//TODO: Add cycle verification rule here
	
//	for (final CycleType cycleType : registration.getDegreeType().getCycleTypes()) {
//	    if (!registration.hasConcludedCycle(cycleType, (ExecutionYear) null)) {
//		final LabelFormatter labelFormatter = new LabelFormatter();
//		labelFormatter.appendLabel(cycleType.getDescription());
//		
//		throw new DomainExceptionWithLabelFormatter("error.registration.has.not.concluded.cycle", labelFormatter);
//	    }
//	}
	
	init(registration, person, dateTime);
	registration.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);
	if (registration.getStudent().getRegistrationsCount() == 1) {
	    registration.getPerson().removeRoleByType(RoleType.STUDENT);
	}
    }

    @Override
    public void delete() {
	if (getRegistration().hasConcludedDiplomaRequest()) {
	    throw new DomainException("cannot.delete.concluded.state.of.registration.with.concluded.diploma.request");
	} 
	
	getRegistration().setFinalAverage(null);
        super.delete();
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

    static public class RegistrationConcludedStateCreator extends RegistrationStateCreator {

	public RegistrationConcludedStateCreator(Registration registration) {
	    super(registration);
	    setStateType(RegistrationStateType.CONCLUDED);
	    setStateDate(registration.getLastApprovementDate());
	    setFinalAverage(registration.getAverage().intValue());
	}

	Integer finalAverage;
	
	public Integer getFinalAverage() {
	    return finalAverage;
	}

	public void setFinalAverage(Integer finalAverage) {
	    this.finalAverage = finalAverage;
	}

	@Override
	public Object execute() {
	    getRegistration().setFinalAverage(getFinalAverage());
	    return super.execute();
	}
	
    }
    
}
