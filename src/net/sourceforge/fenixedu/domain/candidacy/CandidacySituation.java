package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.workflow.CandidacyOperation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

import org.joda.time.DateTime;

// FIXME: Rename to CandidacyState
public abstract class CandidacySituation extends CandidacySituation_Base implements IState {

    public static Comparator<CandidacySituation> DATE_COMPARATOR = new Comparator<CandidacySituation>() {
	public int compare(CandidacySituation leftCandidacySituation,
		CandidacySituation rightCandidacySituation) {
	    int comparationResult = leftCandidacySituation.getSituationDate().compareTo(
		    rightCandidacySituation.getSituationDate());
	    return (comparationResult == 0) ? leftCandidacySituation.getIdInternal().compareTo(
		    rightCandidacySituation.getIdInternal()) : comparationResult;
        }
    };

    protected CandidacySituation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSituationDate(new DateTime());
        setOjbConcreteClass(this.getClass().getName());
    }
    
    protected final void init(final Candidacy candidacy, final Person person) {
	checkParameters(candidacy, person);
	super.setCandidacy(candidacy);
	super.setPerson(person);
	if (canExecuteOperationAutomatically()) {
	    try {
		// This is to be able to save the candidacy situations dates
                // different along the candidacy workflow.
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		throw new DomainException("error.candidacy.CandidacySituation.cannot.execute.operation");
	    }
	    getOperations().iterator().next().execute(person);
	}
    }

    private Set<Operation> getOperations() {
	return getCandidacy().getOperations(this);
    }

    public void executeSingleOperation() {

    }

    private void checkParameters(Candidacy candidacy, Person person) {
	if (candidacy == null) {
	    throw new DomainException("error.candidacy.CandidacySituation.candidacy.cannot.be.null");
	}

	if (person == null) {
	    throw new DomainException("error.candidacy.CandidacySituation.person.cannot.be.null");
	}
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.candidacy.CandidacySituation.cannot.modify.person");
    }

    @Override
    public void setCandidacy(Candidacy candidacy) {
	throw new DomainException("error.candidacy.CandidacySituation.cannot.modify.candidacy");
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources").getString(
		getCandidacySituationType().getQualifiedName());
    }

    public boolean canChangePersonalData() {
        return false;
    }
    
    public boolean getCanCandidacyDataBeValidated() {
        return false;
    }

    public boolean getCanGeneratePass() {
        return true;
    }
    
    public boolean getCanRegister() {
        return false;
    }

    public Collection<Operation> getOperationsForPerson(Person person) {
	final Collection<Operation> operationsForPerson = new HashSet<Operation>();
	for (final Operation operation : getOperations()) {
	    if (operation.isAuthorized(person)) {
		operationsForPerson.add(operation);
	    }
	}

	return operationsForPerson;
    }

    public Operation getOperationByTypeAndPerson(CandidacyOperationType type, Person person) {
	for (final Operation operation : getOperationsForPerson(person)) {
	    if (((CandidacyOperation) operation).getType() == type) {
		return operation;
	    }
	}

	return null;
    }

    public void onOperationFinished(Operation operation, Person person) {
	getCandidacy().moveToNextState(((CandidacyOperation) operation).getType(), person);
    }

    public abstract void nextState();

    public abstract void checkConditionsToForward();

    public abstract void nextState(String nextState);

    public abstract void checkConditionsToForward(String nextState);

    public abstract Set<String> getValidNextStates();

    public abstract CandidacySituationType getCandidacySituationType();

    public abstract boolean canExecuteOperationAutomatically();
}
