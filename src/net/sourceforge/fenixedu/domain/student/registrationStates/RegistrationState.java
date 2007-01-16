package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.IdInternalBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.StateMachine;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;

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

    public static RegistrationState createState(Registration registration, Person person,
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
		.getUserView() != null ? AccessControl.getPerson() : null);
	setResponsiblePerson(person);
	setStateDate(stateDate != null ? stateDate : new DateTime());
    }

    protected void init(Registration registration) {
	init(registration, null, null);
    }

    public void nextState(String nextState) {
	createState(getRegistration(), AccessControl.getPerson(), null, RegistrationStateType
		.valueOf(nextState));
    }

    public abstract RegistrationStateType getStateType();

    @Checked("RegistrationStatePredicates.deletePredicate")
    public void delete() {
	RegistrationState nextState = getNext();
	RegistrationState previousState = getPrevious();
	if (nextState != null && previousState != null
		&& !previousState.getValidNextStates().contains(nextState.getStateType().name())) {
	    throw new DomainException("error.cannot.delete.registrationState.incoherentState");
	}
	removeRegistration();
	removeResponsiblePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    private RegistrationState getNext() {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(
		getRegistration().getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, DATE_COMPARATOR);
	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(); iter
		.hasNext();) {
	    RegistrationState state = (RegistrationState) iter.next();
	    if (state.equals(this)) {
		if (iter.hasNext()) {
		    return iter.next();
		}
		return null;
	    }
	}
	return null;
    }

    private RegistrationState getPrevious() {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(
		getRegistration().getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, DATE_COMPARATOR);
	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates
		.listIterator(sortedRegistrationsStates.size()); iter.hasPrevious();) {
	    RegistrationState state = (RegistrationState) iter.previous();
	    if (state.equals(this)) {
		if (iter.hasPrevious()) {
		    return iter.previous();
		}
		return null;
	    }
	}
	return null;
    }

    public static class RegistrationStateDeleter extends IdInternalBean implements FactoryExecutor {

	public RegistrationStateDeleter(Integer idInternal) {
	    super(idInternal);
	}

	public Object execute() {
	    RootDomainObject.getInstance().readRegistrationStateByOID(getIdInternal()).delete();
	    return null;
	}
    }

    public static class RegistrationStateCreator extends RegistrationStateBean implements
	    FactoryExecutor {

	public RegistrationStateCreator(Registration registration) {
	    super(registration);
	}

	public Object execute() {

	    RegistrationState createdState = null;

	    final RegistrationState previousState = getRegistration().getStateInDate(getStateDate());
	    if (previousState == null) {
		createdState = RegistrationState.createState(getRegistration(), null, getStateDate()
			.toDateTimeAtMidnight(), getStateType());
	    } else {
		StateMachine.execute(previousState, getStateType().name());
		createdState = getRegistration().getActiveState();
		createdState.setStateDate(getStateDate().toDateTimeAtMidnight());
	    }
	    createdState.setRemarks(getRemarks());

	    RegistrationState nextState = createdState.getNext();
	    if (nextState != null
		    && !createdState.getValidNextStates().contains(nextState.getStateType().name())) {
		throw new DomainException("error.cannot.add.registrationState.incoherentState");
	    }

	    return createdState;
	}

    }

}
