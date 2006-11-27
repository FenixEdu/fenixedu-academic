package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.StateMachine;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class RegistrationState extends RegistrationState_Base implements IState {

    public static Comparator<RegistrationState> DATE_COMPARATOR = new Comparator<RegistrationState>() {
	public int compare(RegistrationState leftState, RegistrationState rightState) {
	    int comparationResult = leftState.getStateDate().compareTo(rightState.getStateDate());
	    return (comparationResult == 0) ? leftState.getIdInternal().compareTo(
		    rightState.getIdInternal()) : comparationResult;
	}
    };

    public RegistrationState() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    protected static RegistrationState createState(Registration registration, Person person,
	    DateTime dateTime, RegistrationStateType stateType) {

	switch (stateType) {
	case REGISTERED:
	    return new RegisteredState(registration, person, dateTime);
	case CANCELED:
	    return new CanceledState(registration, person, dateTime);
	case CONCLUDED:
	    return new ConcludedState(registration, person, dateTime);
	case FLUNKED:
	    return new FlunkedState(registration, person, dateTime);
	case INTERRUPTED:
	    return new InterruptedState(registration, person, dateTime);
	case SCHOOLPARTCONCLUDED:
	    return new SchoolPartConcludedState(registration, person, dateTime);
	case INTERNAL_ABANDON:
	    return new InternalAbandonState(registration, person, dateTime);
	case EXTERNAL_ABANDON:
	    return new ExternalAbandonState(registration, person, dateTime);
	case MOBILITY:
	    return new MobilityState(registration, person, dateTime);
	}

	return null;
    }

    protected void init(Registration registration, Person responsiblePerson, DateTime stateDate) {
	setRegistration(registration != null ? registration : null);
	final Person person = responsiblePerson != null ? responsiblePerson : (AccessControl
		.getUserView() != null ? AccessControl.getUserView().getPerson() : null);
	setResponsiblePerson(person);
	setStateDate(stateDate != null ? stateDate : new DateTime());
    }

    protected void init(Registration registration) {
	init(registration, null, null);
    }

    public void nextState(String nextState) {
	createState(getRegistration(), AccessControl.getUserView().getPerson(), null,
		RegistrationStateType.valueOf(nextState));
    }

    public abstract RegistrationStateType getStateType();

    public static class RegistrationStateCreator extends RegistrationStateBean implements
	    FactoryExecutor {

	public RegistrationStateCreator(Registration registration) {
	    super(registration);
	}

	public Object execute() {
	    final RegistrationState previousState = getRegistration().getActiveState();
	    StateMachine.execute(previousState, getStateType().name());
	    final RegistrationState createdState = getRegistration().getActiveState();
	    createdState.setRemarks(getRemarks());
	    if (!getStateDate().equals(createdState.getStateDate().toYearMonthDay())
		    && previousState.getStateDate().isBefore(getStateDate().toDateTimeAtMidnight())) {
		createdState.setStateDate(getStateDate().toDateTimeAtMidnight());
	    }
	    return createdState;
	}

    }

}
