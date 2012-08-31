package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import dml.runtime.RelationAdapter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class RegistrationState extends RegistrationState_Base implements IState {

    static {
	RegistrationStateRegistration.addListener(new RelationAdapter<RegistrationState, Registration>() {

	    @Override
	    public void afterAdd(RegistrationState state, Registration registration) {
		super.afterAdd(state, registration);

		if (registration != null && state != null) {
		    new RegistrationStateLog(state, EnrolmentAction.ENROL, AccessControl.getPerson());
		}
	    }

	    @Override
	    public void beforeRemove(RegistrationState state, Registration registration) {
		super.beforeRemove(state, registration);

		if (registration != null && state != null) {
		    new RegistrationStateLog(state, EnrolmentAction.UNENROL, AccessControl.getPerson());
		}
	    }

	});
    }

    public static Comparator<RegistrationState> DATE_COMPARATOR = new Comparator<RegistrationState>() {
	@Override
	public int compare(RegistrationState leftState, RegistrationState rightState) {
	    int comparationResult = leftState.getStateDate().compareTo(rightState.getStateDate());
	    return (comparationResult == 0) ? leftState.getIdInternal().compareTo(rightState.getIdInternal()) : comparationResult;
	}
    };

    public static Comparator<RegistrationState> DATE_AND_STATE_TYPE_COMPARATOR = new Comparator<RegistrationState>() {
	@Override
	public int compare(RegistrationState leftState, RegistrationState rightState) {
	    int comparationResult = DATE_COMPARATOR.compare(leftState, rightState);
	    if (comparationResult != 0) {
		return comparationResult;
	    }
	    comparationResult = leftState.getStateType().compareTo(rightState.getStateType());
	    return (comparationResult == 0) ? leftState.getIdInternal().compareTo(rightState.getIdInternal()) : comparationResult;
	}
    };

    public RegistrationState() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    private static RegistrationState createState(Registration registration, Person person, DateTime dateTime,
	    RegistrationStateType stateType) {

	RegistrationState newState = null;
	switch (stateType) {
	case REGISTERED:
	    newState = new RegisteredState(registration, person, dateTime);
	    break;
	case CANCELED:
	    newState = new CanceledState(registration, person, dateTime);
	    break;
	case CONCLUDED:
	    newState = new ConcludedState(registration, person, dateTime);
	    break;
	case FLUNKED:
	    newState = new FlunkedState(registration, person, dateTime);
	    break;
	case INTERRUPTED:
	    newState = new InterruptedState(registration, person, dateTime);
	    break;
	case SCHOOLPARTCONCLUDED:
	    newState = new SchoolPartConcludedState(registration, person, dateTime);
	    break;
	case STUDYPLANCONCLUDED:
	    newState = new StudyPlanConcludedState(registration, person, dateTime);
	    break;
	case INTERNAL_ABANDON:
	    newState = new InternalAbandonState(registration, person, dateTime);
	    break;
	case EXTERNAL_ABANDON:
	    newState = new ExternalAbandonState(registration, person, dateTime);
	    break;
	case MOBILITY:
	    newState = new MobilityState(registration, person, dateTime);
	    break;
	case TRANSITION:
	    newState = new TransitionalState(registration, person, dateTime);
	    break;
	case TRANSITED:
	    newState = new TransitedState(registration, person, dateTime);
	    break;
	case INACTIVE:
	    newState = new InactiveState(registration, person, dateTime);
	    break;
	}
	registration.getStudent().updateStudentRole();

	return newState;
    }

    protected void init(Registration registration, Person responsiblePerson, DateTime stateDate) {
	setStateDate(stateDate != null ? stateDate : new DateTime());
	setRegistration(registration);
	setResponsiblePerson(selectPerson(responsiblePerson));
    }

    private Person selectPerson(final Person responsiblePerson) {
	if (responsiblePerson != null) {
	    return responsiblePerson.hasRole(RoleType.MANAGER) ? null : responsiblePerson;
	} else {
	    final Person loggedPerson = AccessControl.getPerson();
	    return (loggedPerson == null) ? null : (loggedPerson.hasRole(RoleType.MANAGER) ? null : loggedPerson);
	}
    }

    protected void init(Registration registration) {
	init(registration, null, null);
    }

    @Override
    final public IState nextState() {
	return nextState(new StateBean(defaultNextStateType().toString()));
    }

    protected RegistrationStateType defaultNextStateType() {
	throw new DomainException("error.no.default.nextState.defined");
    }

    @Override
    public IState nextState(final StateBean bean) {
	return createState(getRegistration(), bean.getResponsible(), bean.getStateDateTime(), RegistrationStateType.valueOf(bean
		.getNextState()));
    }

    @Override
    final public void checkConditionsToForward() {
	checkConditionsToForward(new RegistrationStateBean(defaultNextStateType()));
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
	checkCurriculumLinesForStateDate(bean);
    }

    private void checkCurriculumLinesForStateDate(final StateBean bean) {
	final ExecutionYear year = ExecutionYear.readByDateTime(bean.getStateDateTime());
	final RegistrationStateType nextStateType = RegistrationStateType.valueOf(bean.getNextState());

	if (nextStateType.canHaveCurriculumLinesOnCreation()) {
	    return;
	}

	if (getRegistration().hasAnyEnroledEnrolments(year)) {
	    throw new DomainException("RegisteredState.error.registration.has.enroled.enrolments.for.execution.year", year
		    .getName());
	}
    }

    @Override
    public Set<String> getValidNextStates() {
	return Collections.emptySet();
    }

    public abstract RegistrationStateType getStateType();

    public ExecutionYear getExecutionYear() {
	return ExecutionYear.readByDateTime(getStateDate());
    }

    @Checked("RegistrationStatePredicates.deletePredicate")
    public void delete() {
	RegistrationState nextState = getNext();
	RegistrationState previousState = getPrevious();
	if (nextState != null && previousState != null
		&& !previousState.getValidNextStates().contains(nextState.getStateType().name())) {
	    throw new DomainException("error.cannot.delete.registrationState.incoherentState: "
		    + previousState.getStateType().name() + " -> " + nextState.getStateType().name());
	}
	deleteWithoutCheckRules();
    }

    public void deleteWithoutCheckRules() {
	final Registration registration = getRegistration();
	try {
	    removeRegistration();
	    removeResponsiblePerson();
	    removeRootDomainObject();
	    super.deleteDomainObject();
	} finally {
	    registration.getStudent().updateStudentRole();
	}
    }

    public RegistrationState getNext() {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistration()
		.getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, DATE_COMPARATOR);
	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(); iter.hasNext();) {
	    RegistrationState state = iter.next();
	    if (state.equals(this)) {
		if (iter.hasNext()) {
		    return iter.next();
		}
		return null;
	    }
	}
	return null;
    }

    public RegistrationState getPrevious() {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistration()
		.getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, DATE_COMPARATOR);
	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(sortedRegistrationsStates.size()); iter
		.hasPrevious();) {
	    RegistrationState state = iter.previous();
	    if (state.equals(this)) {
		if (iter.hasPrevious()) {
		    return iter.previous();
		}
		return null;
	    }
	}
	return null;
    }

    public DateTime getEndDate() {
	RegistrationState state = getNext();
	return (state != null) ? state.getStateDate() : null;
    }

    public void setStateDate(YearMonthDay yearMonthDay) {
	super.setStateDate(yearMonthDay.toDateTimeAtMidnight());
    }

    public static class RegistrationStateDeleter extends VariantBean implements FactoryExecutor {

	public RegistrationStateDeleter(Integer idInternal) {
	    super();
	    setInteger(idInternal);
	}

	@Override
	public Object execute() {
	    RootDomainObject.getInstance().readRegistrationStateByOID(getInteger()).delete();
	    return null;
	}
    }

    public static class RegistrationStateCreator extends RegistrationStateBean implements FactoryExecutor {

	public RegistrationStateCreator(Registration registration) {
	    super(registration);
	}

	private RegistrationStateCreator(Registration reg, Person responsible, DateTime creation, RegistrationStateType stateType) {
	    this(reg);
	    setResponsible(responsible);
	    setStateDateTime(creation);
	    setStateType(stateType);
	}

	public static RegistrationState createState(Registration reg, Person responsible, DateTime creation,
		RegistrationStateType stateType) {
	    return (RegistrationState) new RegistrationStateCreator(reg, responsible, creation, stateType).execute();
	}

	@Override
	public Object execute() {
	    RegistrationState createdState = null;

	    final RegistrationState previousState = getRegistration().getStateInDate(getStateDateTime());
	    if (previousState == null) {
		createdState = RegistrationState.createState(getRegistration(), null, getStateDateTime(), getStateType());
	    } else {
		createdState = (RegistrationState) StateMachine.execute(previousState, this);
	    }
	    createdState.setRemarks(getRemarks());

	    final RegistrationState nextState = createdState.getNext();
	    if (nextState != null && !createdState.getValidNextStates().contains(nextState.getStateType().name())) {
		throw new DomainException("error.cannot.add.registrationState.incoherentState");
	    }

	    return createdState;
	}

    }

    public boolean isActive() {
	return getStateType().isActive();
    }

    public boolean includes(final ExternalEnrolment externalEnrolment) {
	if (getStateType() == RegistrationStateType.MOBILITY) {
	    final DateTime mobilityDate = getStateDate();
	    return externalEnrolment.hasExecutionPeriod() && externalEnrolment.getExecutionYear().containsDate(mobilityDate);
	}

	throw new DomainException("RegistrationState.external.enrolments.only.included.in.mobility.states");
    }

    static public boolean hasAnyState(final Collection<RegistrationState> states, final Collection<RegistrationStateType> types) {
	for (final RegistrationState state : states) {
	    if (types.contains(state.getStateType())) {
		return true;
	    }
	}

	return false;
    }

}
