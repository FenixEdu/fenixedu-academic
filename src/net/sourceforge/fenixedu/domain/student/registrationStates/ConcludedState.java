package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.StateMachine;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

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

    public IState nextState() {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public IState nextState(String nextState) {
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
	    setStateDate(registration.getLastApprovementDate()); // just for interface viewing purposes
	    setFinalAverage(registration.getAverage().setScale(0, RoundingMode.HALF_UP).intValue());
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
	    if (getRegistration().isRegistrationConclusionProcessed()) {
		throw new DomainException("ConcludedState.cannot.create.concluded.state.on.registration.with.average");
	    }
	    
	    // conclusion state date is always current state
	    final DateTime stateDateTime = new YearMonthDay().toDateTimeAtCurrentTime();

	    RegistrationState conclusionState = null;
	    final RegistrationState previousState = getRegistration().getStateInDate(stateDateTime);
	    if (previousState == null) {
		conclusionState = RegistrationState.createState(getRegistration(), null, stateDateTime, getStateType());
	    } else if (previousState.getStateType() == RegistrationStateType.CONCLUDED) {
		conclusionState = previousState;
	    } else {
		StateMachine.execute(previousState, getStateType().name());
		conclusionState = getRegistration().getActiveState();
	    }
	    conclusionState.setStateDate(stateDateTime);
	    conclusionState.setResponsiblePerson(AccessControl.getUserView() == null ? null : AccessControl.getPerson());
	    conclusionState.setRemarks(getRemarks());

	    RegistrationState nextState = conclusionState.getNext();
	    if (nextState != null && !conclusionState.getValidNextStates().contains(nextState.getStateType().name())) {
		throw new DomainException("error.cannot.add.registrationState.incoherentState");
	    }

	    getRegistration().setFinalAverage(getFinalAverage());
	    return conclusionState;
	}
	
    }
    
}
